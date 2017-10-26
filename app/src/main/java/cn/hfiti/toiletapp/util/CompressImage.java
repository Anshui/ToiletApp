package cn.hfiti.toiletapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CompressImage {
	// 质量压缩方法
			public Bitmap compressImage(Bitmap image) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这�?100表示不压缩，把压缩后的数据存放到baos�?
				int options = 100;
				while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大�?100kb,大于继续压缩
					baos.reset();// 重置baos即清空baos
					image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
					options -= 10;// 每次都减�?10
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream�?
				Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
				return bitmap;
			}

			// 图片按比例大小压缩方法（根据路径获取图片并压缩）
			public Bitmap getimage(String srcPath) {
				BitmapFactory.Options newOpts = new BitmapFactory.Options();
				// �?始读入图片，此时把options.inJustDecodeBounds 设回true�?
				newOpts.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

				newOpts.inJustDecodeBounds = false;
				int w = newOpts.outWidth;
				int h = newOpts.outHeight;
				// 现在主流手机比较多是800*480分辨率，�?以高和宽我们设置�?
				float hh = 800f;// 这里设置高度�?800f
				float ww = 480f;// 这里设置宽度�?480f
				// 缩放比�?�由于是固定比例缩放，只用高或�?�宽其中�?个数据进行计算即�?
				int be = 1;// be=1表示不缩�?
				if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
					be = (int) (newOpts.outWidth / ww);
				} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
					be = (int) (newOpts.outHeight / hh);
				}
				if (be <= 0)
					be = 1;
				newOpts.inSampleSize = be;// 设置缩放比例
				// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
				bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
				return bitmap;// 压缩好比例大小后再进行质量压�?
			}

			// 图片按比例大小压缩方法（根据Bitmap图片压缩�?
			public Bitmap comp(Bitmap image) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
					baos.reset();// 重置baos即清空baos
					image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos�?
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
				BitmapFactory.Options newOpts = new BitmapFactory.Options();
				// �?始读入图片，此时把options.inJustDecodeBounds 设回true�?
				newOpts.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
				newOpts.inJustDecodeBounds = false;
				int w = newOpts.outWidth;
				int h = newOpts.outHeight;
				// 现在主流手机比较多是800*480分辨率，�?以高和宽我们设置�?
				float hh = 800f;// 这里设置高度�?800f
				float ww = 480f;// 这里设置宽度�?480f
				// 缩放比�?�由于是固定比例缩放，只用高或�?�宽其中�?个数据进行计算即�?
				int be = 1;// be=1表示不缩�?
				if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩�?
					be = (int) (newOpts.outWidth / ww);
				} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩�?
					be = (int) (newOpts.outHeight / hh);
				}
				if (be <= 0)
					be = 1;
				newOpts.inSampleSize = be;// 设置缩放比例
				// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false�?
				isBm = new ByteArrayInputStream(baos.toByteArray());
				bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
				return compressImage(bitmap);// 压缩好比例大小后再进行质量压�?
			}
}
