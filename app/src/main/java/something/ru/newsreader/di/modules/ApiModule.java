package something.ru.newsreader.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.api.deserializers.NewsContentDeserializer;
import something.ru.newsreader.model.api.deserializers.NewsDeserializer;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;


@Module
public class ApiModule {
    private static final String BASE_URL = "https://api.tinkoff.ru/v1/";
    private static final int CONNECTION_TIMEOUT_SEC = 3;

    @Provides
    public IApiService api(Retrofit retrofit) {
        return retrofit.create(IApiService.class);
    }

    @Provides
    public Retrofit retrofit(String baseUrl,
                             @Named("defaultOkHttp") OkHttpClient client,
                             RxJava2CallAdapterFactory rxJava2CallAdapterFactory,
                             GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public String baseUrl() {
        return BASE_URL;
    }

    @Named("defaultOkHttp")
    @Provides
    public OkHttpClient defOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();
    }

    @Named("interceptOkHttp")
    @Provides
    public OkHttpClient intOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }


    @Provides
    public RxJava2CallAdapterFactory rxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }


    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public Gson gson(@Named("newsType") Type newsType,
                     @Named("newsDeserializer") JsonDeserializer<News> newsDeserializer,
                     @Named("newsContentType") Type newsContentType,
                     @Named("newsContentDeserializer") JsonDeserializer<NewsContent> contentDeserializer) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(newsType, newsDeserializer);
        gsonBuilder.registerTypeAdapter(newsContentType, contentDeserializer);
        return gsonBuilder.create();
    }

    @Named("newsType")
    @Provides
    public Type newsType() {
        return News.class;
    }

    @Named("newsDeserializer")
    @Provides
    public JsonDeserializer<News> newsDeserializer() {
        return new NewsDeserializer();
    }

    @Named("newsContentType")
    @Provides
    public Type newsContentType() {
        return NewsContent.class;
    }

    @Named("newsContentDeserializer")
    @Provides
    public JsonDeserializer<NewsContent> newsContentDeserializer() {
        return new NewsContentDeserializer();
    }


}
