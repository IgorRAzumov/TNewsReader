package something.ru.newsreader.view.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import something.ru.newsreader.R;
import something.ru.newsreader.presenter.MainViewPresenter;
import something.ru.newsreader.view.fragment.newsContent.NewsContentFragment;
import something.ru.newsreader.view.fragment.newsList.NewsListFragment;


public class MainActivity extends MvpAppCompatActivity implements MainView,
        NewsListFragment.OnFragmentInteractionListener {
    @BindView(R.id.tb_activity_main_toolbar)
    Toolbar toolbar;

    @InjectPresenter
    MainViewPresenter mainViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void init() {
        toolbar.setTitle(getString(R.string.main_activity_toolbar_title));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        mainViewPresenter.onBackPressed(isNewsListFragmentVisible());
    }

    @Override
    public void onNewsClick(String newsId) {
        mainViewPresenter.onNewsClick(newsId);
    }

    @Override
    public void showNewsListView() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_activity_main_frame, NewsListFragment.newInstance(), NewsListFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void showNewsContentView(String newsId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment newsListFragment = fragmentManager
                .findFragmentByTag(NewsListFragment.TAG);

        fragmentManager
                .beginTransaction()
                .detach(newsListFragment)
                .add(R.id.fl_activity_main_frame, NewsContentFragment.newInstance(newsId),
                        NewsContentFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void closeNewsContentView() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment newsContentFragment = fragmentManager
                .findFragmentByTag(NewsContentFragment.TAG);

        Fragment newsListFragment = fragmentManager
                .findFragmentByTag(NewsListFragment.TAG);

        fragmentManager
                .beginTransaction()
                .remove(newsContentFragment)
                .attach(newsListFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }

    @Override
    public void exitFromApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private boolean isNewsListFragmentVisible() {
        Fragment newsListFragment = getSupportFragmentManager()
                .findFragmentByTag(NewsListFragment.TAG);
        return newsListFragment != null && newsListFragment.isVisible();
    }
}
