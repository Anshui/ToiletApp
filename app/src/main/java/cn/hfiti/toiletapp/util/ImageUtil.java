package cn.hfiti.toiletapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class ImageUtil {
	/**
	 * 尽量不要使用setImageBitmap或setImageResource或BitmapFactory.decodeResource来设置一张大图
	 * ， 因为这些函数在完成decode后，最终都是通过java层的createBitmap来完成的，需要消耗更多内存。
	 * 因此，改用先通过BitmapFactory.decodeStream方法，创建出一个bitmap，再将其设为ImageView的 source，
	 * decodeStream最大的秘密在于其直接调用JNI>>nativeDecodeAsset()来完成decode，
	 * 无需再使用java层的createBitmap，从而节省了java层的空间。
	 * 如果在读取时加上图片的Config参数，可以跟有效减少加载的内存，从而跟有效阻止抛out of Memory异常
	 * 另外，decodeStream直接拿的图片来读取字节码了， 不会根据机器的各种分辨率来自动适应，
	 * 使用了decodeStream之后，需要在hdpi和mdpi，ldpi中配置相应的图片资源，
	 * 否则在不同分辨率机器上都是同样大小（像素点数量），显示出来的大小就不对了。
	 */
	/*
	 * //从资源中获取Bitmap Resources res = getResources(); Bitmap bmp =
	 * BitmapFactory.decodeResource(res, R.drawable.icon);
	 */
	/**
	 * 从资源中获取Bitmap 解决内存溢出 OOM溢出处理了 有时候还是会溢出
	 * 
	 * @param resources
	 *            getResources()
	 * @param id
	 *            R.drawable.icon
	 * @return
	 */
	public static Bitmap zoomBitmapWithWidthHeightFromResources(
			Resources resources, int id, int width, int height) {

		BitmapFactory.Options opts = null;
		if (width > 0 && height > 0) {
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(resources, id, opts);
			// 计算图片缩放比例
			final int minSideLength = Math.min(width, height);
			opts.inSampleSize = computeSampleSize(opts, minSideLength, width
					* height);
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;
		}
		try {
			return BitmapFactory.decodeResource(resources, id, opts);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Bitmap → byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[] → Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * Bitmap缩放 没有做OOM溢出处理
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmapWithWidthHeight(Bitmap bitmap, int width,
			int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static Bitmap rotate(Bitmap bitmap, int angle) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		return bitmap;
	}

	/**
	 * Bitmap设置缩放大小对图片作处理 从File OOM溢出处理了 <br>
	 * 使用这个方法解决加载大图片内存溢出问题
	 * 
	 * @param dst
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmapFromFileWithWidthHeight(File dst, int width,
			int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 计算样本大小
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	// 计算初始化样本大小
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 从Uri获得文件路径
	 * 
	 * @param uri
	 * @param activity
	 * @return
	 */
	public static String getImagePathByUri(Uri uri, Activity activity) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, proj, // Which
																		// columns
																		// to
																		// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	/**
	 * 从文件路径获得Uri
	 */
	public static Uri getUriByImagePath(String picPath, Activity activity) {
		// TODO 自动生成的方法存根
		Uri mUri = Uri.parse("content://media/external/images/media");
		Uri mImageUri = null;

		Cursor cursor = activity.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			String data = cursor.getString(cursor
					.getColumnIndex(MediaStore.MediaColumns.DATA));
			if (picPath.equals(data)) {
				int ringtoneID = cursor.getInt(cursor
						.getColumnIndex(MediaStore.MediaColumns._ID));
				mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);

			}
			cursor.moveToNext();
		}
		return mImageUri;
	}

	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 最省内存的方式读取本地资源的图片 很少出现OOM
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap Resources2Bitmap(Resources resources, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = resources.openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * Bitmap2Drawable BtimapDrawable是Drawable的子类
	 * 
	 * @param resources
	 * @param bitmap
	 * @return
	 */
	public static BitmapDrawable Bitmap2Drawable(Resources resources,
			Bitmap bitmap) {
		// TODO 自动生成的方法存根
		/*
		 * //Bitmap转换成Drawable 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。 Bitmap
		 * bm=xxx; //xxx根据你的情况获取 BitmapDrawable bd= new
		 * BitmapDrawable(getResource(), bm);
		 */
		BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
		return bitmapDrawable;
	}

	/**
	 * 获得圆角图片
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            要切的像素大小
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获得圆形的图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int r = 0;
		// 取最短边做边长
		if (width < height) {
			r = width;
		} else {
			r = height;
		}
		// 构建一个bitmap
		Bitmap backgroundBm = Bitmap.createBitmap(r, r, Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBm);
		Paint p = new Paint();
		// 设置边缘光滑，去掉锯齿
		p.setAntiAlias(true);
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, p);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, p);
		return backgroundBm;
	}

	/**
	 * 获得带倒影的图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Drawable缩放 没有做OOM溢出处理
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawableWithWidthHeight(Drawable drawable,
			int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		// drawable转换成bitmap
		Bitmap oldbmp = drawable2Bitmap(drawable);
		// 创建操作图片用的Matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		// 设置缩放比例
		matrix.postScale(sx, sy);
		// 建立新的bitmap，其内容是对原bitmap的缩放后的图
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * 图片压缩方法实现 OOM溢出处理了
	 * 
	 * @param srcPath
	 *            原图地址
	 * @param finishPath
	 *            压缩后保存图片地址
	 * @param avatorpath
	 *            保存的文件夹路径
	 * @return
	 */
	public static Bitmap zoomBitmapByPathAndSaveNewImg(String srcPath,
			String finishPath, String avatorpath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int hh = 800;// 这里设置高度为800f
		int ww = 480;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据高度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap, finishPath, avatorpath);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 质量压缩
	 * 
	 * @param image
	 * @return
	 */
	private static Bitmap compressImage(Bitmap image, String finishPath,
			String avatorpath) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 60;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		try {
			File fileDir = new File(avatorpath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();// 创建文件夹
			}
			FileOutputStream out = new FileOutputStream(finishPath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 保存图片
	 * 
	 * @param bitmap
	 * @param picName
	 * @param savePath
	 *            "/sdcard/namecard/"
	 * @return
	 */

	public static String saveBitmap(Bitmap bitmap, String picName,
			String savePath) {
		// 判断存储卡是否可以用，可用进行存储
		String environmentState = Environment.getExternalStorageState();
		if (environmentState.equals(Environment.MEDIA_MOUNTED)) {
			File fileDir = new File(savePath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();// 创建文件夹cameraPhotoPath
			}
			// Log.e(TAG, "保存图片");
			File f = new File(savePath, picName);
			if (f.exists()) {
				f.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				// Log.i(TAG, "已经保存");
				return savePath + picName;

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			System.err.println("ImageUtil  sd卡不可用");
			return "ImageUtil sd卡不可用";
		}
		return "保存出错";
	}

	/**
	 * 保存图片到图库 2015年4月22日14:40:14 louisgeek
	 * 
	 * @param activity
	 * @param bmp
	 */
	public void saveImageToGallery(Activity activity, Bitmap bmp) {

		// A02保存图片到图库
		String uriGalleryStr = MediaStore.Images.Media.insertImage(
				activity.getContentResolver(), bmp, "", "");
		String pathGalleryStr = getImagePathByUri(Uri.parse(uriGalleryStr),
				activity);

		// A02最后通知图库内指定文件更新 需要的是 file:// 的uri
		activity.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
						+ pathGalleryStr)));

		// 通知指定目录更新 无效
		// sendBroadcast(new
		// Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR",
		// Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

		// 通知指定SD卡更新 4.4以上不支持 下面注释的另一个方法
		// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		// Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		Toast.makeText(activity, "保存成功，可到本地相册进行查看!", Toast.LENGTH_SHORT);
	}

	/**
	 * 保存图片到指定文件 其实是调用saveBitmap里的方法 多一个通知 使图库更新
	 * 
	 * @param context
	 * @param bmp
	 * @param photoPath
	 *            "Agronet/JournalPhoto"
	 */
	public void saveImageToGalleryOtherDirectory(Context context, Bitmap bmp,
			String photoPath) {
		// A01保存图片到指定文件
		File appDir = new File(Environment.getExternalStorageDirectory(),
				photoPath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = DateUtil.getDateTimeStr4FileName("IMG", DateUtil.JPG);
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// ToastMassage.showToast(context, "保存图片失败！"+e.getMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			// ToastMassage.showToast(context, "保存图片失败！"+e.getMessage());
			return;
		}
		String path = file.getAbsolutePath();
		/*
		 * //A01保存图片到指定文件 try { String
		 * uriGalleryOtherDirectoryStr=MediaStore.Images
		 * .Media.insertImage(context.getContentResolver(),path, fileName,
		 * null); } catch (FileNotFoundException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		// A01最后通知指定文件更新
		// ##context.sendBroadcast(new
		// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(new
		// File(path))));
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + path)));

		// 通知指定目录更新 无效
		// sendBroadcast(new
		// Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR",
		// Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

		// 通知指定SD卡更新 4.4以上不支持 下面注释的另一个方法
		// sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		// Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		Toast.makeText(context, "保存成功，可到本地相册进行查看!", Toast.LENGTH_SHORT);
	}

	// 第一种方法 需要新线程 4.1估计有错误
	public static Bitmap getBitmapFromUrlByHttpURLConnection(String strUrl) {
		Bitmap bitmap = null;
		try {
			// 初始化一个URL对象
			URL url = new URL(strUrl);
			// 获得HTTPConnection网络连接对象
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5 * 1000);
			connection.setDoInput(true);
			connection.connect();
			// 得到输入流
			InputStream is = connection.getInputStream();
			Log.i("TAG", "*********inputstream**" + is);
			bitmap = BitmapFactory.decodeStream(is);
			Log.i("TAG", "*********bitmap****" + bitmap);
			// 关闭输入流
			is.close();
			// 关闭连接
			connection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	// 第二种方法 需要新线程
	public static Bitmap getBitmapFromUrl(String strUrl) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(strUrl);
			bitmap = BitmapFactory.decodeStream(url.openStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	// 把bitmap转换成base64
	public static String getBase64PicStrFromBitmap(Bitmap bitmap,
			int bitmapQuality) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
		byte[] bytes = bStream.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	// 把base64转换成bitmap
	public static Bitmap getBitmapFromBase64PicStr(String PicString) {
		byte[] bitmapArray = null;
		try {
			bitmapArray = Base64.decode(PicString, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BitmapFactory
				.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}

	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
}
