package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public interface BlurInvoker
{
    /**
     * 设置是否在子线程执行
     * <br>
     * 如果在子线程执行的话，每次发起新的任务都会先取消旧的任务
     *
     * @param async
     * @return
     */
    BlurInvoker async(boolean async);

    /**
     * 模糊后设置给ImageView
     *
     * @param imageView
     * @return
     */
    BlurInvoker into(ImageView imageView);

    /**
     * 模糊后设置给view的背景
     *
     * @param view
     * @return
     */
    BlurInvoker intoBackground(View view);

    /**
     * 模糊后设置给某个目标
     *
     * @param target
     * @return
     */
    BlurInvoker into(Target target);

    /**
     * 取消异步请求
     *
     * @return
     */
    BlurInvoker cancelAsync();

    interface Target
    {
        /**
         * 模糊回调
         *
         * @param bitmap 模糊后的Bitamp，可能为null
         */
        void onBlurred(Bitmap bitmap);
    }
}
