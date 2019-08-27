package com.accepted.acceptedtalentplanet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by kwonhong on 2017-12-03.
 */

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public LruBitmapCache(int maxSize){
        super(maxSize);
    }

    public LruBitmapCache(Context ctx){
        this(getCaheSize(ctx));
    }

    public static int getCaheSize(Context ctx){
        final DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;

        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }

    @Override
    protected int sizeOf(String key, Bitmap value){
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url){
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap){
        put(url, bitmap);
    }
}
