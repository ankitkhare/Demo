package demo.com.demo.ui;

import dagger.Component;
import demo.com.demo.annotations.dagger.ActivityScope;

@ActivityScope
@Component(modules = {HomeModule.class, HomeAbstractModule.class})
public interface HomeComponent {

    void inject(HomeActivity activity);

}
