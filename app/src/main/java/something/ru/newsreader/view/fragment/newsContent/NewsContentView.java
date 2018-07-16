package something.ru.newsreader.view.fragment.newsContent;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface NewsContentView extends MvpView  {

    @StateStrategyType(SkipStrategy.class)
    void init();

    void showNewsContent(String content, String lastModificationDate);

    void showLoading();

    void hideLoading();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showEmptyDataNoNetworkMessage();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showErrorDataLoadMessage();
}
