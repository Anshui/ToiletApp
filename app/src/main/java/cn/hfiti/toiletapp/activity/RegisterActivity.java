package cn.hfiti.toiletapp.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.entity.WeightInfo;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.MyCustomDatePickerDialog;
import cn.hfiti.toiletapp.util.MyCustomPicker;
import es.senselesssolutions.gpl.weightchart.Database;

public class RegisterActivity extends Activity implements OnClickListener{
	
	private ImageView mImgRegistBack;
	private TextView mTextBirth;
	private TextView mTextSex;
	private TextView mTextHigh;
	private TextView mTextWeight;
	private TextView mTextRegister;
	private EditText mEditPwd;
	private EditText mEditUserName;
	private EditText mEditName;
	private AlertDialog alertDialog;
	private GregorianCalendar mDateTime;
//	private UserInfo mUserInfo;
//	private UserInfoDBHelper mUserInfoDBHelper;
//	private static DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("yuhao", "RegisterActivity---------onCreate-------");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mImgRegistBack = (ImageView) findViewById(R.id.register_back);
		mTextBirth = (TextView) findViewById(R.id.register_birth);
		mTextSex = (TextView) findViewById(R.id.register_sex);
		mTextHigh = (TextView) findViewById(R.id.register_high);
		mTextWeight = (TextView) findViewById(R.id.register_weight);
		mTextRegister = (TextView) findViewById(R.id.register);
		mEditPwd = (EditText) findViewById(R.id.register_password);
		mEditUserName = (EditText) findViewById(R.id.register_username);
		mEditName = (EditText) findViewById(R.id.register_name);
		mDateTime = new GregorianCalendar();
        mDateTime.set(GregorianCalendar.SECOND, 0);
//		mUserInfo = new UserInfo();
//		dbManager = new DBManager(this);
//		mUserInfoDBHelper = new UserInfoDBHelper(this);
		
		mImgRegistBack.setOnClickListener(this);
		mTextBirth.setOnClickListener(this);
		mTextSex.setOnClickListener(this);
		mTextHigh.setOnClickListener(this);
		mTextWeight.setOnClickListener(this);
		mTextRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_back:
			finish();
			break;
		case R.id.register_birth:
			try {
				selectBrithday();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.register_sex:
			selectsex();
			break;
		case R.id.register_high:
			selectHeight();
			break;
		case R.id.register_weight:
			selectWeight();
			break;
		case R.id.register:
			registerConfirm();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("yuhao", "RegisterActivity---------onDestroy---------");
	}

	private void registerConfirm() {
		// TODO Auto-generated method stub
		String userName = mEditUserName.getText().toString();
		String password = mEditPwd.getText().toString();
		String name = mEditName.getText().toString();
		String sex = mTextSex.getText().toString();
		String birth = mTextBirth.getText().toString();
		String high = mTextHigh.getText().toString();
		String weight = mTextWeight.getText().toString();
		Log.d("yuhao", "userName=----------------"+userName);
		Log.d("yuhao", "password=----------------"+password);
		Log.d("yuhao", "name=----------------"+name);
		Log.d("yuhao", "sex=----------------"+sex);
		Log.d("yuhao", "birth=----------------"+birth);
		Log.d("yuhao", "high=----------------"+high);
		Log.d("yuhao", "weight=----------------"+weight);
		if (android.text.TextUtils.isEmpty(userName)) {
			Toast.makeText(RegisterActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
		} else if (android.text.TextUtils.isEmpty(password)) {
			Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
		} else if (android.text.TextUtils.isEmpty(name)) {
			Toast.makeText(RegisterActivity.this, "请输入姓名！", Toast.LENGTH_SHORT).show();
		} else if (android.text.TextUtils.isEmpty(sex)) {
			Toast.makeText(RegisterActivity.this, "请输入性别！", Toast.LENGTH_SHORT).show();
		} else if (android.text.TextUtils.isEmpty(birth)) {
			Toast.makeText(RegisterActivity.this, "请输入生日！", Toast.LENGTH_SHORT).show();
		} else if (android.text.TextUtils.isEmpty(high)) {
			Toast.makeText(RegisterActivity.this, "请输入身高！", Toast.LENGTH_SHORT).show();
		}
//		else if (android.text.TextUtils.isEmpty(weight)) {
//			Toast.makeText(RegisterActivity.this, "请输入体重！", Toast.LENGTH_SHORT).show();		
//		}
		else {
			Log.d("yuhao", "else--------------------------------");
			ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();
			UserInfo mUserInfo = new UserInfo();
			saveUserInfo(mUserInfo);
			userInfoList.add(mUserInfo);
			Log.d("yuhao", "userInfoList=---1---"+userInfoList);
//			dbManager.add(infoList);
			Define.dbManager.add(userInfoList);
//			insertUserData(mUserInfoDBHelper.getWritableDatabase(),
//					userName,password,name,sex,birth,high,weight);
									
			ArrayList<WeightInfo> weightInfoList = new ArrayList<WeightInfo>();
			WeightInfo mWeightInfo = new WeightInfo();
			saveWeightInfo(mWeightInfo);
			weightInfoList.add(mWeightInfo);
			Log.d("yuhao", "weightInfoList=---1----"+weightInfoList);
			Database database = new Database(RegisterActivity.this);
			Define.dbManager.add1(weightInfoList);
			Log.d("yuhao", "userInfoList=---2---"+userInfoList);
			Log.d("yuhao", "weightInfoList=---2----"+weightInfoList);
									
			Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
			LoginActivity.instance.finish();
		}
	}
	private void saveWeightInfo(WeightInfo mWeightInfo) {
		// TODO Auto-generated method stub
		mWeightInfo.setUserIdName(mEditUserName.getText().toString());
		mWeightInfo.setUserName(mEditName.getText().toString());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = format.format(new java.util.Date());
		mWeightInfo.setCompleteTime(str);
		long createdAt = (mDateTime == null ? System.currentTimeMillis() : mDateTime.getTime().getTime()) / 1000;
		Define.CREAT_TIME = createdAt;
		Log.d("yuhao", "createdAt=---------------"+createdAt);
		mWeightInfo.setGTime(createdAt);
		try {
			mWeightInfo.setUserWeight(Float.parseFloat(mTextWeight.getText().toString()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void insertUserData(SQLiteDatabase db, String userName,
			String password, String name, String sex, String birth, String high, String weight) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "insertUserData---------------");
		db.execSQL("insert into userinfo values(null,?,?,?,?,?,?,?)",
				new String[] { userName, password, name, sex, birth, high, weight});
	}

	private void saveUserInfo(UserInfo mUserInfo) {
		// TODO Auto-generated method stub
		mUserInfo.setUserIdName(mEditUserName.getText().toString());
		mUserInfo.setUserPwd(mEditPwd.getText().toString());
		mUserInfo.setUserName(mEditName.getText().toString());
		mUserInfo.setUserSex(mTextSex.getText().toString());
		mUserInfo.setUserBrithday(mTextBirth.getText().toString());
		mUserInfo.setUserHeight(Float.parseFloat(mTextHigh.getText().toString()));
		try {
			mUserInfo.setUserWeight(Float.parseFloat(mTextWeight.getText().toString()));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void selectWeight() {
		// TODO Auto-generated method stub
		LinearLayout linearLayout = new LinearLayout(RegisterActivity.this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER);

		final MyCustomPicker numberPicker = new MyCustomPicker(RegisterActivity.this);

		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setMaxValue(300);
		numberPicker.setMinValue(15);

		final MyCustomPicker numberPicker2 = new MyCustomPicker(RegisterActivity.this);

		numberPicker2.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker2.setNumberPickerDividerColor(numberPicker2);
		numberPicker2.setMaxValue(9);
		numberPicker2.setMinValue(0);
		if (!TextUtils.isEmpty(mTextWeight.getText())) {
			int weightInt = (int) (Float.parseFloat(mTextWeight.getText().toString()));
			numberPicker.setValue(weightInt);
			numberPicker2.setValue((int) ((Float.parseFloat(mTextWeight.getText().toString()) - weightInt) * 10));
		} else {
			numberPicker.setValue(60);
			numberPicker2.setValue(0);
		}

		TextView textView = new TextView(RegisterActivity.this);
		textView.setText(".");
		textView.setTextSize(22);
		textView.setGravity(Gravity.CENTER);

		linearLayout.addView(numberPicker);
		linearLayout.addView(textView);
		linearLayout.addView(numberPicker2);

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(linearLayout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTextWeight.setText(
								"" + ((float) numberPicker.getValue() + ((float) numberPicker2.getValue()) / 10));
					}
				}).setNegativeButton("取消", null).create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private void selectHeight() {
		// TODO Auto-generated method stub
		final MyCustomPicker numberPicker = new MyCustomPicker(RegisterActivity.this);
		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setMaxValue(300);
		numberPicker.setMinValue(20);
		if (!TextUtils.isEmpty(mTextHigh.getText())) {
			numberPicker.setValue(Integer.parseInt(mTextHigh.getText().toString()));

		} else {
			numberPicker.setValue(160);
		}

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTextHigh.setText(numberPicker.getValue() + "");
					}
				}).setNegativeButton("取消", null).create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private void selectsex() {
		// TODO Auto-generated method stub
		final String[] sexs = { "男", "女" };

		final MyCustomPicker numberPicker = new MyCustomPicker(RegisterActivity.this);

		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setDisplayedValues(sexs);
		numberPicker.setMaxValue(sexs.length - 1);
		numberPicker.setMinValue(0);

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTextSex.setText(sexs[numberPicker.getValue()]);
					}
				}).setNegativeButton("取消", null).create();
		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private void selectBrithday() throws ParseException {
		Date d1 = new Date();
		if (!TextUtils.isEmpty(mTextBirth.getText())) {
			d1 = new SimpleDateFormat("yyyy-MM-dd").parse(mTextBirth.getText().toString());
		} else {
			d1 = new SimpleDateFormat("yyyy-MM-dd").parse("1991-01-01");
		}

		String str_year = new SimpleDateFormat("yyyy").format(d1);
		String str_month = new SimpleDateFormat("MM").format(d1);
		String str_day = new SimpleDateFormat("dd").format(d1);
		int int_year = 0;
		int int_month = 0;
		int int_day = 0;
		Date date = new Date();

		try {
			int_year = Integer.parseInt(str_year);
			int_month = Integer.parseInt(str_month);
			int_day = Integer.parseInt(str_day);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
				mTextBirth.setText(i + "-" + String.format("%02d", (i1 + 1)) + "-" + String.format("%02d", i2));
			}
		}, int_year, int_month-1, int_day);
		datePickerDialog.getDatePicker().setMaxDate(date.getTime());
		datePickerDialog.getDatePicker().setMinDate(date.getTime() - (long) 47304 * 100000000);
		datePickerDialog.show();

//		MyCustomDatePickerDialog datePickerDialog = new MyCustomDatePickerDialog(this, new OnDateSetListener() {
//
//			@Override
//			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//				mTextBirth.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
//						+ String.format("%02d", dayOfMonth));
//			}
//		}, int_year, int_month - 1, int_day);
//		datePickerDialog.setDatePickerDividerColor(datePickerDialog.getDatePicker(), this);
//		datePickerDialog.getDatePicker().setMaxDate(date.getTime());
//		datePickerDialog.getDatePicker().setMinDate(date.getTime() - (long) 47304 * 100000000);
//		datePickerDialog.show();
	}

}
