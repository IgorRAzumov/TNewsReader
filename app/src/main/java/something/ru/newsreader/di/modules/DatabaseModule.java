package something.ru.newsreader.di.modules;

import dagger.Module;
import dagger.Provides;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.database.realm.RealmDatabaseService;

@Module
public class DatabaseModule {
    @Provides
    public IDatabaseService databaseService() {
        return new RealmDatabaseService();
    }
}
