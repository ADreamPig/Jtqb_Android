package com.ningsheng.jietong.Utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 图片加载
 * Created by zhang on 2015/11/26.
 */
public class MediaUtil {


    private static ImageLoader IMAGE_LOADER;
    private static DisplayImageOptions DISPLAY_IMAGE_OPTIONS;
    private static ImageLoader IMAGE_LOADER_VALUABLE;
    private static DisplayImageOptions DISPLAY_IMAGE_OPTIONS_VALUABLE;
    private static ImageLoader IMAGE_LOADER_HEAD;
    private static DisplayImageOptions DISPLAY_IMAGE_OPTIONS_HEAD;
    private static ImageLoader IMAGE_LOADER_OPTIONS_AIBUM_QQ_QUICK;
    private static DisplayImageOptions DISPLAY_IMAGE_OPTIONS_AIBUM_QQ_QUICK;

    private MediaUtil() {

    }


    /**
     * 从指定的uri加载图片
     *
     * @param uri
     * @param imageView
     */
    public static final void displayImage(Context context, String uri, ImageView imageView) {
        if (IMAGE_LOADER == null) {
            IMAGE_LOADER = ImageLoader.getInstance();
            IMAGE_LOADER.init(ImageLoaderConfiguration.createDefault(context));
            DISPLAY_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true)
                    .resetViewBeforeLoading(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        }
        IMAGE_LOADER.displayImage(uri, imageView, DISPLAY_IMAGE_OPTIONS);
    }

    /**
     * 比较宝贵的图片,常用图标
     *
     * @param uri
     * @param imageView
     */
    public static final void displayImageValuable(Context context, String uri, ImageView imageView, int imageOnLoading, int imageForEmptyUri, int imageOnFail) {
        if (IMAGE_LOADER_VALUABLE == null) {
            IMAGE_LOADER_VALUABLE = ImageLoader.getInstance();
            File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Valuable");
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).diskCache(new UnlimitedDiskCache(cacheDir)).build();
            IMAGE_LOADER_VALUABLE.init(configuration);
            DISPLAY_IMAGE_OPTIONS_VALUABLE = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(imageOnLoading).showImageForEmptyUri(imageForEmptyUri).showImageOnFail(imageOnFail)
                    .resetViewBeforeLoading(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        }
        IMAGE_LOADER_VALUABLE.displayImage(uri, imageView, DISPLAY_IMAGE_OPTIONS_VALUABLE);
    }

    /**
     * 比较宝贵的图片,头像
     *
     * @param uri
     * @param imageView
     */
    public static final void displayImageHead(Context context, String uri, ImageView imageView) {
        if (IMAGE_LOADER_HEAD == null) {
            IMAGE_LOADER_HEAD = ImageLoader.getInstance();
            File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/HEAD");
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).diskCache(new UnlimitedDiskCache(cacheDir)).build();
            IMAGE_LOADER_HEAD.init(configuration);
        }
        if (DISPLAY_IMAGE_OPTIONS_HEAD == null) {
            DISPLAY_IMAGE_OPTIONS_HEAD = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(R.mipmap.wode_photo).showImageForEmptyUri(R.mipmap.wode_photo).showImageOnFail(R.mipmap.wode_photo)
                    .resetViewBeforeLoading(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        }
        IMAGE_LOADER_HEAD.displayImage(uri, imageView, DISPLAY_IMAGE_OPTIONS_HEAD);
    }

    /**
     * 自定义相册专用
     *
     * @param uri
     * @param imageView
     */
    public static void displayImageQqQuick(String uri, ImageView imageView) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited())
            initImageLoaderConfiguration2ImageQqQuick(MyApplication.getInstance());
        if (DISPLAY_IMAGE_OPTIONS_AIBUM_QQ_QUICK == null) {
            DISPLAY_IMAGE_OPTIONS_AIBUM_QQ_QUICK = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.ALPHA_8)
                    .resetViewBeforeLoading(true).showImageOnLoading(R.mipmap.ic_picture_loadfailed).showImageOnFail(R.mipmap.ic_picture_loadfailed).showImageForEmptyUri(R.mipmap.ic_picture_loadfailed)
                    .build();
        }
        imageLoader.displayImage(uri, imageView, DISPLAY_IMAGE_OPTIONS_AIBUM_QQ_QUICK);
    }

    public static void initImageLoaderConfiguration2ImageQqQuick(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(100, 100).diskCacheExtraOptions(100, 100, null).threadPoolSize(5).diskCacheFileCount(300).memoryCache(new LRULimitedMemoryCache((int) (Runtime.getRuntime().maxMemory() / 4))).
                diskCache(new UnlimitedDiskCache(new File(FileUtil.getPerfectionPath(context), "imageQqQuick"))).build();
        if (ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().destroy();
        ImageLoader.getInstance().init(configuration);
    }

    public static void initImageLoaderDisplayImageOptions2ImageQqQuick(boolean isCacheInMemory, boolean isCacheOnDisk) {
        DISPLAY_IMAGE_OPTIONS_AIBUM_QQ_QUICK = new DisplayImageOptions.Builder()
                .cacheInMemory(isCacheInMemory).cacheOnDisk(isCacheOnDisk).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.ALPHA_8)
                .resetViewBeforeLoading(true).showImageOnLoading(R.mipmap.ic_picture_loadfailed).showImageOnFail(R.mipmap.ic_picture_loadfailed).showImageForEmptyUri(R.mipmap.ic_picture_loadfailed)
                .build();
    }


}



