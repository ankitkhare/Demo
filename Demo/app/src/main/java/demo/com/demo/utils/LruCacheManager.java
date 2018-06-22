package demo.com.demo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import demo.demo.com.demo.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LruCacheManager {

    private LruCache<String, Bitmap> mMemoryCache;

    private static LruCacheManager instance;

    public static LruCacheManager getInstance() {
        if (instance == null) {
            instance = new LruCacheManager();
            instance.init();
        }

        return instance;
    }

    private void init() {
        mMemoryCache = new LruCache<String, Bitmap>(6 * 1024 * 1024) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };

    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void clear() {
        mMemoryCache.evictAll();
    }

    public void loadBitmap(String url, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromMemCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
            setImageFromUrl(imageView, url);
        }
    }

    private void setImageFromUrl(final ImageView imageView, final String urlString) {

        Observable.just(urlString)
                .map(s -> {
                    URL url;
                    try {
                        url = new URL(urlString);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        addBitmapToMemoryCache(urlString, myBitmap);
                        return myBitmap;
                    } catch (final IOException ex) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageView::setImageBitmap);
    }
}

