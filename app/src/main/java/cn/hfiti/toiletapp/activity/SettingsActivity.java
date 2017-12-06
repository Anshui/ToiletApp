package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.MyCustomPicker;
import cn.hfiti.toiletapp.util.SharedTool;

public class SettingsActivity extends Activity implements OnClickListener{
	
	private ImageView mImgSettingsBack;
	private LinearLayout mTempSeat;
	private LinearLayout mTempWater;
	private LinearLayout mPressWater;
	private LinearLayout mTempDry;
	private LinearLayout mConfirm;
	private AlertDialog alertDialog;
	private TextView mTempSeatValue;
	private TextView mTempWaterValue;
	private TextView mPressWaterValue;
	private TextView mTempDryValue;
    private SharedTool sharedTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub		
		mImgSettingsBack = (ImageView) findViewById(R.id.settings_back);
		mTempSeat = (LinearLayout) findViewById(R.id.settings_tempseat);
		mTempWater = (LinearLayout) findViewById(R.id.settings_tempwater);
		mPressWater = (LinearLayout) findViewById(R.id.settings_presswater);
		mTempDry = (LinearLayout) findViewById(R.id.settings_tempdry);
		mConfirm = (LinearLayout) findViewById(R.id.settings_confirm);
		mTempSeatValue = (TextView) findViewById(R.id.value_tempseat);
		mTempWaterValue = (TextView) findViewById(R.id.value_tempwater);
		mPressWaterValue = (TextView) findViewById(R.id.value_presswater);
		mTempDryValue = (TextView) findViewById(R.id.value_tempdry);
		mTempSeatValue.setText(Integer.toString(Define.SET_SEAT_TEMP));
		mTempWaterValue.setText(Integer.toString(Define.SET_WATER_TEMP));
		mPressWaterValue.setText(Integer.toString(Define.SET_WATER_STH));
		mTempDryValue.setText(Integer.toString(Define.SET_DRY_TEMP));
		
		mTempSeat.setOnClickListener(this);
		mTempWater.setOnClickListener(this);
		mPressWater.setOnClickListener(this);
		mTempDry.setOnClickListener(this);
		mImgSettingsBack.setOnClickListener(this);
		mConfirm.setOnClickListener(this);

        sharedTool = new SharedTool(this);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {  
	        Log.d("yuhao", "onKeyDown-------------------");
	        saveSettings();
	    } 
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settings_tempseat:
			setTempSeat();
			break;
		case R.id.settings_tempwater:
			setTempWater();
			break;
		case R.id.settings_presswater:
			setPressWater();
			break;
		case R.id.settings_tempdry:
			setTempDry();
			break;
		case R.id.settings_back:
			saveSettings();
			finish();			
			break;
		case R.id.settings_confirm:
			saveSettings();
			finish();
			break;

		default:
			break;
		}
	}

	private void saveSettings() {
		// TODO Auto-generated method stub
		Define.SET_SEAT_TEMP = Integer.parseInt(mTempSeatValue.getText().toString());
		Define.SET_WATER_TEMP = Integer.parseInt(mTempWaterValue.getText().toString());
		Define.SET_WATER_STH = Integer.parseInt(mPressWaterValue.getText().toString());
		Define.SET_DRY_TEMP = Integer.parseInt(mTempDryValue.getText().toString());
	}

	private void setTempDry() {
		// TODO Auto-generated method stub
		final MyCustomPicker numberPicker = new MyCustomPicker(SettingsActivity.this);
		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setMaxValue(3);
		numberPicker.setMinValue(1);
		if (!TextUtils.isEmpty(mTempDryValue.getText())) {
			numberPicker.setValue(Integer.parseInt(mTempDryValue.getText().toString()));

		} else {
			numberPicker.setValue(Define.SET_DRY_TEMP);
		}

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTempDryValue.setText(numberPicker.getValue() + "");
						switch (numberPicker.getValue()) {
						case 1:
							MainActivity.getMainActivity().setDryTemp_1();
							break;
						case 2:
							MainActivity.getMainActivity().setDryTemp_2();
							break;
						case 3:
							MainActivity.getMainActivity().setDryTemp_3();
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("取消", null).create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private void setPressWater() {
		// TODO Auto-generated method stub
		final MyCustomPicker numberPicker = new MyCustomPicker(SettingsActivity.this);
		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setMaxValue(3);
		numberPicker.setMinValue(1);
		if (!TextUtils.isEmpty(mPressWaterValue.getText())) {
			numberPicker.setValue(Integer.parseInt(mPressWaterValue.getText().toString()));

		} else {
			numberPicker.setValue(Define.SET_WATER_STH);
		}

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mPressWaterValue.setText(numberPicker.getValue() + "");
						switch (numberPicker.getValue()) {
						case 1:
							MainActivity.getMainActivity().setWaterPress_1();
							break;
						case 2:
							MainActivity.getMainActivity().setWaterPress_2();
							break;
						case 3:
							MainActivity.getMainActivity().setWaterPress_3();
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("取消", null).create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private void setTempSeat() {
		// TODO Auto-generated method stub
		final MyCustomPicker numberPicker = new MyCustomPicker(SettingsActivity.this);
		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setMaxValue(3);
		numberPicker.setMinValue(1);
		if (!TextUtils.isEmpty(mTempSeatValue.getText())) {
			numberPicker.setValue(Integer.parseInt(mTempSeatValue.getText().toString()));

		} else {
			numberPicker.setValue(Define.SET_SEAT_TEMP);
		}

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTempSeatValue.setText(numberPicker.getValue() + "");
						switch (numberPicker.getValue()) {
						case 1:
							MainActivity.getMainActivity().setSeatTemp_1();
							break;
						case 2:
							MainActivity.getMainActivity().setSeatTemp_2();
							break;
						case 3:
							MainActivity.getMainActivity().setSeatTemp_3();
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("取消", null).create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private void setTempWater() {
		// TODO Auto-generated method stub
		final MyCustomPicker numberPicker = new MyCustomPicker(SettingsActivity.this);
		numberPicker.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		numberPicker.setNumberPickerDividerColor(numberPicker);
		numberPicker.setMaxValue(3);
		numberPicker.setMinValue(1);
		if (!TextUtils.isEmpty(mTempWaterValue.getText())) {
			numberPicker.setValue(Integer.parseInt(mTempWaterValue.getText().toString()));

		} else {
			numberPicker.setValue(Define.SET_WATER_TEMP);
		}

		alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setView(numberPicker)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mTempWaterValue.setText(numberPicker.getValue() + "");
						switch (numberPicker.getValue()) {
						case 1:
							MainActivity.getMainActivity().setWaterTemp_1();
							break;
						case 2:
							MainActivity.getMainActivity().setWaterTemp_2();
							break;
						case 3:
							MainActivity.getMainActivity().setWaterTemp_3();
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("取消", null).create();

		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}
}
