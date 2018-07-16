package something.ru.newsreader.view.fragment.newsContent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
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
    @BindView(R.id.srf_fr_news_content_root_view)
    SwipeRefreshLayout rootView;
    @BindView(R.id.sv_fr_news_content_news_scroll)
    ScrollView newsContentScrollView;
    @BindView(R.id.tv_fr_news_content_news_text)
    TextView newsText;
    @BindView(R.id.tv_fr_news_content_modify_text)
    TextView newDateText;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        rootView.setEnabled(false);

        Bundle args = getArguments();
        if (args != null) {
            newsContentPresenter.getNewsContent(args.getString(NEWS_ID_BUNDLE_KEY));
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void showNewsContent(String content, String lastModificationDate) {
        newsText.setText(Html.fromHtml(content));
        newDateText.setVisibility(View.VISIBLE);
        newDateText.setText(lastModificationDate);
    }

    @Override
    public void showLoading() {
        rootView.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        rootView.setRefreshing(false);
    }

    @Override
    public void showErrorDataLoadMessage() {
        showMessageWithRetryLoad(getString(R.string.error_data_load_message));
    }


    @Override
    public void showEmptyDataNoNetworkMessage() {
        showMessageWithRetryLoad(getString(R.string.error_empty_data_no_network));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showMessageWithRetryLoad(String message) {
        final Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE);
        snackbar
                .setAction(R.string.news_list_fr_snackbar_action_text_retry, view -> {
                    newsContentPresenter.retryLoad();
                    snackbar.dismiss();
                });
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }
}

