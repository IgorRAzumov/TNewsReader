package something.ru.newsreader.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.realm.RealmChangeListener;
import something.ru.newsreader.model.entity.NewsContent;
import something.ru.newsreader.model.repo.NewsRepo;
import something.ru.newsreader.view.fragment.newsContent.NewsContentView;

@InjectViewState
public class NewsContentPresenter extends MvpPresenter<NewsContentView> {
    @Inject
    NewsRepo newsRepo;

    private String currentNewsId;
    private NewsContent currentNews;
    RealmChangeListener<NewsContent> listener = newsContent -> {

    };

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void setCurrentNewsId(String currentNewsId) {
        this.currentNewsId = currentNewsId;

        newsRepo
                .getNewsContent(currentNewsId)
                .addChangeListener(listener);
    }

}
