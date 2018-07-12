package something.ru.newsreader.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import something.ru.newsreader.view.mainScreen.MainView;

@InjectViewState
public class MainViewPresenter extends MvpPresenter<MainView> {

    private String currentNewsId;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showNewsListFragment();
    }

    public void onNewsClick(String newsId) {
        currentNewsId = newsId;
        getViewState().showNewsContentFragment(currentNewsId);
    }

    public String getCurrentNewsId() {
        return currentNewsId;
    }
}
