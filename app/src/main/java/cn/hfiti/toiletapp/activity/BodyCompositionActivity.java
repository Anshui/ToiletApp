package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.view.CustomActionBar;

public class BodyCompositionActivity extends Activity implements OnClickListener{
	
	private Button startTest;
	private TextView lifeAdvice;
	private CustomActionBar urineTest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.body_fat);
        initView();
		intiData();
	}

	private void intiData() {
	}

	private void initView() {
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
