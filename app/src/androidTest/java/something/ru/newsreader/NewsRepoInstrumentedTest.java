package something.ru.newsreader;

import android.support.annotation.NonNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import something.ru.newsreader.di.TestComponent;
import something.ru.newsreader.di.modules.ApiModule;
import something.ru.newsreader.model.entity.NewsContent;
import something.ru.newsreader.model.repo.NewsContentRepo;

import static org.junit.Assert.assertEquals;

public class NewsRepoInstrumentedTest {
    private static final String NEWS_ID = "10123";
    private static final String NEWS_CONTENT_CONTENT = "test text";
    private static final String NEWS_CONTENT_TYPE_ID = "usual";
    private static final long NEWS_CONTENT_CREATION_DATE = new Date().getTime();
    private static final long NEWS_CONTENT_MODIFY_DATE = new Date().getTime();
    private static final Integer NEWS_CONTENT_BANK_INFO_TYPE_ID = 1;

    private static MockWebServer mockWebServer;

    @Inject
    NewsContentRepo newsContentRepo;

    @BeforeClass
    public static void setupClass() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        mockWebServer.shutdown();
    }

    @Before
    public void setup() {
        TestComponent component = DaggerTestComponent
                .builder()
                .apiModule(new ApiModule() {
                    @Override
                    public String baseUrl() {
                        return mockWebServer.url("/").toString();
                    }
                }).build();
        component.inject(this);
    }

    @Test
    public void loadAllNews() {
        mockWebServer.enqueue(createNewsResponse());
        TestObserver<NewsContent> newsContentObserver = new TestObserver<>();
        newsContentRepo.getNewsContent(NEWS_ID).subscribe(newsContentObserver);

        newsContentObserver.awaitTerminalEvent();
        newsContentObserver.assertValueCount(1);

        NewsContent newsContent = newsContentObserver.values().get(0);

        assertEquals(newsContent.getId(), NEWS_ID);
        assertEquals(newsContent.getContent(), NEWS_CONTENT_CONTENT);
        assertEquals(newsContent.getCreationDate().getTime(), NEWS_CONTENT_CREATION_DATE);
        assertEquals(newsContent.getLastModificationDate().getTime(), NEWS_CONTENT_MODIFY_DATE);
        assertEquals(newsContent.getBankInfoTypeId(), NEWS_CONTENT_BANK_INFO_TYPE_ID);
        assertEquals(newsContent.getTypeId(), NEWS_CONTENT_TYPE_ID);
    }


    @NonNull
    private MockResponse createNewsResponse() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"resultCode\":\"OK\",\"payload\":{\"title\":{\"id\":\"");
        stringBuilder.append(NEWS_ID);
        stringBuilder.append("\",\"name\":\"test\",\"text\":\"testText\",\"publicationDate\":{\"milliseconds\":1513767691000},\"bankInfoTypeId\":2},\"creationDate\":{\"milliseconds\":");
        stringBuilder.append(NEWS_CONTENT_CREATION_DATE);
        stringBuilder.append("},\"lastModificationDate\":{\"milliseconds\":");
        stringBuilder.append(NEWS_CONTENT_MODIFY_DATE);
        stringBuilder.append("},\"content\":\"");
        stringBuilder.append(NEWS_CONTENT_CONTENT);
        stringBuilder.append("\",\"bankInfoTypeId\":");
        stringBuilder.append(NEWS_CONTENT_BANK_INFO_TYPE_ID);
        stringBuilder.append(",\"typeId\":\"");
        stringBuilder.append(NEWS_CONTENT_TYPE_ID);
        stringBuilder.append("\"},\"trackingId\":\"79819640629\"}");
        return new MockResponse().setBody(stringBuilder.toString());
    }
}
