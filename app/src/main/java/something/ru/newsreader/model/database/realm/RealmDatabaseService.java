package something.ru.newsreader.model.database.realm;

import java.util.List;

import io.reactivex.Completable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import something.ru.newsreader.model.database.IDatabaseService;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;

public class RealmDatabaseService implements IDatabaseService {

    @Override
    public RealmResults<News> getNews() {
        Realm realm = Realm.getDefaultInstance();
        return realm
                .where(News.class)
                .sort("publicationDate", Sort.DESCENDING)
                .findAll();
    }

    @Override
    public Completable insertOrUpdateNews(List<News> newsList) {
        return Completable.create((completableEmitter) -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(innerRealm -> {
                realm.insertOrUpdate(newsList);
            });
            completableEmitter.onComplete();
        });
    }

    @Override
    public NewsContent getNewsContent(String newsId) {
        Realm realm = Realm.getDefaultInstance();
        return realm
                .where(NewsContent.class)
                .equalTo("newsId", newsId)
                .findFirst();
    }

    @Override
    public Completable insertOrUpdateNewsContent(NewsContent newsContent) {
        return null;
    }

}
