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
        Realm realm = Realm.getDefaultInstance();
        return realm
                .where(News.class)
                .sort("publicationDate", Sort.DESCENDING)
                .findAllAsync();
    }

    @Override
    public Completable insertOrUpdateNews(List<News> newsList) {
        return Completable.fromAction(() -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(innerRealm -> {
                    realm.insertOrUpdate(newsList);
                });
            }
        });
    }


    @Override
    public Completable insertOrUpdateNewsContent(NewsContent newsContent) {
        return Completable
                .create(completableEmitter -> {
                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(innerRealm -> {
                            innerRealm.insertOrUpdate(newsContent);
                        });
                        completableEmitter.onComplete();
                    }
                });
    }

    @Override
    public Maybe<NewsContent> getNewsContent(String newsId) {
        return Maybe.create(emitter -> {
            try (Realm realm = Realm.getDefaultInstance()) {
                NewsContent newsContent = realm
                        .where(NewsContent.class)
                        .equalTo("newsId", newsId)
                        .findFirst();
                if (newsContent == null) {
                    emitter.onComplete();
                } else {
                    emitter.onSuccess(realm.copyFromRealm(newsContent));
                }
            }
        });
    }
}
