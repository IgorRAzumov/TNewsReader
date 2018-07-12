package something.ru.newsreader.model.database;

import java.util.List;

import io.reactivex.Completable;
import io.realm.RealmResults;
import something.ru.newsreader.model.entity.News;
import something.ru.newsreader.model.entity.NewsContent;

public interface IDatabaseService {

    RealmResults<News> getNews();

    Completable insertOrUpdateNews(List<News> newsList);


    NewsContent getNewsContent(String newsId);

    Completable insertOrUpdateNewsContent(NewsContent newsContent);
}
