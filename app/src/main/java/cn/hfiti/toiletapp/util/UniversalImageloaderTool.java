package cn.hfiti.toiletapp.util;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import cn.hfiti.toiletapp.R;

public class UniversalImageloaderTool {
	private static Context mContext;
	public static void config(Context context) {
		mContext = context;
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_empty) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_empty) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
				.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
				.delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
				.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位 
				//.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
				.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示 
				//.displayer(new RoundedBitmapDisplayer(int roundPixels)) //设置圆角图片
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context.getApplicationContext())
				.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
				.memoryCacheSize(12 * 1024 * 1024)
				.defaultDisplayImageOptions(defaultOptions)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)
				// 缓存一百张图片
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}
	
	
	public static void clearCache(){
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}
	
	/**
	 * 
	 * @param imageUrl 图片地址
	 * @param dpValue 圆角大小，如果为整圆，则传入Imageview的height或width一半大小
	 */
	public static void displayRoundedImage(String imageUrl,ImageView mImageView,int dpValue){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_empty) // 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.icon_empty) // 设置图片加载或解码过程中发生错误显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位 
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示 
		.displayer(new RoundedBitmapDisplayer(Util.dip2px(mContext, dpValue))) //设置圆角图片
		.build();
		ImageLoader.getInstance().displayImage(imageUrl, mImageView, options);
	}
	

	/**
	 * 
	 * @param imageUrl
	 *            如果是本地需要时file://开头的绝对路径 如果是相对路径 请手动加file://
	 * @param mImageView
	 */
	public static void doSimpleImageLoadWithOptions(String imageUrl,
			final ImageView mImageView) {
		// TODO Auto-generated method stub
		// 显示图片的配置
		ImageSize imageSize = new ImageSize(100, 100);
		// 图片的配置
		/**
		 * ImageScaleType EXACTLY :图像将完全按比例缩小的目标大小
		 * EXACTLY_STRETCHED:图片会缩放到目标大小完全 IN_SAMPLE_INT:图像将被二次采样的整数倍
		 * IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小 NONE:图片不会调整 //白话
		 * NONE不缩放。 NONE_SAFE根据需要以整数倍缩小图片，使得其尺寸不超过 Texture 可接受最大尺寸。
		 * IN_SAMPLE_POWER_OF_2根据需要以 2 的 n 次幂缩小图片，使其尺寸不超过目标大小，比较快的缩小方式。
		 * IN_SAMPLE_INT根据需要以整数倍缩小图片，使其尺寸不超过目标大小。 EXACTLY根据需要缩小图片到宽或高有一个与目标尺寸一致。
		 * EXACTLY_STRETCHED根据需要缩放图片到宽或高有一个与目标尺寸一致。
		 */
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		/*
		 * .showImageForEmptyUri(R.drawable.icon)
		 * .showImageOnFail(R.drawable.icon)
		 */
		.cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE_SAFE).build();

		ImageLoader.getInstance().loadImage(imageUrl, imageSize, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
						mImageView.setImageBitmap(loadedImage);
					}
				});
	}

	public static void doDisplayImageFromAllUri(String imageUrl,
			final ImageView mImageView) {
		// TODO Auto-generated method stub
		// 图片来源于SD卡
		String imgUrl = imageUrl;
		// 图片来源于SD卡
		String pathUrl = Scheme.FILE.wrap("/mnt/sdcard/image.png");
		// 图片来源于Content provider
		String contentPrividerUrl = "content://media/external/audio/albumart/13";
		// 图片来源于assets
		String assetsUrl = Scheme.ASSETS.wrap("ic_launcher.png");
		// 图片来源于 drawable
		String drawableUrl = Scheme.DRAWABLE.wrap("R.drawable.ic_launcher.png");

		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading_image)
				.showImageOnFail(R.drawable.default_image).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		ImageLoader.getInstance().displayImage(imgUrl, mImageView, options);

	}

	public static void doDisplayImageFromAllUriWithProgress(String imageUrl,
			final ImageView mImageView) {
		// TODO Auto-generated method stub
		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading_image)
				.showImageOnFail(R.drawable.default_image).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

		ImageLoader.getInstance().displayImage(imageUrl, mImageView, options,
				new SimpleImageLoadingListener(),
				new ImageLoadingProgressListener() {

					@Override
					/*
					 * public void onProgressUpdate(String arg0, View arg1, int
					 * arg2, int arg3) { // TODO Auto-generated method stub
					 * //得到图片的加载进度 }
					 */
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						// TODO Auto-generated method stub
						// 得到图片的加载进度
						System.out.println("==========imageUri:" + imageUri);
						System.out.println("==========view:" + view);

						double percentPlus = (double) current / total * 100;
						String result = String.format("%.2f", percentPlus);
						System.out.println("==========进度:" + result + "%");
					}
				});

	}

	public static void doSimpleImageLoad(String imageUrl, final ImageView mImageView) {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().loadImage(imageUrl,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
						mImageView.setImageBitmap(loadedImage);
					}
				});
	}

	public static void doLoadImage(String imageUrl, final ImageView mImageView) {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().loadImage(imageUrl,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						// TODO Auto-generated method stub
						mImageView.setImageBitmap(arg2);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});
	}
}
