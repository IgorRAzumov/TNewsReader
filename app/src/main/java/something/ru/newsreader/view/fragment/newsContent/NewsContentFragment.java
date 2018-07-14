package something.ru.newsreader.view.fragment.newsContent;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import something.ru.newsreader.App;
import something.ru.newsreader.R;
import something.ru.newsreader.presenter.NewsContentPresenter;

@StateStrategyType(AddToEndSingleStrategy.class)
public class NewsContentFragment extends MvpAppCompatFragment implements NewsContentView {
    public static final String TAG = NewsContentFragment.class.getSimpleName();
    private static final String NEWS_ID_BUNDLE_KEY = "news-id-bundle-key";

    @BindView(R.id.tv_fr_news_cont_news_text)
    TextView newsText;
    @BindView(R.id.pb_fr_news_cont_news_loading)
    ProgressBar loadingProgress;


    @InjectPresenter
    NewsContentPresenter newsContentPresenter;

    private Unbinder unbinder;


    @ProvidePresenter
    public NewsContentPresenter provideNewsContentPresenter() {
        NewsContentPresenter presenter = new NewsContentPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    public NewsContentFragment() {

    }

    public static NewsContentFragment newInstance(String newsId) {
        NewsContentFragment fragment = new NewsContentFragment();
        Bundle args = new Bundle();
        args.putString(NEWS_ID_BUNDLE_KEY, newsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        Bundle args = getArguments();
        if (args != null) {
            newsContentPresenter.getNewsContent(args.getString(NEWS_ID_BUNDLE_KEY));
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void showNewsContent(String content, Long creationDate, Long lastModificationDate) {
        newsText.setText(Html.fromHtml(content));
    }

    @Override
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void showNetworkSearchError() {

    }

    @Override
    public void showEmptyDataNoNetworkMessage() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

