package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.util.Define;
import cn.hfiti.toiletapp.util.SharedTool;
import cn.hfiti.toiletapp.view.CustomActionBar;

public class BodyCompositionActivity extends Activity implements OnClickListener{

    private TextView username, height, weight, age, sex, bmi, bmr, body_fat;
    private CustomActionBar body_fat_actionbar;
    private SharedTool sharedTool;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.body_fat);
        initView();
		intiData();
	}

	private void intiData() {
        if (sharedTool.getSharedBoolean("auto_login", false)) {
            String userIdName = sharedTool.getSharedString("userIdName", null);
            ArrayList<UserInfo> list = Define.dbManager.searchData(userIdName);
            UserInfo info = list.get(0);

            height.setText(info.getUserHeight() + "");
            weight.setText(info.getUserWeight() + "");
            sex.setText(info.getUserSex());
            username.setText(info.getUserName());
            age.setText(getAge(info.getUserBrithday()) + "");

            DecimalFormat format = new DecimalFormat("##0.0");
            bmi.setText(format.format(info.getUserWeight() / Math.sqrt(info.getUserHeight() / 100)));
            bmr.setText(format.format(getBMR(info)));
        } else {
            Toast.makeText(this, "尚未登录！", Toast.LENGTH_LONG).show();
        }
    }

    private double getBMR(UserInfo info) {
        double bmr_ = 0.0;
        if (info.getUserSex().equals("男")) {
            bmr_ = 67 + 13.73 * info.getUserWeight() + 5 * info.getUserHeight() - 6.9 * getAge(info.getUserBrithday());
        } else {
            bmr_ = 661 + 9.6 * info.getUserWeight() + 1.72 * info.getUserHeight() - 4.7 * getAge(info.getUserBrithday());
        }
        return bmr_;
    }

    private int getAge(String date) {
        int age_ = 0;
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        try {
            Date birth = format.parse(date);
            Date now = new Date();

            age_ = now.getYear() - birth.getYear() - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age_;
    }

    private void initView() {
        username = findViewById(R.id.user_name);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        age = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        bmi = findViewById(R.id.body_fat_bmi);
        bmr = findViewById(R.id.body_fat_bmr);
        body_fat = findViewById(R.id.body_fat);

        body_fat_actionbar = findViewById(R.id.body_fat_actionbar);
        body_fat_actionbar.setTitleClickListener(this);

        sharedTool = new SharedTool(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_bar_left_button:
			finish();
			
			break;

		default:
			break;
		}
	}
}
