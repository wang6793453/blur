package com.sd.lib.blur.viewblur;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.sd.lib.blur.api.BlurApi;
import com.sd.lib.blur.api.BlurApiFactory;

import java.lang.ref.WeakReference;

abstract class BaseViewBlur<V extends View> implements ViewBlur<V>
{
    private final Context mContext;
    private BlurApi mBlurApi;

    private WeakReference<V> mSource;
    private WeakReference<V> mTarget;

    public BaseViewBlur(Context context)
    {
        mContext = context.getApplicationContext();
    }

    protected final BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(mContext);
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    private void destroyBlurApi()
    {
        if (mBlurApi != null)
            mBlurApi.destroy();
    }

    @Override
    public final ViewBlur<V> setBlurRadius(int radius)
    {
        getBlurApi().setRadius(radius);
        return this;
    }

    @Override
    public final ViewBlur<V> setBlurDownSampling(int downSampling)
    {
        getBlurApi().setDownSampling(downSampling);
        return this;
    }

    @Override
    public final ViewBlur<V> setBlurColor(int color)
    {
        getBlurApi().setColor(color);
        return this;
    }

    @Override
    public final V getTarget()
    {
        return mTarget == null ? null : mTarget.get();
    }

    @Override
    public final ViewBlur<V> setTarget(V target)
    {
        final V old = getTarget();
        if (old != target)
        {
            if (old != null)
                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);

            mTarget = target == null ? null : new WeakReference<>(target);

            if (target != null)
            {
                target.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
                notifyUpdate();
            } else
            {
                destroyBlurApi();
            }
        }
        return this;
    }

    @Override
    public final V getSource()
    {
        return mSource == null ? null : mSource.get();
    }

    @Override
    public final ViewBlur<V> setSource(V source)
    {
        final V old = getSource();
        if (old != source)
        {
            if (old != null)
            {
                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);

                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
            }

            mSource = source == null ? null : new WeakReference<>(source);

            if (source != null)
            {
                source.addOnAttachStateChangeListener(mOnAttachStateChangeListener);

                final ViewTreeObserver observer = source.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);

                notifyUpdate();
            } else
            {
                destroyBlurApi();
            }
        }
        return this;
    }

    @Override
    public void release()
    {
        setSource(null);
        setTarget(null);
        destroyBlurApi();
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            notifyUpdate();
            return true;
        }
    };

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
            notifyUpdate();
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            destroyBlurApi();
        }
    };

    private void notifyUpdate()
    {
        final V source = getSource();
        if (!isAttachedToWindow(source))
            return;

        final V target = getTarget();
        if (!isAttachedToWindow(target))
            return;

        onUpdate(source, target);
    }

    protected abstract void onUpdate(V source, V target);

    private static boolean isAttachedToWindow(View view)
    {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
