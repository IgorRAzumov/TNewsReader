package something.ru.newsreader.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
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
    private AtomicBoolean isDataUpdating;

    public NewsListPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        listener = createListener();
        isDataUpdating = new AtomicBoolean();
        isDataUpdating.set(false);
    }


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
        getViewState().showLoading();
        loasdNews();
    }


    @SuppressLint("CheckResult")
    private void loasdNews() {
        newsRepo
                .getAllNews()
                .subscribe(news -> {
                    newsRealmResults = news;
                    boolean isOnline = INetworkStatus.isOnline();

                    if (newsRealmResults.isEmpty()) {
                        if (!isOnline) {
                            getViewState().hideLoading();
                            getViewState().showEmptyDataNoNetworkMessage();
                        } else {
                            updateNews();
                        }
                    } else {
                        getViewState().hideLoading();
                        if (!isOnline) {
                            getViewState().showNoNetworkForUpdateMessage();
                        } else {
                            updateNews();
                        }
                    }
                }, throwable -> {
                    getViewState().hideLoading();
                    if (!INetworkStatus.isOnline()) {
                        getViewState().showErrorMessageWithNoNetwork();
                    } else {
                        getViewState().showErrorMessage();
                    }
                });
    }

    /*addNewsRealResultListener();*/
    @SuppressLint("CheckResult")
    private void updateNews() {
        newsRealmResults.removeAllChangeListeners();
        addNewsRealResultListener();
        isDataUpdating.set(true);

        newsRepo
                .updateNNNews()
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(() -> {
                    isDataUpdating.set(false);
                    getViewState().hideLoading();
                }, throwable -> {
                    isDataUpdating.set(false);
                    getViewState().hideLoading();
                    getViewState().showErrorUpdateNewsMessage();
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
        if (newsRealmResults != null) {
            addNewsRealResultListener();
        }
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


    private OrderedRealmCollectionChangeListener<RealmResults<News>> createListener() {
        return (news, changeSet) -> {
            if (changeSet == null) {
                getViewState().notifyNewDataChanged();
                return;
            }

            OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
            for (int i = deletions.length - 1; i >= 0; i--) {
                OrderedCollectionChangeSet.Range range = deletions[i];
               getViewState().notifyNewsRemoved(range.startIndex, range.length);
            }

            OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
            for (OrderedCollectionChangeSet.Range range : insertions) {
               getViewState().notifyItemRangeInserted(range.startIndex, range.length);
            }

            OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
            for (OrderedCollectionChangeSet.Range range : modifications) {
                getViewState().notifyNewsChanged(range.startIndex, range.length);
            }
        };
    }

    private void addNewsRealResultListener() {
        newsRealmResults.addChangeListener(listener);
    }

    private void removeNewsRealResultListener() {
        if (newsRealmResults != null) {
            newsRealmResults.removeChangeListener(listener);
        }
    }


    public void retryLoad() {
        if (!isDataUpdating.get()) {
        }
    }

    public void exitButtonClick() {
        getViewState().exitFromApp();
    }
}
