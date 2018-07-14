package something.ru.newsreader.view.fragment.newsContent;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NewsContentView extends MvpView  {

    @StateStrategyType(SkipStrategy.class)
    void init();

    void showLoading();

    void hideLoading();

    void showNetworkSearchError();

    void showEmptyDataNoNetworkMessage();

    void showNewsContent(String content, Long creationDate, Long lastModificationDate);

    void showErrorMessage();
}
