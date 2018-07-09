package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.fanwe.lib.blur.core.Blur;

class DrawableBlurApi extends BlurApi<Drawable>
{
    private final Drawable mDrawable;

    DrawableBlurApi(Drawable source, Blur blur)
    {
        super(source, blur);
        mDrawable = source;
    }

    @Override
    protected Bitmap blurImplemention()
    {
        return getBlur().blur(mDrawable);
    }
}
