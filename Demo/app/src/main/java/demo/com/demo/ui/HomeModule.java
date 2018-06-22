package demo.com.demo.ui;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private final HomeMVPContract.View mView;

    public HomeModule(HomeMVPContract.View view) {
        mView = view;

    }

    @Provides
    protected HomeMVPContract.View getView() {
        return mView;
    }

}
