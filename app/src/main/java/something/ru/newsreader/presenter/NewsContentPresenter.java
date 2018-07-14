package something.ru.newsreader.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import something.ru.newsreader.model.entity.NewsContent;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.repo.NewsContentRepo;
import something.ru.newsreader.view.fragment.newsContent.NewsContentView;

@InjectViewState
public class NewsContentPresenter extends MvpPresenter<NewsContentView> {
    @Inject
    NewsContentRepo newsRepo;

    private NewsContent currentNews;
    private Scheduler scheduler;

    public NewsContentPresenter(Scheduler scheduler) {
        this.scheduler = scheduler;
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

                    if (newsContent.isEmpty()) {
                        emptyDataLoaded();
                    } else {
                        getViewState().showNewsContent(currentNews.getContent(),
                                currentNews.getCreationDate(),
                                currentNews.getLastModificationDate());
                    }
                }, throwable -> {
                    getViewState().hideLoading();
                    getViewState().showErrorMessage();
                });
    }

    private void emptyDataLoaded() {
        if (INetworkStatus.isOnline()) {
            getViewState().showNetworkSearchError();
        } else {
            getViewState().showEmptyDataNoNetworkMessage();
        }
    }


}
