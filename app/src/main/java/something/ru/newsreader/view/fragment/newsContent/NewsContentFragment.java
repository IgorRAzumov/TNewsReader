package something.ru.newsreader.view.fragment.newsContent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import something.ru.newsreader.R;
import something.ru.newsreader.presenter.NewsContentPresenter;

@StateStrategyType(AddToEndSingleStrategy.class)
public class NewsContentFragment extends MvpAppCompatFragment implements NewsContentView {
    private static final String NEWS_ID_BUNDLE_KEY = "news-id-bundle-key";

    @InjectPresenter
    NewsContentPresenter newsContentPresenter;

    private Unbinder unbinder;
    private OnFragmentInteractionListener onFragmentInteractionListener;

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
            newsContentPresenter.setCurrentNewsId(args.getString(NEWS_ID_BUNDLE_KEY));
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnFragmentInteractionListener {

    }


}
