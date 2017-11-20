package cn.hfiti.toiletapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import cn.hfiti.toiletapp.util.SystemBarTintManager;

/**
 * @author Arthur
 * 
 *         项目自定义基础Activity类，完成所有Activity的公共设定。
 */
public class BaseActivity extends Activity {
	/*
	 * 使App界面文字大小不会随系统变化而变化。
	 * 
	 * @see android.view.ContextThemeWrapper#getResources(
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initWindow();
	}
	
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}
	
	
	private SystemBarTintManager tintManager;
	   @TargetApi(19)
	   private void initWindow(){
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
	            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	            tintManager = new SystemBarTintManager(this);
	            //tintManager.setStatusBarTintColor(getResources().getColor(R.color.blue));
	            tintManager.setStatusBarTintEnabled(true);
	            tintManager.setNavigationBarTintEnabled(false);
	        }
	   }
}

