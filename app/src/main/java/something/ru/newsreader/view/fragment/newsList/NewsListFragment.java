package something.ru.newsreader.view.fragment.newsList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import something.ru.newsreader.App;
import something.ru.newsreader.R;
import something.ru.newsreader.presenter.NewsListPresenter;
import something.ru.newsreader.view.UiUtils;
import something.ru.newsreader.view.adapters.NewsListAdapter;
import timber.log.Timber;


public class NewsListFragment extends MvpAppCompatFragment implements NewsListView {
    public static final String TAG = NewsListFragment.class.getSimpleName();
    @BindView(R.id.srl_fragment_news_list_news)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_fragment_news_list_news)
    RecyclerView newsRecycler;

    @InjectPresenter
    NewsListPresenter newsListPresenter;

    private Snackbar snackbar;
    private NewsListAdapter newsListAdapter;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private Unbinder unbinder;

    @ProvidePresenter
    public NewsListPresenter provideMainPresenter() {
        NewsListPresenter presenter = new NewsListPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    public NewsListFragment() {

    }

    public static NewsListFragment newInstance() {
        return new NewsListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            RuntimeException exception = new RuntimeException(context.toString()
                    + getString(R.string.fragment_interact_list_impl_error));
            Timber.e(exception);
            throw exception;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        RecyclerView.LayoutManager layoutManager = newsRecycler.getLayoutManager();
        if (layoutManager != null) {
            int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            newsListPresenter.viewOnPause(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        newsListPresenter.viewOnResume();
    }

    @Override
    public void init() {
        initNewsRecycler();
        initRefreshLayout();
    }

    @Override
    public void restoreViewState(int savedNewsPosition) {
        newsRecycler.scrollToPosition(savedNewsPosition);
    }

    @Override
    public void showNewsContent(String newsId) {
        onFragmentInteractionListener.onNewsClick(newsId);
    }

    @Override
    public void notifyNewDataChanged() {
        newsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemRangeInserted(int startIndex, int length) {
        newsListAdapter.notifyItemRangeInserted(startIndex, length);
    }

    @Override
    public void notifyNewsRemoved(int startIndex, int length) {
        newsListAdapter.notifyItemRangeRemoved(startIndex, length);
    }

    @Override
    public void notifyNewsChanged(int startIndex, int length) {
        newsListAdapter.notifyItemRangeChanged(startIndex, length);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyDataNoNetworkMessage() {
        showMessageWithRetryLoad(getString(R.string.error_empty_data_no_network),
                Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    public void showSavedDataNoNetworkMessage() {
        showMessageWithRetryLoad(getString(R.string.error_saved_data_no_network_essage),
                Snackbar.LENGTH_LONG);
    }

    @Override
    public void showErrorDataLoadNoNetworkMessage() {
        showMessageWithRetryLoad(getString(R.string.error_data_load_no_network),
                Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    public void showErrorDataLoadMessage() {
        showMessageWithRetryLoad(getString(R.string.error_data_load_message),
                Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void clearMessages() {
        if (snackbar != null && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
        }
    }

    private void showMessageWithRetryLoad(String message, int duration) {
        clearMessages();
        snackbar = UiUtils.createSnackbar(duration, message,
                getString(R.string.news_list_fr_snackbar_action_text_retry),
                swipeRefreshLayout, v -> newsListPresenter.retryLoad());
        snackbar.show();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> newsListPresenter.retryLoad());
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_600, R.color.red_600,
                R.color.green_600, R.color.orange_600);
    }

    private void initNewsRecycler() {
        Context context = getContext();
        assert context != null;

        newsRecycler.setLayoutManager(new LinearLayoutManager(context));
        newsRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        newsListAdapter = new NewsListAdapter(newsListPresenter);
        newsRecycler.setAdapter(newsListAdapter);
    }

    public interface OnFragmentInteractionListener {
        void onNewsClick(String newsId);
    }
}
