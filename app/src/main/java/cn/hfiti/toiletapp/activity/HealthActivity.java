package cn.hfiti.toiletapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import cn.hfiti.toiletapp.R;
import cn.hfiti.toiletapp.view.CustomActionBar;

public class HealthActivity extends Activity implements OnClickListener{
	
	private Button weightButton,urineButton,bodyComposition,heartRate;
	private CustomActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_center);
		initView();
		initData();
		
	}

	private void initData() {
		
		weightButton.setOnClickListener(this);
		urineButton.setOnClickListener(this);
		bodyComposition.setOnClickListener(this);
		heartRate.setOnClickListener(this);
		mActionBar.setTitleClickListener(this);
		
	}

	private void initView() {
		
		weightButton = (Button) findViewById(R.id.weight_button);
		urineButton = (Button) findViewById(R.id.urine_button);
		bodyComposition = (Button) findViewById(R.id.body_composition_button);
		heartRate = (Button) findViewById(R.id.heart_rate_button);
		mActionBar = (CustomActionBar) findViewById(R.id.health_center_action_bar);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.weight_button:
			changeToUser();
			
			break;
		case R.id.urine_button:
			changeToUrine();
			
			break;
		case R.id.body_composition_button:
			changeToBody();
			
			break;
		case R.id.heart_rate_button:
			changeToHeartRate();
			
			break;
		case R.id.action_bar_left_button:
			finish();
			break;
		default:
			break;
		}
		
	}

	private void changeToHeartRate() {
		Intent intent = new Intent(this,HeartRateActivity.class);
		startActivity(intent);
	}

	private void changeToBody() {
		Intent intent = new Intent(this,BodyCompositionActivity.class);
		startActivity(intent);
	}

	private void changeToUrine() {
		Intent intent = new Intent(this,UrineTestActivity.class);
		startActivity(intent);
	}

	private void changeToUser() {
		Intent intent = new Intent(this,UserActivity.class);
		startActivity(intent);
	}

}
