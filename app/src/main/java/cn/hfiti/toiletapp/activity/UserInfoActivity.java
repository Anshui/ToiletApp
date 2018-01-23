package cn.hfiti.toiletapp.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.db.DBManager;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.util.CompressImage;
import cn.hfiti.toiletapp.util.CustomProgressDialog;
import cn.hfiti.toiletapp.util.DateUtil;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.ImageUtil;
import cn.hfiti.toiletapp.util.MyCustomDatePickerDialog;
import cn.hfiti.toiletapp.util.MyCustomPicker;
import cn.hfiti.toiletapp.util.SharedTool;

public class UserInfoActivity extends Activity implements OnClickListener{
	
	private ImageView mImgInfoBack;
	private ImageView mImgInfoPhoto;
	private TextView mTextInfoIdName;
	private TextView mTextInfoName;
	private TextView mTextInfoSex;
	private TextView mTextInfoBirth;
	private TextView mTextInfoHigh;
	private TextView mTextInfoWeight;
	private TextView mTextInfoSubmit;
	private DBManager dbManager;
	
	private String mIdName = null;
	private String mName = null;
	private String mSex = null;
	private String mBrithday = null;
	private float mHeight = 0;
	private float mWeight = 0;
	
	private AlertDialog alertdDialog;
	private CustomProgressDialog progressDialog;
	public static UserInfoActivity instance;
	private String environmentState = Environment.getExternalStorageState();
	public final static int REQUESTCODE_GET_IMAGE = 10002;
	public final static int REQUESTCODE_TAKE_PHOTO = 10000;
	private CompressImage compress = new CompressImage();
	private String ToiletImagePath = Environment.getExternalStorageDirectory()
			+ "/Toilet/image/";
	private String cameraPhotoPath = ToiletImagePath + "CameraPhoto/";
	private String albumPhotoTempPath = ToiletImagePath + "AlbumPhoto/";
    private SharedTool sharedTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_info);
		init();
		initInfo();
	}

	private void initInfo() {
		// TODO Auto-generated method stub
		ArrayList<UserInfo> infoListName = new ArrayList<UserInfo>();
        infoListName = Define.dbManager.searchData(sharedTool.getSharedString("userIdName", null));
        Log.d("yuhao", "UserInfoActivity-----infoListName=--------"+infoListName);
        String result = "";
        if (infoListName.size()!=0) {
        	for (UserInfo info : infoListName) {
                result = result + info.userIdName + "|" + info.userPwd
                         + "|" + info.userName + "|" + info.userSex + "|" + info.userBrithday + "|" + 
                		info.userHeight+ "|" + info.userWeight;
                result = result + "\n" + "------------------------------------------" + "\n";
                mTextInfoIdName.setText(info.userIdName);
                mTextInfoName.setText(info.userName);
                mTextInfoSex.setText(info.userSex);
                mTextInfoBirth.setText(info.userBrithday);
                mTextInfoHigh.setText((int)info.userHeight+"");
                mTextInfoWeight.setText(info.userWeight+"");
            }
		}
        else {
        	Toast.makeText(UserInfoActivity.this, "数据异常！", Toast.LENGTH_SHORT).show();
		}
        Log.i("-UserInfoActivity--", result);
    }

	private void init() {
		// TODO Auto-generated method stub
		dbManager = new DBManager(this);
		instance = this;
		mImgInfoBack = (ImageView) findViewById(R.id.my_info_back);
		mImgInfoPhoto = (ImageView) findViewById(R.id.my_infomation_select_photo);
		mTextInfoIdName = (TextView) findViewById(R.id.my_infomation_username);
		mTextInfoName = (TextView) findViewById(R.id.my_infomation_name);
		mTextInfoSex = (TextView) findViewById(R.id.my_infomation_sex);
		mTextInfoBirth = (TextView) findViewById(R.id.my_infomation_birthday);
		mTextInfoHigh = (TextView) findViewById(R.id.my_infomation_height);
		mTextInfoWeight = (TextView) findViewById(R.id.my_infomation_weight);
		mTextInfoSubmit = (TextView) findViewById(R.id.my_infomation_submit);
		mTextInfoSubmit.setVisibility(View.GONE);
		mImgInfoBack.setOnClickListener(this);
		mImgInfoPhoto.setOnClickListener(this);
		mTextInfoName.setOnClickListener(this);
		mTextInfoSex.setOnClickListener(this);
		mTextInfoBirth.setOnClickListener(this);
		mTextInfoHigh.setOnClickListener(this);
		mTextInfoWeight.setOnClickListener(this);
		mTextInfoSubmit.setOnClickListener(this);

        sharedTool = new SharedTool(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_info_back:
			finish();
			break;
		case R.id.my_infomation_select_photo:
			selectPhoto();
			break;
		case R.id.my_infomation_name:
			selectName();
			break;
		case R.id.my_infomation_sex:
			selectsex();
			break;
		case R.id.my_infomation_birthday:
			try {
				selectBrithday();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.my_infomation_height:
			selectHeight();
			break;
		case R.id.my_infomation_weight:
			selectWeight();
			break;
		case R.id.my_infomation_submit:
			submitSettings();
			break;
		default:
			break;
		}
	}

	//提交更改信息，更新数据库
	private void submitSettings() {
		// TODO Auto-generated method stub
		Log.d("yuhao", "submitSettings-----------------");
		mIdName = mTextInfoIdName.getText().toString();
		mName = mTextInfoName.getText().toString();
		mSex = mTextInfoSex.getText().toString();
		mBrithday = mTextInfoBirth.getText().toString();
		mHeight =  Float.parseFloat(mTextInfoHigh.getText().toString());
		mWeight = Float.parseFloat(mTextInfoWeight.getText().toString());
		if (mName == null) {
			Toast.makeText(getApplicationContext(), "姓名不能为空", Toast.LENGTH_LONG).show();
		}
		else {
			dbManager.updateData("user_name",mName,mIdName);
			dbManager.updateData("user_sex",mSex,mIdName);
			dbManager.updateData("user_birthday",mBrithday,mIdName);
			dbManager.updateData("user_high",mHeight,mIdName);
			dbManager.updateData("user_weight",mWeight,mIdName);
		}		
		finish();
	}

	private void selectWeight() {
		// TODO Auto-generated method stub
		LinearLayout linearLayout=new LinearLayout(instance);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER);
		
		final MyCustomPicker numberPicker=new  MyCustomPicker(instance);

		 numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
	     numberPicker.setNumberPickerDividerColor(numberPicker);
		 numberPicker.setMaxValue(300);
		 numberPicker.setMinValue(15);
		
			final MyCustomPicker numberPicker2=new MyCustomPicker(instance);
	
			numberPicker2.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		    numberPicker2.setNumberPickerDividerColor(numberPicker2);
			numberPicker2.setMaxValue(9);
			numberPicker2.setMinValue(0);
			float weight = Float.parseFloat(mTextInfoWeight.getText().toString());
			numberPicker.setValue((int)weight);
			numberPicker2.setValue((int)((weight-(int)weight)*10));
			
			TextView textView=new TextView(instance);
			textView.setText(".");
			textView.setTextSize(22);
			textView.setGravity(Gravity.CENTER);
			
			linearLayout.addView(numberPicker);
			linearLayout.addView(textView);
			linearLayout.addView(numberPicker2);
			
			alertdDialog=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
					.setView(linearLayout)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub						
	//						personalDetails.setUserWeight((float)numberPicker.getValue()+((float)numberPicker2.getValue())/10);
	//						waitSubmit();
							Log.d("yuhao", "numberPicker.getValue()=--------"+numberPicker.getValue());
							Log.d("yuhao", "numberPicker2.getValue()=--------"+numberPicker2.getValue());
							mTextInfoWeight.setText((float)numberPicker.getValue()+((float)numberPicker2.getValue())/10+"");
							mTextInfoSubmit.setVisibility(View.VISIBLE);
					}
				})
				.setNegativeButton("取消", null).create();
		
		if (!alertdDialog.isShowing()) {
			alertdDialog.show();
		}
	}

	private void selectHeight() {
		// TODO Auto-generated method stub
		final MyCustomPicker numberPicker=new  MyCustomPicker(instance);
		 numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
	     numberPicker.setNumberPickerDividerColor(numberPicker);
		 numberPicker.setMaxValue(300);
		 numberPicker.setMinValue(20);
		 float hight = Float.parseFloat(mTextInfoHigh.getText().toString());
		 numberPicker.setValue((int)hight);
		
		alertdDialog=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
				.setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						personalDetails.setUserHeight(numberPicker.getValue());
//						waitSubmit();
						mTextInfoHigh.setText(numberPicker.getValue()+"");
						mTextInfoSubmit.setVisibility(View.VISIBLE);
					}
				})
				.setNegativeButton("取消", null).create();
		
		if (!alertdDialog.isShowing()) {
			alertdDialog.show();
		}
	}

	private void selectBrithday() throws ParseException{
		Date d1 = new Date();
//		数据库拿数据
//		if (personalDetails.getUserBrithday() != null) {
//			d1 = new SimpleDateFormat("yyyy/MM/dd").parse(personalDetails.getUserBrithday());
//		} else {
//			d1 = new SimpleDateFormat("yyyy/MM/dd").parse("1990/01/01");
//		}
		String birth = mTextInfoBirth.getText().toString();
		Log.d("yuhao", "birth=---------------"+birth);
		d1 = new SimpleDateFormat("yyyy-MM-dd").parse(birth);

		String str_year = new SimpleDateFormat("yyyy").format(d1);
		String str_month = new SimpleDateFormat("MM").format(d1);
		String str_day = new SimpleDateFormat("dd").format(d1);
		int int_year = 0;
		int int_month = 0;
		int int_day = 0;
		Date date = new Date();

		try {
			int_year = Integer.parseInt(str_year);
			int_month = Integer.parseInt(str_month)-1;
			int_day = Integer.parseInt(str_day);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mTextInfoBirth.setText(year + "-" + String.format("%02d", (month + 1)) + "-"
                        + String.format("%02d", dayOfMonth));
						mTextInfoSubmit.setVisibility(View.VISIBLE);
            }
        }, int_year, int_month - 1, int_day);
        datePickerDialog.getDatePicker().setMaxDate(date.getTime());
        datePickerDialog.getDatePicker().setMinDate(date.getTime() - (long) 47304 * 100000000);
        datePickerDialog.show();

//		MyCustomDatePickerDialog datePickerDialog = new MyCustomDatePickerDialog(instance,
//				new DatePickerDialog.OnDateSetListener() {
//
//					@Override
//					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////						提交数据库
////						personalDetails.setUserBrithday(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
////								+ String.format("%02d", dayOfMonth));
////						waitSubmit();
//						mTextInfoBirth.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
//								+ String.format("%02d", dayOfMonth));
//						mTextInfoSubmit.setVisibility(View.VISIBLE);
//					}
//				}, int_year, int_month - 1, int_day);
//		datePickerDialog.setDatePickerDividerColor(datePickerDialog.getDatePicker(), this);
//		datePickerDialog.getDatePicker().setMaxDate(date.getTime());
//		datePickerDialog.getDatePicker().setMinDate(date.getTime() - (long) 47304 * 100000000);
//		datePickerDialog.show();
    }

	private void selectsex() {
		// TODO Auto-generated method stub
		final String[] sexs={"男","女"};		
		final MyCustomPicker numberPicker=new  MyCustomPicker(instance);
		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
	    numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setDisplayedValues(sexs);
		numberPicker.setMaxValue(sexs.length-1);
		numberPicker.setMinValue(0);		
		alertdDialog=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
				.setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						personalDetails.setUserSex(sexs[numberPicker.getValue()]);
//						提交数据库
//						waitSubmit();
						mTextInfoSex.setText(sexs[numberPicker.getValue()]);
						mTextInfoSubmit.setVisibility(View.VISIBLE);
					}
				})
				.setNegativeButton("取消", null).create();
		if (!alertdDialog.isShowing()) {
			alertdDialog.show();
		}
	}

	private void selectName() {
		// TODO Auto-generated method stub
		View window=getLayoutInflater().inflate(R.layout.dialog_step_target_enter, null);
		TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
		tv_title.setText("请输入姓名");
		final TextView step = (TextView) window.findViewById(R.id.tv_dialog_edit);
		//此处从数据库取姓名
		//step.setText(personalDetails.getUserName());
		step.setText("");
		alertdDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setView(window)
				.setPositiveButton("确定", null)
				.setNegativeButton("取消", null).create();
		if (!alertdDialog.isShowing()) {
			alertdDialog.show();
		}
		alertdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = step.getText().toString();				
//				if(!name.isEmpty()&&name!=personalDetails.getUserName()){
//					personalDetails.setUserName(name);
//					alertdDialog.dismiss();
//					waitSubmit();
//				}
				alertdDialog.dismiss();
				mTextInfoName.setText(name);
				mTextInfoSubmit.setVisibility(View.VISIBLE);
			}
		});
	}

	private void selectPhoto() {
		// TODO Auto-generated method stub
		alertdDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setItems(new String[] { "照相", "本地文件" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								if (which == 0) {
									getPhotoByCameraZoom();
								} else {
									getPhotoFromAlbum();
								}
							}
						}).create();
		if (!alertdDialog.isShowing()) {
			alertdDialog.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			/*
			 * case REQUEST_CODE_BACK_LOGIN_L:
			 * LogUtil.i("louis==app==REQUEST_CODE_BACK_LOGIN_L");
			 * goToUserInfo(); break;
			 */
			case REQUESTCODE_GET_IMAGE:
				getPhotoByCameraZoomBack(data);
				// ToastMessage.showShort(this, "REQUESTCODE_GET_IMAGE");
				break;
			case REQUESTCODE_TAKE_PHOTO:
				// backPhotoByCamera();
				// LogPrintUtil.i(TAG,"TAKE_PHOTO");
				getPhotoFromAlbumBack(data);
				// ToastMessage.showShort(this, "REQUESTCODE_TAKE_PHOTO");
				break;
			}
		}
	}
	private void getPhotoFromAlbumBack(Intent dataIntent) {
		// TODO Auto-generated method stub
		Uri imgUri2 = dataIntent.getData();
		if (imgUri2 == null) {
			// 部分相机返回uri为空 这时候另外处理
			// use bundle to get data
			Bundle bundle = dataIntent.getExtras();
			if (bundle != null) {
				Bitmap bitmap = (Bitmap) bundle.get("data"); // get bitmap
				Bitmap newbp = compress.comp(bitmap);
				// spath :生成图片取个名字和路径包含类型
				String imagePath2 = ImageUtil.saveBitmap(newbp,
						getPhotoFileName(), cameraPhotoPath);
				clearBitmap(newbp);
				clearBitmap(bitmap);
				goToCropped(imagePath2);

				// saveImage(Bitmap photo, String spath);
			} else {
				Toast.makeText(getApplicationContext(), "err****",
						Toast.LENGTH_LONG).show();
				return;
			}
		} else {
			String imagePath2 = ImageUtil.getImageAbsolutePath(instance,
					imgUri2);
			Bitmap bitmap = compress.getimage(imagePath2);
			String picPath = ImageUtil.saveBitmap(bitmap, getPhotoFileName(),
					cameraPhotoPath);
			clearBitmap(bitmap);
			goToCropped(picPath);

		}
	}
	/***/
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				updateUI();
				progressDialog.dismiss();
//				changeBtn.setVisibility(View.GONE);
				break;
			case 101:
				progressDialog.dismiss();
				Toast.makeText(instance, "暂无数据!", Toast.LENGTH_SHORT).show();
				break;
			case 401:
				progressDialog.dismiss();
				Toast.makeText(instance, "网络异常!", Toast.LENGTH_SHORT).show();
				break;
			case UploadPhotoActivity.FINISH_UPLOAD_HEAD_PHOTO_USERINFO:
				CroppedPhotoActivity.instance.finish();
				String imgSrcPath = msg.obj.toString();
//				changeHeadPhotoByPhotoPath(imgSrcPath);
				break;
			default:
				break;
			}
		};
	};
	private void getPhotoByCameraZoomBack(Intent dataIntent) {
		// TODO Auto-generated method stub
		Uri imgUri = dataIntent.getData(); // 获得图片的uri
		if (imgUri == null) {
			// 部分相机返回uri为空 这时候另外处理
			Bundle bundle = dataIntent.getExtras();
			if (bundle != null) {
				Bitmap bitmap = bundle.getParcelable("data");

				Bitmap newbp = compress.comp(bitmap);
				// spath :生成图片取个名字和路径包含类型
				String imagePath = ImageUtil.saveBitmap(newbp,
						getPhotoFileName(), cameraPhotoPath);
				// 这个方法是根据Uri获取Bitmap图片的静态方法
				// bitmap =
				// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
				// mImageCaptureUri);
				clearBitmap(newbp);
				clearBitmap(bitmap);
				goToCropped(imagePath);
			} else {
				Toast.makeText(getApplicationContext(),
						"err****imgUri=" + imgUri, Toast.LENGTH_LONG).show();
				return;
			}
		} else {
			// String imagePath=ImageUtil.getImagePathByUri(imgUri,
			// MainActivity.this);//华为返回uri格式和一般手机不一致 不能使用
			Bitmap bitmapxx = null;
			String path = getPath(imgUri);
			bitmapxx = lessenUriImage(path);
			/*
			 * int resultWidth=800; int
			 * resultHeight=bitmapxx.getHeight()*resultWidth
			 * /bitmapxx.getWidth(); Bitmap
			 * bitmapxxNew=ImageUtil.zoomBitmapWithWidthHeight
			 * (bitmapxx,resultWidth,resultHeight);
			 */
			// 不进行裁剪
			String picPath = ImageUtil.saveBitmap(bitmapxx, getPhotoFileName(),
					albumPhotoTempPath);
			clearBitmap(bitmapxx);
			goToCropped(picPath);
			// Bitmap bitmapxxNew=ImageUtil.zoomBitmapWithWidthHeight(bitmapxx,
			// 400, 400);//解决!!! FAILED BINDER TRANSACTION !!!
			// goToCroppedWithBitmap(bitmapxxNew);
		}
	}

	private Bitmap lessenUriImage(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be; // 重新读入图片，注意此时已经把 options.inJustDecodeBounds
									// 设回 false 了
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + " " + h); // after zoom
		return bitmap;
	}

	private String getPath(Uri uri) {
		// TODO Auto-generated method stub
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	protected void updateUI() {
		// TODO Auto-generated method stub
		
	}

	private void goToCropped(String imagePath) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(UserInfoActivity.this, CroppedPhotoActivity.class);
		// 绑定数据
		intent.putExtra("imagePath", imagePath);
		intent.putExtra("page", 0);
		startActivity(intent);
		// MainActivity.this.finish();
	}

	private void clearBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	private String getPhotoFileName() {
		// TODO Auto-generated method stub
		return DateUtil.getDateTimeStr4FileName("IMG", "png");
	}

	protected void getPhotoFromAlbum() {
		// TODO Auto-generated method stub
		Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);// Intent.ACTION_PICK
		intentAlbum.setDataAndType(
				MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
		// File(albumPhotoTempPath)));
		startActivityForResult(intentAlbum, REQUESTCODE_TAKE_PHOTO);
	}

	protected void getPhotoByCameraZoom() {
		// TODO Auto-generated method stub
		if (environmentState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intentCameraZoom = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);//
			startActivityForResult(intentCameraZoom, REQUESTCODE_GET_IMAGE);
		} else {
			Toast.makeText(getApplicationContext(),"错误",
					Toast.LENGTH_LONG).show();
		}
	}

}
