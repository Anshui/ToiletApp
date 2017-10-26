package cn.hfiti.toiletapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.hfiti.toiletapp.util.Define;

public class UserInfoDBHelper extends SQLiteOpenHelper {

	public UserInfoDBHelper(Context context) {
		super(context,Define.DB_NAMES,null,Define.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("yuhao", "UserInfoDBHelper-----------onCreate-------------");
		db.execSQL(Define.USER_TABLE_CREATE);
		db.execSQL(Define.WEIGHT_TABLE_CREATE);
		
//		db.execSQL("CREATE TABLE weight (_id INTEGER PRIMARY KEY AUTOINCREMENT, weight, created_at)");
//	    db.execSQL("CREATE INDEX weight_created_at ON weight (created_at)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(Define.USER_TABLE_DROP);
		db.execSQL(Define.WEIGHT_TABLE_DROP);
		onCreate(db);
	}
}
