package something.ru.newsreader.view.fragment.newsList;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;



public interface NewsListView extends MvpView{
    @StateStrategyType(AddToEndSingleStrategy.class)
    void init();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLoading();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideLoading();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNewsContent(String newsId);


    void notifyNewDataChanged();


    void notifyItemRangeInserted(int startIndex, int length);


    void notifyNewsRemoved(int startIndex, int length);


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

    @StateStrategyType(OneExecutionStateStrategy.class)
    void clearMessages();
}
