package something.ru.newsreader.model.repo;

import io.reactivex.Maybe;
import io.reactivex.Single;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.ApiResponse;
import something.ru.newsreader.model.entity.NewsContent;

public class NewsContentRepo implements INetworkContentRepo {
    private final IApiService apiService;
    private final IDatabaseService databaseService;

    public NewsContentRepo(IApiService apiService, IDatabaseService databaseService) {
        this.apiService = apiService;
        this.databaseService = databaseService;
    }

    @Override
    public Single<NewsContent> getNewsContent(String newsId) {
        Maybe<NewsContent> remoteData = apiService
                .getNewsContent(newsId)
                .filter(newsContentApiResponse -> newsContentApiResponse.getResultCode().equals("OK"))
                .map(ApiResponse::getPayload)
                .doOnSuccess(newsContent -> databaseService
                        .insertOrUpdateNewsContent(newsContent)
                        .subscribe());

        Maybe<NewsContent> localData = databaseService
                .getNewsContent(newsId)
                .filter(newsContent -> newsContent.isLoaded())
                .doOnError(throwable -> {
                    //logging  local error
                });

        return Maybe
                .concat(remoteData, localData)
                .first(new NewsContent());
    }
}
