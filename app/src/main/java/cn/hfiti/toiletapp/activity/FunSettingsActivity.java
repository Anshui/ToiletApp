package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.util.Define;

public class FunSettingsActivity extends Activity implements OnClickListener{
	
	private ImageView mFunSettingsBack;
	private LinearLayout mConfirm;
	private Switch mSwitchDry;
	private Switch mSwitchFlush;
	private Switch mSwitchCover;
	private Switch mSwitchSmell;
	private Switch mSwitchPower;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fun_settings);
		init();
		initSwitchState();
	}

	private void initSwitchState() {
		// TODO Auto-generated method stub
		if (Define.DRY_STATE) {
			mSwitchDry.setChecked(true);
		}
		else {
			mSwitchDry.setChecked(false);
		}
		if (Define.FLUSH_STATE) {
			mSwitchFlush.setChecked(true);
		}
		else {
			mSwitchFlush.setChecked(false);
		}
		if (Define.COVER_STATE) {
			mSwitchCover.setChecked(true);
		}
		else {
			mSwitchCover.setChecked(false);
		}
		if (Define.SMELL_STATE) {
			mSwitchSmell.setChecked(true);
		}
		else {
			mSwitchSmell.setChecked(false);
		}
		if (Define.POWER_STATE) {
			mSwitchPower.setChecked(true);
		}
		else {
			mSwitchPower.setChecked(false);
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		mFunSettingsBack = (ImageView) findViewById(R.id.settings_new_back);
		mConfirm = (LinearLayout) findViewById(R.id.settings_confirm_new);
		mSwitchDry = (Switch) findViewById(R.id.switch_dry);
		mSwitchFlush = (Switch) findViewById(R.id.switch_flush);
		mSwitchCover = (Switch) findViewById(R.id.switch_cover);
		mSwitchSmell = (Switch) findViewById(R.id.switch_smell_clean);
		mSwitchPower = (Switch) findViewById(R.id.switch_power_save);
		mFunSettingsBack.setOnClickListener(this);
		mConfirm.setOnClickListener(this);
		mSwitchDry.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					MainActivity.getMainActivity().autoDryOpen();
					Define.DRY_STATE = true;
				}
				else {
					MainActivity.getMainActivity().autoDryClose();
					Define.DRY_STATE = false;
				}
			}
		});
		mSwitchFlush.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					MainActivity.getMainActivity().autoFlushOpen();
					Define.FLUSH_STATE = true;
				}
				else {
					MainActivity.getMainActivity().autoFlushClose();
					Define.FLUSH_STATE = false;
				}
			}
		});
		mSwitchCover.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					MainActivity.getMainActivity().autoCoverOpen();
					Define.COVER_STATE = true;
				}
				else {
					MainActivity.getMainActivity().autoCoverClose();
					Define.COVER_STATE = false;
				}
			}
		});
		mSwitchSmell.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					MainActivity.getMainActivity().autoSmellOpen();
					Define.SMELL_STATE = true;
				}
				else {
					MainActivity.getMainActivity().autoSmellClose();
					Define.SMELL_STATE = false;
				}
			}
		});
		mSwitchPower.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					MainActivity.getMainActivity().autoPowerOpen();
					Define.POWER_STATE = true;
				}
				else {
					MainActivity.getMainActivity().autoPowerClose();
					Define.POWER_STATE = false;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settings_new_back:
			saveSettings();
			finish();
			break;
		case R.id.settings_confirm_new:
			saveSettings();
			finish();
			break;

		default:
			break;
		}
	}

	private void saveSettings() {
		// TODO Auto-generated method stub
		
	}	
}
