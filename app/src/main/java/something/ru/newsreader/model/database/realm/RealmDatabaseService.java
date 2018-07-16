package something.ru.newsreader.model.database.realm;

import java.util.ArrayList;
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
                .findAll();
    }


    @Override
    public void insertOrUpdateNews(final List<News> newsList) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(innerRealm -> realm.insertOrUpdate(createUpdateList(newsList, innerRealm)));
        }
    }

    @Override
    public Maybe<NewsContent> getNewsContent(final String newsId) {
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

    @Override
    public Completable insertOrUpdateNewsContent(NewsContent newsContent) {
        return Completable
                .create(completableEmitter -> {
                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(innerRealm -> {
                            NewsContent savedNewsContent = innerRealm
                                    .where(NewsContent.class)
                                    .equalTo("newsId", newsContent.getId())
                                    .findFirst();
                            if (savedNewsContent == null ||
                                    savedNewsContent.getLastModificationDate()
                                            .before(newsContent.getLastModificationDate())) {
                                realm.insertOrUpdate(newsContent);
                            }
                            completableEmitter.onComplete();
                        });
                    }
                });
    }

    private List<News> createUpdateList(List<News> newsList, Realm innerRealm) {
        RealmResults<News> realmNewsList = innerRealm
                .where(News.class)
                .findAll();
        if (realmNewsList.isEmpty() && realmNewsList.isLoaded()) {
            return newsList;
        }

        List<News> newsToInsertOrUpdate = new ArrayList<>();
        for (News news : newsList) {
            News realmNews = innerRealm
                    .where(News.class)
                    .equalTo("id", news.getId())
                    .findFirst();
            if (realmNews == null) {
                newsToInsertOrUpdate.add(news);
            } else {
                if (realmNews.getPublicationDate().before(news.getPublicationDate())) {
                    newsToInsertOrUpdate.add(news);
                }
            }
        }
        return newsToInsertOrUpdate;
    }
}
