package something.ru.newsreader.model.repo;

public interface INetworkContentRepo<T> {
    T getNewsContent(String newsId);
}
