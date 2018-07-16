package something.ru.newsreader.di.modules;


import dagger.Module;
import dagger.Provides;
import something.ru.newsreader.model.networkStatus.INetworkStatus;
import something.ru.newsreader.model.networkStatus.android.NetworkStatus;

@Module
public class NetworkStatusModule {
    @Provides
    public INetworkStatus networkStatus() {
        return new NetworkStatus();
    }
}
