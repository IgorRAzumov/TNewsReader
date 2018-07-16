package something.ru.newsreader.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import something.ru.newsreader.model.entity.NewsContent;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.repo.NewsContentRepo;
import something.ru.newsreader.view.fragment.newsContent.NewsContentView;
import timber.log.Timber;


@InjectViewState
public class NewsContentPresenter extends MvpPresenter<NewsContentView> {
    private static final String DATE_FORMAT = "HH:mm dd-MMM-yyyy";

    @Inject
    NewsContentRepo newsRepo;

    @Inject
    INetworkStatus networkStatus;

    private final Scheduler scheduler;
    private final SimpleDateFormat dateFormat;

    private NewsContent currentNews;

    public NewsContentPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
        dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    @SuppressLint("CheckResult")
    public void getNewsContent(String currentNewsId) {
        getViewState().showLoading();

        newsRepo
                .getNewsContent(currentNewsId)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(newsContent -> {
                    currentNews = newsContent;
                    getViewState().hideLoading();
                    getViewState().showNewsContent(currentNews.getContent(),
                            dateFormat.format(currentNews.getLastModificationDate()));
                }, throwable -> {
                    Timber.e(throwable);
                    noDataLoaded();
                }, this::noDataLoaded);
    }

    public void retryLoad() {
        getViewState().init();
    }

    private void noDataLoaded() {
        getViewState().hideLoading();
        if (networkStatus.isOnline()) {
            getViewState().showErrorDataLoadMessage();
        } else {
            getViewState().showEmptyDataNoNetworkMessage();
        }
    }
}
