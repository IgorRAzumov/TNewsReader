package something.ru.newsreader.model.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import something.ru.newsreader.model.api.entity.NewsContentResponse;
import something.ru.newsreader.model.api.entity.NewsResponse;
import something.ru.newsreader.model.database.entity.News;

public interface IApiService {
    @GET("news")
    Single<NewsResponse<News>> getAllNews();

    @GET("news_content")
    Single<NewsContentResponse> getNewsContent(@Query("id") int id);
}
