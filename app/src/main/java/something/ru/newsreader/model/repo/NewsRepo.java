package something.ru.newsreader.model.repo;

import android.annotation.SuppressLint;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.ApiResponse;
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
                .subscribeOn(Schedulers.io())
                .subscribe(apiResponse -> {
                            if (apiResponse.getResultCode().equals("OK")) {
                                databaseService
                                        .insertOrUpdateNews(apiResponse.getPayload())
                                        .subscribe();
                            }
                        }
                        , throwable -> {
                            System.out.println();
                        });
    }

    public Single<ApiResponse<NewsContent>> getNewsContent() {
        return null;//apiService.getNewsContent(10024);
    }
}
