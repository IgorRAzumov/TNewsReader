package something.ru.newsreader.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.repo.NewsRepo;
import something.ru.newsreader.view.adapters.INewsRowView;
import something.ru.newsreader.view.fragment.newsList.NewsListView;

@InjectViewState
public class NewsListPresenter extends MvpPresenter<NewsListView> implements INewsListPresenter {
    @Inject
    NewsRepo newsRepo;

    private RealmResults<News> newsRealmResults;
    private final Scheduler scheduler;
    private final OrderedRealmCollectionChangeListener<RealmResults<News>> listener;
    private boolean isDataLoading;

    public NewsListPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        listener = createListener();
        isDataLoading = false;
    }


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showLoading();
        loadNews();
    }

    @SuppressLint("CheckResult")
    private void loadNews() {
        isDataLoading = true;
        newsRepo
                .getAllNews()
                .subscribe(news -> {
                    newsRealmResults = news;
                    if (newsRealmResults.isEmpty()) {
                        getViewState().loadCompleted();

                        if (!INetworkStatus.isOnline()) {
                            isDataLoading = false;
                            getViewState().hideLoading();
                            getViewState().showNoNetworkEmptyDataMessage();
                        }
                    } else {
                        isDataLoading = false;
                        getViewState().hideLoading();
                        getViewState().loadCompleted();
                    }
                }, throwable -> {
                    isDataLoading = false;
                    getViewState().hideLoading();
                    getViewState().showErrorMessage();
                });
    }

    @Override
    public void bindNewsListRow(int position, INewsRowView rowView) {
        News news = newsRealmResults.get(position);
        if (news != null) {
            rowView.setNewsText(news.getText());
            rowView.setPublishDate(String.valueOf(news.getPublicationDate()));
        }
    }

    @Override
    public void onNewsClick(int position) {
        News news = newsRealmResults.get(position);
        if (news != null) {
            getViewState().showNewsContent(news.getId());
        }
    }

    @Override
    public void onViewAttach() {
        addNewsRealResultListener();
    }

    @Override
    public void onViewDetached() {
        removeNewsRealResultListener();
    }

    @Override
    public int getItemCount() {
        return (newsRealmResults == null || !newsRealmResults.isValid())
                ? 0
                : newsRealmResults.size();
    }


    public void updateNews() {
        newsRepo.updateNews();
    }


    private OrderedRealmCollectionChangeListener<RealmResults<News>> createListener() {
        return (news, changeSet) -> {
            if (changeSet == null) {
                getViewState().notifyNewDataChanged();
                return;
            }
            if (!news.isEmpty()) {
                getViewState().hideLoading();
            }

            OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
            for (int i = deletions.length - 1; i >= 0; i--) {
                OrderedCollectionChangeSet.Range range = deletions[i];
                NewsListPresenter.this.getViewState().notifyNewsRemoved(range.startIndex, range.length);
            }

            OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
            for (OrderedCollectionChangeSet.Range range : insertions) {
                NewsListPresenter.this.getViewState().notifyItemRangeInserted(range.startIndex, range.length);
            }

            OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
            for (OrderedCollectionChangeSet.Range range : modifications) {
                NewsListPresenter.this.getViewState().notifyNewsChanged(range.startIndex, range.length);
            }
        };
    }

    private void addNewsRealResultListener() {
        newsRealmResults.addChangeListener(listener);

    }

    private void removeNewsRealResultListener() {
        newsRealmResults.removeChangeListener(listener);
    }


}

