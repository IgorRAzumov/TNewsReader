package something.ru.newsreader.model.repo;

import android.annotation.SuppressLint;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.api.entity.NewsContentResponse;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.database.entity.News;

public class NewsRepo {
    private final IApiService apiService;
    private final IDatabaseService databaseService;

    public NewsRepo(IApiService IApiService, IDatabaseService databaseService) {
        this.apiService = IApiService;
        this.databaseService = databaseService;
    }

    @SuppressLint("CheckResult")
    public RealmResults<News> getAllNews() {
        if (INetworkStatus.isOnline()) {
            updateNews();
        }
        return databaseService.getNews();
    }

    @SuppressLint("CheckResult")
    public void updateNews() {
        apiService
                .getAllNews()
                .subscribeOn(Schedulers.io())
                .subscribe(newsNewsResponse -> {
                    if (newsNewsResponse.getResultCode().equals("OK")) {
                        databaseService
                                .insertOrUpdateNews(newsNewsResponse.getPayload())
                                .subscribe();
                    }
                }, throwable -> {
                    System.out.println();
                });
    }

    public Single<NewsContentResponse> getNewsContent() {
        return null;//apiService.getNewsContent(10024);
    }
}
