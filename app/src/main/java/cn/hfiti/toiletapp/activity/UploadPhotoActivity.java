package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.util.DateUtil;
import cn.hfiti.toiletapp.util.ImageUtil;

public class UploadPhotoActivity extends Activity implements OnClickListener {

	private static final String TAG = "uploadImage";

	private Button uploadImage;
	private ImageView imageView, iv_back_upload_title;
	public static final int FINISH_UPLOAD_HEAD_PHOTO_USERINFO = 112099;
	private String picPath = null;
	public static UploadPhotoActivity instance;
	private String boxieImagePath = Environment.getExternalStorageDirectory()
			+ "/boxie/image/";
	private String croppedPhotoTempPath = boxieImagePath + "CroppedPhoto/";

	private static int page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
														// 一定放到setContentView之前
		setContentView(R.layout.activity_photo);
		instance = this;
		uploadImage = (Button) this.findViewById(R.id.uploadImage);
		uploadImage.setOnClickListener(this);

		page = getIntent().getIntExtra("page", 0);

		imageView = (ImageView) this.findViewById(R.id.imageView);
		iv_back_upload_title = (ImageView) findViewById(R.id.iv_back_upload_title);
		iv_back_upload_title.setOnClickListener(this);
		getImgFromTopView();

	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		return DateUtil.getDateTimeStr4FileName("IMG", "png");
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();

	}

	/**
	 * 
	 */
	private void getImgFromTopView() {
		// TODO 自动生成的方法存根
		byte[] bis = getIntent().getByteArrayExtra("tekyunPhotoNew");
		Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
		if (bitmap != null) {
			Bitmap bitmapYuanXing = ImageUtil.getRoundedCornerBitmap(bitmap,
					bitmap.getHeight() / 2);
			imageView.setImageBitmap(bitmapYuanXing);
			picPath = ImageUtil.saveBitmap(bitmapYuanXing, getPhotoFileName(),
					croppedPhotoTempPath);
			// croppedImageView.setImageResource(R.drawable.test_pic);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectImage:
			/***
			 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
			 */
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			// 回调图片类使用的
			startActivityForResult(intent, RESULT_CANCELED);
			break;
		case R.id.uploadImage:
			// ToastUtil.show(getApplicationContext(),
			// "loginid:"+LouisApplication.getAppNowUserLoginID()+";userid:"+LouisApplication.getAppNowUserID()+"pwd:"+LouisApplication.getAppNowUserPassWord(),
			// 1);
			// break;
			if (picPath != null && picPath.length() > 0) {
				// 改为byte[]上传
				/*
				 * UploadFileTask uploadFileTask = new UploadFileTask(this);
				 * uploadFileTask.execute(picPath);
				 */
				// 等待更新头像
				if (page == 0) {
					Message msg = UserInfoActivity.instance.handler
							.obtainMessage();
					msg.what = FINISH_UPLOAD_HEAD_PHOTO_USERINFO;
					msg.obj = picPath;
					msg.sendToTarget();
					instance.finish();
				} else if (page == 1) {
//					Message msg = CreatTeamActivity.instance.handler
//							.obtainMessage();
//					msg.what = FINISH_UPLOAD_HEAD_PHOTO_USERINFO;
//					msg.obj = picPath;
//					msg.sendToTarget();
//					instance.finish();
				}
			} else {
				Toast.makeText(UploadPhotoActivity.this, "图片为null",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_back_upload_title:
			UploadPhotoActivity.this.finish();
			break;
		default:
			break;
		}
	}

	protected void updateUI() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @param photodata
	 */
	private void goToCroppedPhoto(String imagePath) {
		Intent intent = new Intent(UploadPhotoActivity.this,
				CroppedPhotoActivity.class);
		intent.putExtra("imagePath", imagePath);
		UploadPhotoActivity.this.startActivity(intent);
		UploadPhotoActivity.this.finish();
	}

	/**
	 * 回调执行的方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			Log.e(TAG, "uri = " + uri);
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(uri, pojo, null,
						null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/***
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
					 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
					 */
					if (path.endsWith("jpg") || path.endsWith("png")
							|| path.endsWith("JPG") || path.endsWith("PNG")) {
						picPath = path;
						Bitmap bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						imageView.setImageBitmap(bitmap);
					} else {
						alert();
					}
				} else {
					alert();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 回调使用
		 */
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void alert() {
		showDialog(UploadPhotoActivity.this, "您选择的不是有效的图片", "确定", false);
		/*
		 * Dialog dialog = new AlertDialog.Builder(this) .setTitle("提示")
		 * .setMessage("您选择的不是有效的图片") .setPositiveButton("确定", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) { picPath = null; } })
		 * .create(); dialog.show();
		 */
	}

	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
	}

	/**
	 * 定义一个显示消息的对话框
	 * 
	 * @param context
	 * @param msg
	 * @param goHome
	 *            回到主界面
	 * @param yesStr
	 *            "确定"
	 */
	public static void showDialog(final Context context, String msg,
			String yesStr, boolean goHome) {
		// 创建一个AlertDialog.Builder对象
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT).setMessage(msg).setCancelable(
				false);
		if (goHome) {
//			builder.setPositiveButton(yesStr, new OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					if (page == 0) {
//						Intent i = new Intent(context,
//								UserInfoActivity.class);
//						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						context.startActivity(i);
//					} else if (page == 1) {
//						Intent i = new Intent(context,
//								UserInfoActivity.class);
//						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						context.startActivity(i);
//					}
//				}
//			});
		} else {
			builder.setPositiveButton(yesStr, null);
		}
		builder.create().show();
	}

}
