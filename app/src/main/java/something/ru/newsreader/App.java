package something.ru.newsreader;

import android.app.Application;

import io.realm.Realm;
import something.ru.newsreader.di.AppComponent;
import something.ru.newsreader.di.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {
    private AppComponent appComponent;
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(this);
        Timber.plant(new Timber.DebugTree());
        appComponent = DaggerAppComponent.builder()
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}

