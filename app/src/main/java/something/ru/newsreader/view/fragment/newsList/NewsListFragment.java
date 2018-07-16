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
import android.widget.TextView;

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
import something.ru.newsreader.view.adapters.NewsListAdapter;


public class NewsListFragment extends MvpAppCompatFragment implements NewsListView {
    public static final String TAG = NewsListFragment.class.getSimpleName();
    @BindView(R.id.srl_fragment_news_list_news)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_fragment_news_list_news)
    RecyclerView newsRecycler;

    @InjectPresenter
    NewsListPresenter newsListPresenter;

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
            throw new RuntimeException(context.toString()
                    + getString(R.string.fragment_interact_list_impl_error));
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
            newsListPresenter.saveNewsPosition(position);
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

    private void initRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> newsListPresenter.updateNews());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initNewsRecycler() {
        Context context = getContext();
        assert context != null;

        newsRecycler.setLayoutManager(new LinearLayoutManager(context));
        newsRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        newsListAdapter = new NewsListAdapter(newsListPresenter);
        newsRecycler.setAdapter(newsListAdapter);
    }

    private void showMessageWithRetryLoad(String message, int duration) {
        final Snackbar snackbar = Snackbar.make(swipeRefreshLayout, message, duration);
        snackbar
                .setAction(R.string.news_list_fr_snackbar_action_text_retry, view -> {
                    newsListPresenter.retryLoad();
                    snackbar.dismiss();
                });
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(getResources().getInteger(R.integer.snackbar_max_lines));
        snackbar.show();
    }

    public interface OnFragmentInteractionListener {
        void onNewsClick(String newsId);
    }
}
