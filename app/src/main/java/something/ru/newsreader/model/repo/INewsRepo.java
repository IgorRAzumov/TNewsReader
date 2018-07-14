package something.ru.newsreader.model.repo;

public interface INewsRepo<T, K> {
    T getAllNews();

    public K updateNews();
}
