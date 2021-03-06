# About
安卓高斯模糊库，参考了[Blurry](https://github.com/wasabeef/Blurry)和[500px-android-blur](https://github.com/500px/500px-android-blur)，并做了扩展和优化，非常感谢以上两个开源库的开发者

# Gradle
[![](https://jitpack.io/v/zj565061763/blur.svg)](https://jitpack.io/#zj565061763/blur)

# BlurApi
```java
public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private BlurApi mBlurApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur);
        mImageView = findViewById(R.id.imageview);
    }

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(this);
            /**
             * 设置当前对象在模糊方法被调用之后是否自动释放资源
             * 当API版本支持RenderScript的时候，如果需要频繁的模糊操作，可以持有BlurApi对象，并设置为false，避免一直创建对象，效率会高很多
             * 在最后需要销毁的地方销毁BlurlApi对象即可
             */
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    @Override
    public void onClick(final View view)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(this);

        getBlurApi()
                .setRadius(15) // 设置模糊半径
                .setDownSampling(8) // 设置压缩倍数
                .setColor(Color.parseColor("#66FFFFFF")) // 设置覆盖层颜色
                .blur(bitmap)
                .async(true) // 设置是否在子线程执行
                .into(mImageView);

        // 自定义Target
        getBlurApi().blur(bitmap).async(true).into(new BlurApi.Target()
        {
            @Override
            public void onBlurred(Bitmap bitmap)
            {
                // 得到模糊后的Bitmap
            }
        });

        /**
         * 直接得到模糊后的Bitmap对象
         */
        final Bitmap bitmapBlurred = getBlurApi().blur(bitmap).bitmap();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        /**
         * 释放资源，并取消所有和该api对象关联的子线程任务
         */
        if (mBlurApi != null)
            mBlurApi.destroy();
    }
}
```

# FBlurImageView
用法和普通的ImageView一样，只不过会把设置的图片进行模糊后展示，可以设置是否在子线程进行模糊操作，默认在UI主线程
```xml
<com.sd.lib.blur.view.FBlurImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/fj5"
    app:blurAsync="true"
    app:blurColor="#99FFFFFF"
    app:blurDownSampling="8"
    app:blurRadius="10" />
```

# View动态模糊
关于View动态模糊请参考demo
<br>
[BlurLayoutActivity.java](https://github.com/zj565061763/blur/blob/master/app/src/main/java/com/sd/blur/BlurLayoutActivity.java)
<br>
[act_blur_layout.xml](https://github.com/zj565061763/blur/blob/master/app/src/main/res/layout/act_blur_layout.xml)
<br>
<br>
[BlurViewActivity.java](https://github.com/zj565061763/blur/blob/master/app/src/main/java/com/sd/blur/BlurViewActivity.java)
<br>
[act_blur_view.xml](https://github.com/zj565061763/blur/blob/master/app/src/main/res/layout/act_blur_view.xml)

# 模糊View参数设置
以上介绍的模糊View都实现了[BlurView](https://github.com/zj565061763/blur/blob/master/lib/src/main/java/com/sd/lib/blur/view/BlurView.java)接口，可以进行模糊参数设置

# ImageViewBlur
如果你不想使用BlurApi接口，也不想使用上述的FBlurImageView，那么可以使用ImageViewBlur
```java
ImageViewBlur blur = new ImageViewBlur(this);
/**
 * 如果blur对象不再被需要的时候可以调用blur.release()方法清空设置的Source和Target并释放指向blur对象的引用
 */
blur.setSource(mImageView) // 设置模糊的数据源
        .setTarget(mImageView); // 设置得到模糊数据后，要模糊的目标
```

# ViewBackgroundBlur
ViewBackgroundBlur的使用方式和ImageViewBlur一样，不同的是它监听模糊的是View的背景

# 覆盖默认参数
[default_blur_settings](https://github.com/zj565061763/blur/blob/master/lib/src/main/res/values/default_blur_settings.xml)