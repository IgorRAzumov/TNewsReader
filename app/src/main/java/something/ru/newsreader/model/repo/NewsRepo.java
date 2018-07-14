package something.ru.newsreader.model.repo;

import android.annotation.SuppressLint;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.networkStatus.INetworkStatus;

public class NewsRepo {
    private final IApiService apiService;
    private final IDatabaseService databaseService;

    public NewsRepo(IApiService IApiService, IDatabaseService databaseService) {
        this.apiService = IApiService;
        this.databaseService = databaseService;
    }

    @SuppressLint("CheckResult")
    public Single<RealmResults<News>> getAllNews() {
        if (INetworkStatus.isOnline()) {
            updateNews();
        }
        return Single
                .just(databaseService.getNews())
                .doOnError(throwable -> {
                    //databaseError
                });
    }

    @SuppressLint("CheckResult")
    public void updateNews() {
        apiService
                .getAllNews()
                .filter(apiResponse -> apiResponse.getResultCode().equals("OK"))
                .subscribeOn(Schedulers.io())
                .subscribe(apiResponse -> {
                            databaseService
                                    .insertOrUpdateNews(apiResponse.getPayload())
                                    .subscribe();

                        }
                        , throwable -> {
                            System.out.println();
                        }, () -> {

                        });
    }

}
