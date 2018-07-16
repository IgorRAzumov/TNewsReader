package something.ru.newsreader.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import something.ru.newsreader.view.mainScreen.MainView;

@InjectViewState
public class MainViewPresenter extends MvpPresenter<MainView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
        getViewState().showNewsListView();
    }

    public void onNewsClick(String newsId) {
        getViewState().showNewsContentView(newsId);
    }

    public void onBackPressed(boolean isNewsListViewVisible) {
        if (isNewsListViewVisible) {
            getViewState().exitFromApp();
        } else {
            getViewState().closeNewsContentView();
        }
    }
}
