package something.ru.newsreader.view.mainScreen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface MainView extends MvpView {

    void showNewsListView();

    void showNewsContentView(String newsId);

    void exitFromApp();

    void closeNewsContentView();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void init();
}
