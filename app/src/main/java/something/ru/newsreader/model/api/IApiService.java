package something.ru.newsreader.model.api;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import something.ru.newsreader.model.entity.ApiResponse;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;

public interface IApiService {
    @GET("news")
    Single<ApiResponse<List<News>>> getAllNews();

    @GET("news_content")
    Maybe<ApiResponse<NewsContent>> getNewsContent(@Query("id") String id);
}
