package demo.com.demo.ui;

import com.google.gson.Gson;

import javax.inject.Inject;

import demo.com.demo.network.Api;
import demo.com.demo.network.NewtorkConnection;
import demo.com.demo.network.OnNetworkComplete;
import demo.com.demo.network.response.Response;

public class HomePresenter implements HomeMVPContract.Presenter, OnNetworkComplete {

    private String mQuery;
    private int pageCount = 1;
    private boolean stopPageIncrement;
    private boolean loadMoreData;

    @Inject
    protected HomeMVPContract.View view;

    @Inject
    public HomePresenter() {

    }

    @Override
    public void onError() {
        view.onEmptyResults(null);
        view.showProgress();
    }

    @Override
    public void onSuccess(String response) {
        view.hideProgress();
        Response photosResponse = new Gson().fromJson(response, Response.class);
        if (photosResponse != null && photosResponse.getPhotos() != null &&
                photosResponse.getPhotos().getPhoto() != null && !photosResponse.getPhotos().getPhoto().isEmpty()) {
            if (loadMoreData) {
                view.moreDataLoaded(photosResponse.getPhotos().getPhoto());
                stopPageIncrement = pageCount > photosResponse.getPhotos().getPages();
                if (!stopPageIncrement)
                    ++pageCount;
                loadMoreData = false;
            } else {
                view.onResult(photosResponse.getPhotos().getPhoto());
            }
        } else {
            view.onEmptyResults(null);
        }
    }

    @Override
    public void onQueryChange(String query) {
        mQuery = query;
        view.showProgress();
        new NewtorkConnection(this).execute(Api.SEARCH_API + pageCount + "&text=" + query);
    }

    @Override
    public void loadMoreItems() {
        loadMoreData = true;
        if (!stopPageIncrement)
            new NewtorkConnection(this).execute(Api.SEARCH_API + pageCount + "&text=" + mQuery);
    }
}
