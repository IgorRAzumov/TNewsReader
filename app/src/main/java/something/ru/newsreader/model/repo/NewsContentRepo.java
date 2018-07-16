package something.ru.newsreader.model.repo;

import io.reactivex.Maybe;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.ApiResponse;
import something.ru.newsreader.model.entity.NewsContent;
import timber.log.Timber;


public class NewsContentRepo {
    private final IApiService apiService;
    private final IDatabaseService databaseService;

    public NewsContentRepo(IApiService apiService, IDatabaseService databaseService) {
        this.apiService = apiService;
        this.databaseService = databaseService;
    }

    public Maybe<NewsContent> getNewsContent(String newsId) {
        Maybe<NewsContent> local = databaseService
                .getNewsContent(newsId)
                .filter(newsContent -> newsContent.isLoaded());

        return apiService
                .getNewsContent(newsId)
                .filter(newsContentApiResponse -> newsContentApiResponse.getResultCode().equals("OK"))
                .map(ApiResponse::getPayload)
                .doOnSuccess(newsContent -> databaseService
                        .insertOrUpdateNewsContent(newsContent)
                        .subscribe())
                .onErrorResumeNext(throwable -> {
                    Timber.e(throwable);
                    return local;
                })
                .switchIfEmpty(local);
    }
}
