package demo.com.demo.ui;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HomeAbstractModule {

    @Binds
    protected abstract HomeMVPContract.Presenter binds(HomePresenter presenter);

}
