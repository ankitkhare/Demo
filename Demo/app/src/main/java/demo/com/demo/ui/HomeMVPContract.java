package demo.com.demo.ui;

import java.util.List;

import demo.com.demo.base.BaseMVPContract;
import demo.com.demo.network.response.PhotoItem;

public class HomeMVPContract {

    interface Presenter {

        void onQueryChange(String query);

        void loadMoreItems();
    }

    interface View extends BaseMVPContract.View {

        void onResult(List<PhotoItem> items);

        void moreDataLoaded(List<PhotoItem> items);

        void onEmptyResults(String query);
    }
}
