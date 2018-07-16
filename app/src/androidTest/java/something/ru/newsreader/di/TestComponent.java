package something.ru.newsreader.di;


import javax.inject.Singleton;

import dagger.Component;
import something.ru.newsreader.NewsRepoInstrumentedTest;
import something.ru.newsreader.di.modules.RepoModule;

@Singleton
@Component(modules = {RepoModule.class})
public interface TestComponent {
    void inject(NewsRepoInstrumentedTest newsRepoInstrumentedTest);
}
