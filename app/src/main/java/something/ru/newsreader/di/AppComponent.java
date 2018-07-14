package something.ru.newsreader.di;

import javax.inject.Singleton;

import dagger.Component;
import something.ru.newsreader.di.modules.RepoModule;
import something.ru.newsreader.presenter.NewsContentPresenter;
import something.ru.newsreader.presenter.NewsListPresenter;

@Singleton
@Component(modules = {RepoModule.class})
public interface AppComponent {
    //  void inject(MainActivity mainActivity);

    void inject(NewsListPresenter newsListPresenter);

    void inject(NewsContentPresenter newsContentPresenter);

}
