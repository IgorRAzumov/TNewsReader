package something.ru.newsreader.view.fragment.newsList;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface NewsListView extends MvpView{
    void init();

    void showLoading();

    void hideLoading();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNewsContent(String newsId);

    @StateStrategyType(SkipStrategy.class)
    void notifyNewDataChanged();

    @StateStrategyType(SkipStrategy.class)
    void notifyItemRangeInserted(int startIndex, int length);

    @StateStrategyType(SkipStrategy.class)
    void notifyNewsRemoved(int startIndex, int length);

    @StateStrategyType(SkipStrategy.class)
    void notifyNewsChanged(int startIndex, int length);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showEmptyDataNoNetworkMessage();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showErrorDataLoadMessage();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSavedDataNoNetworkMessage();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showErrorDataLoadNoNetworkMessage();

    @StateStrategyType(SkipStrategy.class)
    void restoreViewState(int savedNewsPosition);
}
