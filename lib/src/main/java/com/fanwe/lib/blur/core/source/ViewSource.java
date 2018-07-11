package com.fanwe.lib.blur.core.source;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

class ViewSource extends BaseSource<View>
{
    public ViewSource(View source)
    {
        super(source);
    }

    @Override
    public int getWidth()
    {
        return getSource().getWidth();
    }

    @Override
    public int getHeight()
    {
        return getSource().getHeight();
    }

    @Override
    public void draw(final Canvas canvas, Handler handler)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            getSource().draw(canvas);
        } else
        {
            synchronized (ViewSource.this)
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getSource().draw(canvas);
                        synchronized (ViewSource.this)
                        {
                            ViewSource.this.notifyAll();
                        }
                    }
                });

                try
                {
                    ViewSource.this.wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
