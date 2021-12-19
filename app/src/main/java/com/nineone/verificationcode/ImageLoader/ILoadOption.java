package com.nineone.verificationcode.ImageLoader;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

public interface ILoadOption {

    /**
     * 加载图片时占位图片
     *
     * @param res 占位图片的资源id
     */
    void placeholder(@RawRes @DrawableRes @Nullable Integer res);

    /**
     * 加载图片的图像比例类型
     *
     * @param scaleType 具体值为 @link ImageScaleType枚举
     */
    void scaleType(ImageScaleType scaleType);

    /**
     * 加载失败时默认图
     *
     * @param resourceId 加载失败时的默认资源id
     */
    void error(@DrawableRes int resourceId);

    /**
     * 加载圆角
     *
     * @param dp 圆角的dp值
     */
    void round(float dp);

    /**
     * 加载圆角
     *
     * @param leftTopDp     左上圆角dp值
     * @param rightTopDp    右上圆角dp值
     * @param rightBottomDp 右下圆角dp值
     * @param leftBottomDp  左下圆角dp值
     */
    void round(float leftTopDp, float rightTopDp, float rightBottomDp, float leftBottomDp);

    /**
     * 加载图片的缓存设置
     *
     * @param cacheType 具体值参考 ImageCacheType
     */
    void cacheType(ImageCacheType cacheType);

    /**
     * 加载图片的超时设置
     *
     * @param timeoutMs 超时时间设置
     */
    void timeout(@IntRange(from = 0) int timeoutMs);
}
