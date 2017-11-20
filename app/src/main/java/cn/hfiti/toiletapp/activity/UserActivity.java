package cn.hfiti.toiletapp.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.db.DBManager;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.entity.WeightInfo;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.MyCustomDatePickerDialog;
import es.senselesssolutions.gpl.weightchart.ChartDraw;
import es.senselesssolutions.gpl.weightchart.ChartView;
import es.senselesssolutions.gpl.weightchart.Database;
import es.senselesssolutions.gpl.weightchart.EntryListActivity;
import es.senselesssolutions.gpl.weightchart.FileCommands;
import es.senselesssolutions.gpl.weightchart.HeightDialog;
import es.senselesssolutions.gpl.weightchart.LegendActivity;

public class UserActivity extends Activity implements OnClickListener {

	private ImageView mImgUserBack;
	private ImageView mSearch;
	private LinearLayout mUserInformation;
	private TextView mMySettings;
	private TextView mUserName;
	private TextView mSex;
	private TextView mBirth;
	private TextView mHigh;
	private TextView mWeight;
	private TextView mBMI;
	private TextView mStartDate;
	private TextView mEndDate;
	private TextView mBMIAdvice;
	private DBManager dbManager;
	private LinearLayout acharWeight;
	private GregorianCalendar mDateTime;
	private DecimalFormat df = new DecimalFormat("0.00");
	public static UserActivity instance = null;
	private String select_start_time;
	private String select_end_time;
	private String today;
	private double BMIValue = 0;

	private ChartDraw mDraw;
	private Database mDatabase;
	private GestureDetector mGestureDetector;
	private HeightDialog mHeightDialog;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("yuhao", "UserActivity---------onCreate--------");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user);
		init();
		initUserInfo();
		initWeightInfo();

		mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				if (distanceX != 0) {
					mDraw.mScrollX -= distanceX;
					invalidate();
				}
				return true;
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				if (mToast != null) {
					mToast.cancel();
					mToast = null;
				}
				Log.d("yuhao", "onSingleTapUp-----------------------");
				openOptionsMenu();
				return true;
			}
		});
		mGestureDetector.setIsLongpressEnabled(false);
		if (PreferenceManager.getDefaultSharedPreferences(this).getString("weight_unit", null) == null) {
			mHeightDialog = new HeightDialog(this) {
				protected void done() {
					// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					mToast = Toast.makeText(UserActivity.this, R.string.tap_to_open_menu, Toast.LENGTH_LONG);
					mToast.show();
				}
			};
			showDialog(0);
		} else {
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDatabase.close();

		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
	}

	private void initWeightInfo() {
		// TODO Auto-generated method stub
		ArrayList<WeightInfo> infoListName = new ArrayList<WeightInfo>();
		infoListName = Define.dbManager.searchData1(Define.USER_ID_NAME);
		Log.d("yuhao", "initWeightInfo-----infoListName=--------" + infoListName);
		String result = "";
		Log.d("yuhao", "size=----------------" + infoListName.size());
		if (infoListName.size() != 0) {
			Log.d("yuhao", "size-----------------------");
			for (WeightInfo info : infoListName) {
				result = result + info.userIdName + "|" + info.userName + "|" + info.userWeight + "|" + info.time + "|"
						+ info.gTime;
				result = result + "\n" + "------------------------------------------" + "\n";
			}
		} else {
			Toast.makeText(UserActivity.this, "数据异常！", Toast.LENGTH_SHORT).show();
		}
		Log.i("yuhao--WeightInfo--", result);
	}

	protected void invalidate() {
		// TODO Auto-generated method stub
		Log.d("yuhao", "invalidate--------------------");
		((ChartView) findViewById(R.id.chart_view)).invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "onTouchEvent---------------");
		return mGestureDetector.onTouchEvent(event);
		// return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		// if (id == R.id.add) {
		// Log.d("yuhao", "add------------");
		// invalidate();
		// ArrayList<WeightInfo> weightInfoList = new ArrayList<WeightInfo>();
		// WeightInfo mWeightInfo = new WeightInfo();
		// saveWeightInfo(mWeightInfo);
		// weightInfoList.add(mWeightInfo);
		// Define.dbManager.add1(weightInfoList);
		//
		// }
		if (id == R.id.entries) {
			startActivity(new Intent(this, EntryListActivity.class));
		} else if (id == R.id.view_7_days) {
			setDays(7);
		} else if (id == R.id.view_14_days) {
			setDays(14);
		} else if (id == R.id.view_21_days) {
			setDays(21);
		} else if (id == R.id.view_30_days) {
			setDays(30);
		} else if (id == R.id.view_60_days) {
			setDays(60);
		} else if (id == R.id.view_90_days) {
			setDays(90);
		} else if (id == R.id.legend) {
			startActivity(new Intent(this, LegendActivity.class));
		} else if (!new FileCommands(this, mDatabase) {
			@Override
			protected void fillData() {
				invalidate();
			}
		}.onOptionsItemSelected(id)) {
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private void setDays(int days) {
		// TODO Auto-generated method stub
		mDraw.mScrollX *= (float) mDraw.mDays / days;
		mDraw.mDays = days;
		invalidate();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (mHeightDialog == null) {
			mHeightDialog = new HeightDialog(this) {
				protected void done() {
					getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					mToast = Toast.makeText(UserActivity.this, R.string.tap_to_open_menu, Toast.LENGTH_LONG);
					mToast.show();
				}
			};
			// showDialog(0);

		}
		return id > 0 ? mHeightDialog.createDialog(id) : createWeightUnitDialog();
	}

	private Dialog createWeightUnitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(R.string.weight_unit);
		builder.setSingleChoiceItems(R.array.weight_unit_labels, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				dialog.dismiss();
				SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(UserActivity.this)
						.edit();
				editor.putString("weight_unit", getResources().getStringArray(R.array.weight_unit_values)[item]);
				editor.commit();
				showDialog(1);
			}
		});
		return builder.create();
	}

	private void saveWeightInfo(WeightInfo mWeightInfo) {
		// TODO Auto-generated method stub
		mWeightInfo.setUserIdName(Define.USER_ID_NAME);
		mWeightInfo.setUserName(Define.USER_NAME);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = format.format(new java.util.Date());
		mWeightInfo.setCompleteTime(time);
		long gTime = (mDateTime == null ? System.currentTimeMillis() : mDateTime.getTime().getTime()) / 1000;
		mWeightInfo.setGTime(gTime);
		float weight = Float.parseFloat(MainActivity.mMainActivity.searchWeight());
		mWeightInfo.setUserWeight(weight);
		Log.d("yuhao", "weight=------------------" + weight);
		Log.d("yuhao", "gTime=------------------" + gTime);
		Log.d("yuhao", "time=------------------" + time);
	}

	private void initUserInfo() {
		// TODO Auto-generated method stub
		ArrayList<UserInfo> infoListName = new ArrayList<UserInfo>();
		// infoListName = dbManager.searchData(Define.DB_NAMES);
		infoListName = Define.dbManager.searchData(Define.USER_ID_NAME);
		Log.d("yuhao", "initUserInfo-----infoListName=--------" + infoListName);
		String result = "";
		if (infoListName.size() != 0) {
			for (UserInfo info : infoListName) {
				result = result + info.userIdName + "|" + info.userPwd + "|" + info.userName + "|" + info.userSex + "|"
						+ info.userBrithday + "|" + info.userHeight + "|" + info.userWeight;
				result = result + "\n" + "------------------------------------------" + "\n";
				mUserName.setText(info.userName);
				mSex.setText(info.userSex);
				mBirth.setText(info.userBrithday);
				mHigh.setText((int) info.userHeight + "");
				mWeight.setText(info.userWeight + "");
				BMIValue = info.userWeight / (info.userHeight * 0.01 * info.userHeight * 0.01);
				Log.d("yuhao", "BMIValue=-------------" + BMIValue);
				mBMI.setText(df.format(BMIValue));
				mBMIAdvice.setText(getAdvice());
			}
		} else {
			Toast.makeText(UserActivity.this, "数据异常！", Toast.LENGTH_SHORT).show();
		}
		Log.i("yuhao--initInfo--UserInfo--", result);
	}

	private String getAdvice() {
		// TODO Auto-generated method stub
		String advice = "";
		if (BMIValue < 18.5) {
			advice = "偏瘦,营养不全面，人体所需的能量不充分,建议增加日常营养摄入";
		} else if (BMIValue >= 18.5 && BMIValue <= 23.9) {
			advice = "正常,继续保持健康的生活方式";
		} else if (BMIValue >= 24 && BMIValue <= 27.9) {
			advice = "偏胖,建议平时需要有一个合理的生活和饮食习惯，经常运动锻炼身体";
		} else if (BMIValue >= 28) {
			advice = "肥胖,建议平时需要有一个合理的生活和饮食习惯，经常运动锻炼身体";
		}
		return advice;
	}

	private void init() {
		// TODO Auto-generated method stub
		instance = this;
		mImgUserBack = (ImageView) findViewById(R.id.my_back);
		mUserInformation = (LinearLayout) findViewById(R.id.home_my_to_my_information);
		mMySettings = (TextView) findViewById(R.id.my_settingst);
		mUserName = (TextView) findViewById(R.id.user_my_name);
		mSex = (TextView) findViewById(R.id.user_my_sex);
		mBirth = (TextView) findViewById(R.id.user_my_birthday);
		mHigh = (TextView) findViewById(R.id.user_my_high);
		mWeight = (TextView) findViewById(R.id.user_my_weight);
		mBMI = (TextView) findViewById(R.id.user_my_bmi);
		mStartDate = (TextView) findViewById(R.id.start_date);
		mEndDate = (TextView) findViewById(R.id.end_date);
		mBMIAdvice = (TextView) findViewById(R.id.bmi_advice);
		acharWeight = (LinearLayout) findViewById(R.id.achar);
		mSearch = (ImageView) findViewById(R.id.history_search);
		mImgUserBack.setOnClickListener(this);
		mUserInformation.setOnClickListener(this);
		mMySettings.setOnClickListener(this);
		mStartDate.setOnClickListener(this);
		mEndDate.setOnClickListener(this);
		mSearch.setOnClickListener(this);
		dbManager = new DBManager(this);

		mDatabase = new Database(this);
		mDraw = new ChartDraw(this, mDatabase, new GregorianCalendar());
		mDateTime = new GregorianCalendar();
		mDateTime.set(GregorianCalendar.SECOND, 0);
		((ChartView) findViewById(R.id.chart_view)).setChartDraw(mDraw);
		initTime();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("yuhao", "onResume----------UserActivity-------");
		initUserInfo();
		mDraw.loadPreferences(this);
		invalidate();
		super.onResume();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.d("yuhao", "onRestart----------UserActivity-------");
		super.onRestart();
	}

	private void initTime() {
		// TODO Auto-generated method stub
		Date date = new Date();// 创建一个时间对象，获取到当前的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 设置时间显示格式
		select_end_time = sdf.format(date);// 将当前时间格式化为需要的类型
		mEndDate.setText(select_end_time);
		today = select_end_time;
		Date startDate = new Date(date.getTime() - (long) 30 * 24 * 3600 * 1000);
		select_start_time = sdf.format(startDate);
		mStartDate.setText(select_start_time);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_back:
			finish();
			break;
		case R.id.home_my_to_my_information:
			switchToMyInfo();
			break;
		case R.id.my_settingst:
			switchToMySettings();
			break;
		case R.id.start_date:
			try {
				selectTime(true, mStartDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.end_date:
			try {
				selectTime(false, mEndDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.history_search:

			break;

		default:
			break;
		}
	}

	private void selectTime(final boolean is_start, TextView timeTxt) throws ParseException {
		Date d1 = new Date();
		if (!TextUtils.isEmpty(timeTxt.getText())) {
			d1 = new SimpleDateFormat("yyyy-MM-dd").parse(timeTxt.getText().toString());
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

		MyCustomDatePickerDialog datePickerDialog = new MyCustomDatePickerDialog(UserActivity.this,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						if (is_start) {
							String str1 = (year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
									+ String.format("%02d", dayOfMonth));
							if (timeCompare(select_end_time, str1)) {
								select_start_time = str1;
								mStartDate.setText(select_start_time);
							} else {
								Toast.makeText(UserActivity.this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
							}
						} else {
							String str1 = (year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
									+ String.format("%02d", dayOfMonth));
							if (timeCompare(today, str1)) {
								if (timeCompare(str1, select_start_time)) {
									select_end_time = str1;
									mEndDate.setText(select_end_time);
								} else {
									Toast.makeText(UserActivity.this, "结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(UserActivity.this, "结束时间不能晚于今日", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}, int_year, int_month - 1, int_day);
		datePickerDialog.setDatePickerDividerColor(datePickerDialog.getDatePicker(), UserActivity.this);
		datePickerDialog.show();
	}

	private boolean timeCompare(String time1, String time2) {
		Date date = new Date();// 创建一个时间对象，获取到当前的时间
		Date date2 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(time1);
			date2 = sdf.parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// long longstr1 = Long.valueOf(time1.replaceAll("[-\\s:]", ""));
		// long longstr2 = Long.valueOf(time2.replaceAll("[-\\s:]", ""));
		// return longstr1 > longstr2 ? true : false;
		return date.before(date2) ? false : true;
	}

	private void switchToMySettings() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, UserSetupActivity.class);
		startActivity(intent);
	}

	private void switchToMyInfo() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, UserInfoActivity.class);
		startActivity(intent);
	}
}
