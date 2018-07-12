package something.ru.newsreader.view.fragment.newsList;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface NewsListView extends MvpView{

    void showLoading();

    void hideLoading();

    void loadCompleted();

    @StateStrategyType(SkipStrategy.class)
    void showErrorResponseMessage();

    @StateStrategyType(SkipStrategy.class)
    void showErrorLoadMessage();

    @StateStrategyType(SkipStrategy.class)
    void showNewsContent(String newsId);

    @StateStrategyType(SkipStrategy.class)
    void notifyNewDataChanged();

    @StateStrategyType(SkipStrategy.class)
    void notifyItemRangeInserted(int startIndex, int length);

    @StateStrategyType(SkipStrategy.class)
    void notifyNewsRemoved(int startIndex, int length);

    @StateStrategyType(SkipStrategy.class)
    void notifyNewsChanged(int startIndex, int length);

    @StateStrategyType(SkipStrategy.class)
    void showNoNetworkEmptyDataMessage();

    @StateStrategyType(SkipStrategy.class)
    void showNoNetworkMessage();
}
