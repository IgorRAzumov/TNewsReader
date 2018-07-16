package something.ru.newsreader.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.repo.NewsRepo;
import something.ru.newsreader.view.adapters.INewsRowView;
import something.ru.newsreader.view.fragment.newsList.NewsListView;
import timber.log.Timber;

@InjectViewState
public class NewsListPresenter extends MvpPresenter<NewsListView> implements INewsListPresenter {
    private static final String DATE_FORMAT = "HH:mm dd-MMM-yyyy";
    private static final int NO_SAVED_POSITION = -1;

    @Inject
    NewsRepo newsRepo;
    @Inject
    INetworkStatus networkStatus;

    private final Scheduler scheduler;
    private final OrderedRealmCollectionChangeListener<RealmResults<News>> listener;
    private final SimpleDateFormat dateFormat;
    private int savedNewsPosition;
    private RealmResults<News> newsRealmResults;
    private Disposable disposable;

    public NewsListPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        listener = createListener();
        savedNewsPosition = NO_SAVED_POSITION;
        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
        loadNews();
    }

    @SuppressLint("CheckResult")
    private void loadNews() {
        getViewState().showLoading();
        newsRepo
                .getAllNews()
                .observeOn(scheduler)
                .filter(RealmResults::isLoaded)
                .subscribe(news -> {
                    newsRealmResults = news;
                    boolean isOnline = networkStatus.isOnline();

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
                            getViewState().showSavedDataNoNetworkMessage();
                        } else {
                            updateNews();
                        }
                    }
                }, throwable -> {
                    Timber.e(throwable);

                    getViewState().hideLoading();
                    if (networkStatus.isOnline()) {
                        getViewState().showErrorDataLoadNoNetworkMessage();
                    } else {
                        getViewState().showErrorDataLoadMessage();
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void updateNews() {
        newsRealmResults.removeAllChangeListeners();
        addNewsRealResultListener();

        disposable =
                newsRepo
                        .updateNews()
                        .subscribeOn(Schedulers.io())
                        .observeOn(scheduler)
                        .subscribe(() -> getViewState().hideLoading(), throwable -> {
                            Timber.e(throwable);

                            getViewState().hideLoading();
                            if (newsRealmResults != null && !newsRealmResults.isEmpty()) {
                                getViewState().showSavedDataNoNetworkMessage();
                            } else {
                                getViewState().showErrorDataLoadNoNetworkMessage();
                            }
                        });
    }

    public void viewOnPause(int position) {
        savedNewsPosition = position;
        getViewState().clearMessages();
    }

    public void viewOnResume() {
        if (savedNewsPosition != NO_SAVED_POSITION) {
            getViewState().restoreViewState(savedNewsPosition);
            savedNewsPosition = NO_SAVED_POSITION;
        }
    }

    @Override
    public void bindNewsListRow(int position, INewsRowView rowView) {
        News news = newsRealmResults.get(position);
        if (news != null) {
            rowView.setNewsText(news.getText());
            rowView.setPublishDate(dateFormat.format(news.getPublicationDate()));
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

    public void retryLoad() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        boolean fullReload = newsRealmResults != null && newsRealmResults.isValid() &&
                !newsRealmResults.isEmpty();
        if (!fullReload) {
            loadNews();
        } else {
            updateNews();
            getViewState().showLoading();
        }
    }

    private void addNewsRealResultListener() {
        newsRealmResults.addChangeListener(listener);
    }

    private void removeNewsRealResultListener() {
        if (newsRealmResults != null) {
            newsRealmResults.removeChangeListener(listener);
        }
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
}
