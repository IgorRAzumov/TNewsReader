package something.ru.newsreader.model.repo;

import android.annotation.SuppressLint;

import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;
import something.ru.newsreader.model.networkStatus.INetworkStatus;

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
                .subscribe(apiResponse -> {
                            if (apiResponse.getResultCode().equals("OK")) {
                                databaseService
                                        .insertOrUpdateNews(apiResponse.getPayload())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe();
                            }
                        }
                        , throwable -> {

                        });
    }


    @SuppressLint("CheckResult")
    public NewsContent getNewsContent(String newsId) {
        if (INetworkStatus.isOnline()) {
            updateNewsContent(newsId);
        }
        return databaseService.getNewsContent(newsId);
    }

    @SuppressLint("CheckResult")
    private void updateNewsContent(String newsId) {
        apiService
                .getNewsContent(newsId)
                .subscribe(apiResponse -> {
                    if (apiResponse.getResultCode().equals("OK")) {
                        databaseService
                                .insertOrUpdateNewsContent(apiResponse.getPayload())
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                    }
                }, throwable -> {

                });
    }


}
