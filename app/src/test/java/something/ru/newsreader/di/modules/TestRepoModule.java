package something.ru.newsreader.di.modules;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.networkStatus.android.NetworkStatus;
import something.ru.newsreader.model.repo.NewsContentRepo;

@Module
public class TestRepoModule {
    @Provides
    public NewsContentRepo newsContentRepo() {
        return Mockito.mock(NewsContentRepo.class);
    }

    @Provides
    public INetworkStatus networkStatus() {
        return new NetworkStatus();
    }
}