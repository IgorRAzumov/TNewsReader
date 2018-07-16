package something.ru.newsreader.model.api;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import something.ru.newsreader.model.entity.ApiResponse;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;


public interface IApiService {
    @GET("news_content")
    Single<ApiResponse<NewsContent>> getNewsContent(@Query("id") String id);

    @GET("news")
    Single<ApiResponse<List<News>>> getAllNews();
}
