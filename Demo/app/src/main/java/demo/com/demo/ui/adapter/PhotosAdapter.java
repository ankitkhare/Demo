package demo.com.demo.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import demo.com.demo.network.response.PhotoItem;
import demo.com.demo.utils.LruCacheManager;
import demo.demo.com.demo.R;
import demo.demo.com.demo.databinding.ImageItemBinding;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private final List<PhotoItem> mItems;
    private final LruCacheManager cacheManager;

    public PhotosAdapter(List<PhotoItem> mItems) {
        setHasStableIds(true);
        this.mItems = mItems;
        cacheManager = LruCacheManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ImageItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.image_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageItemBinding mBinding;

        public ViewHolder(ImageItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(PhotoItem item) {
            String url = "http://farm" + item.getFarm() + ".static.flickr.com/" + item.getServer() + "/" + item.getId() + "_" + item.getSecret() + ".jpg";
            cacheManager.loadBitmap(url, mBinding.img);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
