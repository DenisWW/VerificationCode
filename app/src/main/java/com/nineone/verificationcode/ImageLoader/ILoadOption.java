package com.nineone.verificationcode.ImageLoader;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

 interface ILoadOption {

    /**
     * 加载图片时占位图片
     *
     * @param res 占位图片的资源id
     */
    void placeholder(@RawRes @DrawableRes @Nullable Integer res);

    /**
     * 加载图片的图像比例类型
     *
     * @param scaleType 具体值为  ImageScaleType枚举
     *                  FIT_CENTER      图像完全显示，但可能不会填满整个imageview
     *                  CENTER_CROP     缩放图像填充满imageview并且裁剪额外的部分
     *                  CENTER_INSIDE   缩放图像使图像的尺寸都等于或者小于视图，图片可能不会充满imageview
     *                  CIRCLE_CROP     缩放图像充满imageview并且裁剪额外都部分，最终裁剪为圆形
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
     *                  NONE          关闭硬盘缓存操作
     *                  SOURCE        在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源
     *                  AUTOMATIC     根据原始图片数据和资源编码策略来自动选择磁盘缓存策略
     *                  ALL           表示既缓存原始图片，也缓存转换过后的图片
     *                  DATA          在资源解码前就将原始数据写入磁盘缓存
     */
    void cacheType(ImageCacheType cacheType);

    /**
     * 加载图片的超时设置
     *
     * @param timeoutMs 超时时间设置
     */
    void timeout(@IntRange(from = 0) int timeoutMs);
}
