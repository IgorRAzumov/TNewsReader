package something.ru.newsreader.view.fragment.newsList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    @BindView(R.id.pb_fragment_news_list_loading)
    ProgressBar loadingProgress;

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
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void loadCompleted() {
        initNewsRecycler();
        initRefreshLayout();
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
    public void showErrorLoadMessage() {

    }

    @Override
    public void showNoNetworkEmptyDataMessage() {

    }


    @Override
    public void showErrorMessage() {

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
        swipeRefreshLayout.setOnRefreshListener(() -> {
            newsListPresenter.updateNews();
        });
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
