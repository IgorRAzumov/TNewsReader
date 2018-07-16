package something.ru.newsreader.di;

import javax.inject.Singleton;

import dagger.Component;
import something.ru.newsreader.di.modules.TestRepoModule;
import something.ru.newsreader.presenter.NewsContentPresenter;

@Singleton
@Component(modules = {TestRepoModule.class})
public interface TestComponent {
    void inject(NewsContentPresenter presenter);

}
