package something.ru.newsreader.di.modules;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import something.ru.newsreader.model.repo.NewsContentRepo;

@Module
public class TestRepoModule {
    @Provides
    public NewsContentRepo newsContentRepo() {
        return Mockito.mock(NewsContentRepo.class);
    }
}