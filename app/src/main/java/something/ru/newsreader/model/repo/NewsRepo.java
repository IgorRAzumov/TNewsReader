package something.ru.newsreader.model.repo;

import android.annotation.SuppressLint;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.RealmResults;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.News;
import timber.log.Timber;


public class NewsRepo {
    private static final String NEWS_RESPONSE_CODE_OK = "OK";
    private static final String NO_RESULTS_ERROR = "error load news(empty)";
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
    public Completable updateNews() {
        return Completable.create(emitter -> apiService
                .getAllNews()
                .filter(apiResponse -> apiResponse.getResultCode().equals(NEWS_RESPONSE_CODE_OK))
                .subscribe(apiResponse -> {
                            databaseService.insertOrUpdateNews(apiResponse.getPayload());
                            emitter.onComplete();
                        }
                        , Timber::e
                        , () -> emitter.onError(new RuntimeException(NO_RESULTS_ERROR))));
    }
}
