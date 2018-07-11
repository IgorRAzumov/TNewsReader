package something.ru.newsreader.model.database.realm;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;

public class RealmDatabaseService implements IDatabaseService {

    @Override
    public RealmResults<News> getNews() {
        RealmResults<News> realmNews;
        Realm realm = Realm.getDefaultInstance();
        realmNews = realm
                .where(News.class)
                .sort("publicationDate", Sort.DESCENDING)
                .findAll();

        return realmNews;
    }

    @Override
    public Completable insertOrUpdateNews(List<News> newsList) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(innerRealm -> {
                realm.insertOrUpdate(newsList);
            });
        });
    }

    @Override
    public NewsContent getNewsContent(int newsId) {
        return null;
    }

    @Override
    public Maybe<NewsContent> insertOrUpdateNewsContent(NewsContent newsContent) {
        return null;
    }

    @Override
    public Completable clearNewsTable() {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(innerRealm -> {
                realm
                        .where(News.class)
                        .findAll()
                        .deleteAllFromRealm();
            });

        });
    }


}
