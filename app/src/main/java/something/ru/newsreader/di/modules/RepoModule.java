package something.ru.newsreader.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import something.ru.newsreader.model.api.IApiService;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.repo.NewsContentRepo;
import something.ru.newsreader.model.repo.NewsRepo;

@Module(includes = {ApiModule.class, DatabaseModule.class, NetworkStatusModule.class})
public class RepoModule {

    @Singleton
    @Provides
    public NewsRepo newsRepo(IApiService apiService, IDatabaseService databaseService) {
        return new NewsRepo(apiService, databaseService);
    }

    @Singleton
    @Provides
    public NewsContentRepo newsContentRepo(IApiService apiService, IDatabaseService databaseService) {
        return new NewsContentRepo(apiService, databaseService);
    }

}
