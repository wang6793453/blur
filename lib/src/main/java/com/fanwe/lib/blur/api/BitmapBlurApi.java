package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.core.Blur;

public final class BitmapBlurApi extends BlurApi<Bitmap, BitmapBlurApi>
{
    private final Bitmap mBitmap;

    BitmapBlurApi(Bitmap source, Blur blur)
    {
        super(source, blur);
        mBitmap = source;
    }

    @Override
    public Bitmap blur()
    {
        return getBlur().blur(mBitmap);
    }
}
