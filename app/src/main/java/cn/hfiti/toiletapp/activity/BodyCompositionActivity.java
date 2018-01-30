package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.hfiti.toiletapp.R;
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
        }
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
        body_fat_actionbar.setOnClickListener(this);

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
