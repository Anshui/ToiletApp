package cn.hfiti.toiletapp.activity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.util.CroppedImageView;
import cn.hfiti.toiletapp.util.ImageUtil;
import cn.hfiti.toiletapp.util.UniversalImageloaderTool;

/**
 * 裁剪头像
 * @author louisgeek
 * 2015年2月13日下午2:56:41 
 */
public class CroppedPhotoActivity extends Activity implements OnGestureListener,OnClickListener{


	private CroppedImageView croppedImageView;
	public static CroppedPhotoActivity instance;
	//==========================
    float savescale = 1;
	float savescalegd = 1;
	Bitmap mBitmap=null;
	//==========================
	private int page = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.activity_cropped_photo);
		instance = this;
		croppedImageView = (CroppedImageView) findViewById(R.id.src_pic);
		// TODO 自动生成的方法存根
		String imagePath = getIntent().getStringExtra("imagePath");
		page = getIntent().getIntExtra("page", 0);

        if(imagePath!=null&&imagePath!=""){//格式：/storage/sdcard0/Sunstar/Agronet/Image/AlbumPhoto/IMG_20150916_112347.png
			UniversalImageloaderTool.doSimpleImageLoadWithOptions("file://"+imagePath, croppedImageView);
		}else {
			Toast.makeText(CroppedPhotoActivity.this, "加载图片出错",Toast.LENGTH_SHORT).show();
		}

		findViewById(R.id.rotationImgButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Matrix matrix = new Matrix();
				matrix.setRotate(90);
				BitmapDrawable bitmapDrawable = (BitmapDrawable)croppedImageView.getDrawable();

				mBitmap = (bitmapDrawable).getBitmap();
				mBitmap = Bitmap.createBitmap(mBitmap, 0,0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
				croppedImageView.setImageBitmap(mBitmap);
			}
		});
		findViewById(R.id.croppedImgButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				// 此处获取剪裁后的bitmap
				Bitmap bitmap = croppedImageView.clip();
				//ToastMessage.showShort(CroppedPhotoActivity.this, "doCroppedPhoto");
				int resultWidth=400;
				int resultHeight=400;
				//int resultHeight=bitmapxx.getHeight()*resultWidth/bitmapxx.getWidth();
				bitmap=ImageUtil.zoomBitmapWithWidthHeight(bitmap, resultWidth, resultHeight);//2015年2月17日10:44:17   不加 图片还是会过大   后期处理
				// 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递！！！！！！！！！！！！！！！！！可用到MainActivity相册选取图片bitmap过大
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
						byte[] bitmapByte = baos.toByteArray();
						clearBitmap(mBitmap);
						clearBitmap(bitmap);
						////===================XXXXXXXXXXXXX====================================
						Intent intent = new Intent(CroppedPhotoActivity.this, UploadPhotoActivity.class);
						intent.putExtra("tekyunPhotoNew", bitmapByte);
						intent.putExtra("page", page);
						startActivity(intent);
				
			}
		});
		//===========================
	}
	
	private void clearBitmap(Bitmap bitmap){
		if(bitmap !=null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	public void onResume() {
		super.onResume();
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	/*	getMenuInflater().inflate(R.menu.main, menu);
		return true;*/
		return super.onCreateOptionsMenu(menu);
	}

	/* （非 Javadoc）
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}
	
	/* （非 Javadoc）
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO 自动生成的方法存根
		return true;
	}

	/* （非 Javadoc）
	 * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO 自动生成的方法存根
		
	}

	/* （非 Javadoc）
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO 自动生成的方法存根
		return true;
	}

	/* （非 Javadoc）
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	
	}

	/* （非 Javadoc）
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO 自动生成的方法存根
		
	}

	/* （非 Javadoc）
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}
}
