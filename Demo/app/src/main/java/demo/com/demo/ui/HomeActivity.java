package demo.com.demo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import demo.com.demo.network.response.PhotoItem;
import demo.com.demo.ui.adapter.PhotosAdapter;
import demo.com.demo.utils.LruCacheManager;
import demo.demo.com.demo.R;
import demo.demo.com.demo.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity implements HomeMVPContract.View,SearchView.OnQueryTextListener {

    private ActivityMainBinding mBinding;
    @Inject
    protected HomeMVPContract.Presenter mPresenter;
    private List<PhotoItem> mItems = new ArrayList<>();
    private PhotosAdapter adapter;
    private int offSet = 40;
    private boolean loadingMoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeComponent component = DaggerHomeComponent.builder()
                .homeModule(new HomeModule(this))
                .build();

        component.inject(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.simpleSearchView.setOnQueryTextListener(this);

        setAdapter();
        loadDataOnScroll();

    }

    private void setAdapter() {
        adapter = new PhotosAdapter(mItems);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mBinding.recylerview.setLayoutManager(mLayoutManager);
        mBinding.recylerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.recylerview.setAdapter(adapter);
    }


    private void loadDataOnScroll() {
        mBinding.recylerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int scrollUp) {
                super.onScrolled(recyclerView, dx, scrollUp);
                if (scrollUp > 0) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();

                    int threshHold = mItems.size() - offSet;
                    if (threshHold > 0 && threshHold < lastItem && !loadingMoreData) {
                        loadingMoreData = true;
                        updateRecyclerView();
                    }
                }
            }
        });
    }

    private void updateRecyclerView() {
        mPresenter.loadMoreItems();
    }

    @Override
    public void onResult(List<PhotoItem> items) {
        mItems.clear();
        mItems.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void moreDataLoaded(List<PhotoItem> items) {
        loadingMoreData = false;
        mItems.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEmptyResults(String query) {
         mItems.clear();
        adapter.notifyDataSetChanged();
        mBinding.emptyMessage.setVisibility(View.VISIBLE);
        mBinding.emptyMessage.setText(R.string.no_data_available);
    }

    @Override
    public void hideProgress() {
        mBinding.progress.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mBinding.progress.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            mPresenter.onQueryChange(newText);
            mBinding.emptyMessage.setVisibility(View.GONE);
        }
        return false;
    }
}
