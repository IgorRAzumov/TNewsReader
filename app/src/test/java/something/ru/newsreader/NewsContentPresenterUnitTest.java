package something.ru.newsreader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import something.ru.newsreader.di.DaggerTestComponent;
import something.ru.newsreader.di.TestComponent;
import something.ru.newsreader.di.modules.TestRepoModule;
import something.ru.newsreader.model.entity.NewsContent;
import something.ru.newsreader.model.repo.NewsContentRepo;
import something.ru.newsreader.presenter.NewsContentPresenter;
import something.ru.newsreader.view.fragment.newsContent.NewsContentView;

public class NewsContentPresenterUnitTest {
    private static final String NEWS_ID = "10123";
    private static final String NEWS_CONTENT_CONTENT = "test text";
    private static final String NEWS_CONTENT_TYPE_ID = "usual";
    private static final Date NEWS_CONTENT_CREATION_DATE = new Date();
    private static final Date NEWS_CONTENT_MODIFY_DATE = new Date();
    private static final Integer NEWS_CONTENT_BANK_INFO_TYPE_ID = 1;
    private static final long SCHEDULER_SEC_DELAY = 3;
    private static final String TEST_ERROR = "test_error";

    @Mock
    NewsContentView newsContentView;

    private NewsContentPresenter presenter;
    private TestScheduler testScheduler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxJavaPlugins.setIoSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        testScheduler = new TestScheduler();
        presenter = Mockito.spy(new NewsContentPresenter(testScheduler, NEWS_ID));
    }

    @Test
    public void onFirstViewAttach() {
        injectNewsContent(provideTestNewsContent());
        presenter.attachView(newsContentView);
        Mockito.verify(newsContentView).init();
    }

    @Test
    public void showNewsContent() {
        injectNewsContent(provideTestNewsContent());
        presenter.attachView(newsContentView);
        testScheduler.advanceTimeBy(SCHEDULER_SEC_DELAY, TimeUnit.SECONDS);
        Mockito.verify(newsContentView).showLoading();
        Mockito.verify(newsContentView).hideLoading();

    }

    @Test
    public void retryLoad() {

    }

    @Test
    public void loadNewsContentEmptyData() {

    }

    @Test
    public void loadNewsContentError() {

    }

    private void injectNewsContent(NewsContent newsContent) {
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public NewsContentRepo newsContentRepo() {
                        NewsContentRepo repo = super.newsContentRepo();
                        Mockito.when(repo.getNewsContent(NEWS_ID))
                                .thenReturn(Maybe.just(newsContent));
                        return repo;
                    }
                }).build();
        component.inject(presenter);
    }


    private void injectErrorWhenLoadNewsContent(RuntimeException exception) {
        TestComponent component = DaggerTestComponent.builder()
                .testRepoModule(new TestRepoModule() {
                    @Override
                    public NewsContentRepo newsContentRepo() {
                        NewsContentRepo repo = super.newsContentRepo();
                        Mockito.when(repo.getNewsContent(NEWS_ID))
                                .thenReturn(Maybe.error(new RuntimeException(TEST_ERROR)));
                        return repo;
                    }
                }).build();

        component.inject(presenter);
    }

    private NewsContent provideTestNewsContent() {
        NewsContent newsContent = new NewsContent();
        newsContent.setId(NEWS_ID);
        newsContent.setContent(NEWS_CONTENT_CONTENT);
        newsContent.setTypeId(NEWS_CONTENT_TYPE_ID);
        newsContent.setCreationDate(NEWS_CONTENT_CREATION_DATE);
        newsContent.setLastModificationDate(NEWS_CONTENT_MODIFY_DATE);
        newsContent.setBankInfoTypeId(NEWS_CONTENT_BANK_INFO_TYPE_ID);
        return newsContent;
    }
}

