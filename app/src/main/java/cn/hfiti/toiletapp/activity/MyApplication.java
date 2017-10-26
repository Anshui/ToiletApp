package cn.hfiti.toiletapp.activity;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	
	private static MyApplication singleton;
	     
	@Override
    public final void onCreate() {
		Log.d("yuhao", "MyApplication-------onCreate----------");
		super.onCreate();
		singleton = this;
    }
    public static MyApplication getInstance(){
        return singleton;
    }
}
