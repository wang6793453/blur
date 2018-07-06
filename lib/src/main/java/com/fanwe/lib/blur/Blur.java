package com.fanwe.lib.blur;

import android.graphics.Bitmap;
import android.view.View;

public interface Blur
{
    /**
     * 设置模糊半径
     *
     * @param radius
     */
    void setRadius(int radius);

    /**
     * 设置压缩倍数
     *
     * @param downSampleFactor
     */
    void setDownSampleFactor(int downSampleFactor);

    /**
     * 设置覆盖层颜色
     *
     * @param colorOverlay
     */
    void setColorOverlay(int colorOverlay);

    /**
     * 设置返回的模糊Bitmap是否要保持模糊之前的宽和高
     *
     * @param keepBitmapSize
     */
    void setKeepBitmapSize(boolean keepBitmapSize);

    /**
     * 返回压缩倍数
     *
     * @return
     */
    int getDownSampleFactor();

    /**
     * 模糊Bitmap，传入的对象不会被回收
     *
     * @param bitmap
     * @return
     */
    Bitmap blur(Bitmap bitmap);

    /**
     * 得到View的模糊Bitmap
     *
     * @param view
     * @return
     */
    Bitmap blur(View view);

    /**
     * 销毁
     */
    void destroy();
}
