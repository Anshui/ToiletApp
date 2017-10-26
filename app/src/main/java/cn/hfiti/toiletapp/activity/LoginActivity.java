package cn.hfiti.toiletapp.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.util.Define;

public class LoginActivity extends Activity implements OnClickListener{
	
	private ImageView mImgBack;
	private TextView mTextRegist;
	private TextView mTextLogin;
	private EditText mInputName;
	private EditText mInputPwd;
	private String user_id_name = null;
	private String password = null;
	public static LoginActivity instance;
//	private static DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "LoginActivity-------onCreate--------");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("yuhao", "LoginActivity-------onDestroy--------");
	}

	private void init() {
		// TODO Auto-generated method stub
		instance = this;
		mImgBack = (ImageView) findViewById(R.id.login_back);
		mTextRegist = (TextView) findViewById(R.id.login_regist);
		mTextLogin = (TextView) findViewById(R.id.login_in);
		mInputName = (EditText) findViewById(R.id.login_name);
		mInputPwd = (EditText) findViewById(R.id.login_password);
		mImgBack.setOnClickListener(this);
		mTextRegist.setOnClickListener(this);
		mTextLogin.setOnClickListener(this);
//		dbManager = new DBManager(this);
		Log.d("yuhao", "LoginActivity-----dbManager---");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.login_regist:
			switchToRegist();
			break;
		case R.id.login_in:			
			if (android.text.TextUtils.isEmpty(mInputName.getText().toString())) {
				Toast.makeText(LoginActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
			} else if (android.text.TextUtils.isEmpty(mInputPwd.getText().toString())) {
				Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
			} else {
				user_id_name = mInputName.getText().toString();
				password = mInputPwd.getText().toString();
				Log.d("yuhao", "user_id_name=----------"+user_id_name);
				Log.d("yuhao", "password=----------"+password);
				ArrayList<UserInfo> infoListName = new ArrayList<UserInfo>();
				infoListName = Define.dbManager.searchData(user_id_name);
                Log.d("yuhao", "infoListName=--------"+infoListName);
                String result = "";
                if (infoListName.size()!=0) {
                	for (UserInfo info : infoListName) {
                        result = result + info.userIdName + "|" + info.userPwd
                                 + "|" + info.userName + "|" + info.userSex + "|" + info.userBrithday + "|" + 
                        		info.userHeight+ "|" + info.userWeight;
                        result = result + "\n" + "------------------------------------------" + "\n";
                        if (password.equals(info.userPwd)) {
                        	switchToMy();
                        	Define.USER_ID_NAME = info.userIdName;
                        	Define.USER_NAME = info.userName;
                        	Define.LOGIN_SUCCESS = true;
                        	Log.d("yuhao", "Define.DB_ID_NAMES=--------"+Define.USER_ID_NAME);
                        	Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
        				}
        				else {
        					Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
        				}
                    }
				}
                else {
                	Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
				}               
                Log.i("yuhao", result);
			}
			break;

		default:
			break;
		}
	}

	private void switchToMy() {
		// TODO Auto-generated method stub
		finish();
		Intent intent = new Intent(LoginActivity.this,UserActivity.class);
		startActivity(intent);
	}

	private void switchToRegist() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(intent);		
	}

}
