package something.ru.newsreader.model.repo;

import android.annotation.SuppressLint;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.RealmResults;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.News;

public class NewsRepo {
    private static final String NEWS_RESPONSE_CODE_OK = "OK";
    private final IApiService apiService;
    private final IDatabaseService databaseService;

    public NewsRepo(IApiService IApiService, IDatabaseService databaseService) {
        this.apiService = IApiService;
        this.databaseService = databaseService;
    }

    @SuppressLint("CheckResult")
    public Single<RealmResults<News>> getAllNews() {
        return Single.just(databaseService.getNews());
    }

    @SuppressLint("CheckResult")
    public Completable updateNNNews() {
        return Completable.create(emitter -> {
            apiService
                    .getAllNews()
                    .filter(apiResponse -> apiResponse.getResultCode().equals(NEWS_RESPONSE_CODE_OK))
                    .subscribe(apiResponse -> {
                                databaseService.insertOrUpdatgeNews(apiResponse.getPayload());
                                emitter.onComplete();
                            }
                            , emitter::onError
                            , () -> emitter.onError(new RuntimeException()));
        });

    }
}
