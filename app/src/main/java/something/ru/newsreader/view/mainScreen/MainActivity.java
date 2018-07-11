package something.ru.newsreader.view.mainScreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import something.ru.newsreader.R;
import something.ru.newsreader.presenter.MainViewPresenter;
import something.ru.newsreader.view.fragment.newsList.NewsListFragment;

public class MainActivity extends MvpAppCompatActivity implements MainView,
        NewsListFragment.OnFragmentInteractionListener {
    @InjectPresenter
    MainViewPresenter mainViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onNewsClick(String newsId) {
        mainViewPresenter.onNewsClick(newsId);
    }

    @Override
    public void showNewsListFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main_frame, NewsListFragment.newInstance())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void showContentFragment(String newsId) {

    }
}
