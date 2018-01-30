package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.SharedTool;

public class UserSetupActivity extends Activity implements OnClickListener{
	
	private ImageView mImgUserSettingsBack;
	private LinearLayout mLayoutCustom;
	private LinearLayout mLayoutLogOut;;
    private SharedTool sharedTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_settings);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mImgUserSettingsBack = (ImageView) findViewById(R.id.user_settings_back);
		mLayoutCustom = (LinearLayout) findViewById(R.id.user_settings_custom);
		mLayoutLogOut = (LinearLayout) findViewById(R.id.log_out);
		mImgUserSettingsBack.setOnClickListener(this);
		mLayoutCustom.setOnClickListener(this);
		mLayoutLogOut.setOnClickListener(this);
		if (Define.LOGIN_SUCCESS) {
			mLayoutLogOut.setVisibility(View.VISIBLE);
		}
        sharedTool = new SharedTool(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_settings_back:
			finish();
			break;
		case R.id.user_settings_custom:
			switchToCustom();
			break;
		case R.id.log_out:
			new AlertDialog.Builder(this).setTitle("确认退出？")
            .setIcon(android.R.drawable.ic_dialog_info) 
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {            	
                @Override 
                public void onClick(DialogInterface dialog, int which) {
                	LoginOut();
                }
            })
            .setNegativeButton("返回", new DialogInterface.OnClickListener() {         
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();		
			break;

		default:
			break;
		}
	}

	private void LoginOut() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(UserSetupActivity.this,LoginActivity.class);
		startActivity(intent);
		Define.LOGIN_SUCCESS = false;
		Define.USER_ID_NAME = null;
        sharedTool.setSharedString("userName", null);
        sharedTool.setSharedString("userPwd", null);
        sharedTool.setSharedString("userIdName", null);
        sharedTool.setSharedBoolean("auto_login", false);
        mLayoutLogOut.setVisibility(View.INVISIBLE);
		UserActivity.instance.finish();
		finish();
	}

	private void switchToCustom() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,SettingsActivity.class);
		startActivity(intent);
	}
}
