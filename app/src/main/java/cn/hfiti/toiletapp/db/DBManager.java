package cn.hfiti.toiletapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.hfiti.toiletapp.entity.UserInfo;
import cn.hfiti.toiletapp.entity.WeightInfo;
import cn.hfiti.toiletapp.util.Define;

public class DBManager {
	private UserInfoDBHelper mUserInfoDBHelper;
    private SQLiteDatabase db;
    public DBManager(Context context){
    	Log.d("yuhao", "DBManager--------------------");
    	mUserInfoDBHelper = new UserInfoDBHelper(context);
        db = mUserInfoDBHelper.getWritableDatabase();
    }

    /**
     * 向表userInfo中增加一个成员信息
     * 
     * @param userInfo
     */
    public void add(List<UserInfo> userInfo) {
        db.beginTransaction();// 开始事务
        try {
            for (UserInfo info : userInfo) {
                Log.d("yuhao", "--add--info--"+info.userIdName+"|"+info.userPwd+"|"+
                		info.userName+"|"+info.userSex+"|"+info.userBrithday+"|"+"|"+info.userHeight+"|"+info.userWeight);
                // 向表info中插入数据
                db.execSQL("INSERT INTO userinfo VALUES(null,?,?,?,?,?,?,?)",
                		new Object[] { info.userIdName, info.userPwd,info.userName,
                        info.userSex, info.userBrithday, info.userHeight, info.userWeight});
            }
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
    }
    
    public void addWeight(String userNameId,String time,float userWeight,long gTime){
    	db.beginTransaction();// 开始事务
    	try {
    		db.execSQL("INSERT INTO weightinfo (record_time, user_weight, g_time) VALUES (?, ?, ?)");
//    		db.execSQL("UPDATE weightinfo SET record_time = ?, user_weight = ? , g_time = ? WHERE user_id_name = ?", new Object[] { time, userWeight, gTime, userNameId }); 
		} finally {
			db.endTransaction();// 结束事
		}
    }
    /**
     * 向表weightInfo中增加一个成员信息
     * 
     * @param weightInfo
     */
    public void add1(List<WeightInfo> weightInfo){
    	db.beginTransaction();// 开始事务
    	try {
            for (WeightInfo info : weightInfo) {
                Log.d("yuhao", "--add--info--"+info.userIdName+"|"+
                		info.userName+"|"+info.time+"|"+info.userWeight+"|"+info.gTime);
                // 向表info中插入数据
                db.execSQL("INSERT INTO weightinfo VALUES(null,?,?,?,?,?)",
                		new Object[] { info.userIdName, info.userName,info.time,info.userWeight,info.gTime});
            }
//            db.execSQL("INSERT INTO weight (weight, created_at, user_id_name) VALUES (?, ?, ?)",
//            		new Object[] { Define.WEIGHT, Define.CREAT_TIME, Define.USER_ID_NAME });
            db.setTransactionSuccessful();// 事务成功
        } finally {
            db.endTransaction();// 结束事务
        }
    }

    /**
     * @param _id
     * @param userIdName
     * @param userPwd
     * @param userName
     * @param userSex
     * @param userBrithday
     * @param userHeight
     * @param userWeight
     */
    public void add(int _id, String userIdName, String userPwd, 
    		String userName, String userSex,String userBrithday, String userHeight, String userWeight) {
        Log.i("yuhao", "------add userInfo data----------");
        ContentValues cv = new ContentValues();
        // cv.put("_id", _id);
        cv.put("userIdName", userIdName);
        cv.put("userPwd", userPwd);
        cv.put("userName", userName);
        cv.put("userSex", userSex);
        cv.put("userBrithday", userBrithday);
        cv.put("userHeight", userHeight);
        cv.put("userWeight", userWeight);
        db.insert(Define.USER_TABLE_NAME, null, cv);
        Log.d("yuhao","-----add-------" +userIdName +
        		"/" + userPwd + "/" + userName + "/" + 
        		userSex+ "/" + userBrithday+"/" + userHeight+"/" + userWeight);
    }
    
    public void add1(String userIdName,String userName,String recordTime,float userWeight,long gTime){
    	Log.i("yuhao", "------add weightInfo data----------");
    	ContentValues cv = new ContentValues();
    	cv.put("user_id_name", userIdName);
        cv.put("user_name", userName);
        cv.put("record_time", recordTime);
        cv.put("user_weight", userWeight);
        cv.put("g_time", gTime);
        db.insert(Define.WEIGHT_TABLE_NAME, null, cv);
    }

    /**
     * 通过name来删除数据
     * 
     * @param name
     */
    public void delData(String name) {
        // ExecSQL("DELETE FROM userInfo WHERE name ="+"'"+name+"'");
        String[] args = { name };
        db.delete(Define.USER_TABLE_NAME, "user_id_name=?", args);
        Log.d("yuhao", "delete data by " + name);

    }

    /**
     * 清空数据
     */
    public void clearData() {
        ExecSQL("DELETE FROM userinfo");
        Log.i("yuhao", "clear data");
    }

    /**
     * 通过名字查询信息,返回所有的数据
     * 
     * @param name
     */
    public ArrayList<UserInfo> searchData(String name) {
        Log.d("yuhao", "searchData--------UserInfo--------");
        String sql = "SELECT * FROM userinfo WHERE user_id_name =" + "'" + name + "'";
        return ExecSQLForUserInfo(sql);
    }

    public ArrayList<WeightInfo> searchData1(String name) {
        Log.d("yuhao", "searchData--------WeightInfo--------");
    	String sql = "SELECT * FROM weightinfo WHERE user_id_name =" + "'" + name + "'";
        return ExecSQLForWeightInfo(sql);
    }

    public ArrayList<UserInfo> searchAllData() {
        String sql = "SELECT * FROM userinfo";
        return ExecSQLForUserInfo(sql);
    }

    /**
     * 通过名字来修改值
     * 
     * @param raw
     * @param rawValue
     * @param whereName
     */
    public void updateData(String raw, String rawValue, String whereName) {
        String sql = "UPDATE userinfo SET " + raw + " =" + " " + "'" + rawValue + "'" + " WHERE user_id_name =" + "'" + whereName
                     + "'";
        ExecSQL(sql);
        Log.i("yuhao", sql);
    }
    
    public void updateData(String raw, float rawValue, String whereName) {
        String sql = "UPDATE userinfo SET " + raw + " =" + " " + "'" + rawValue + "'" + " WHERE user_id_name =" + "'" + whereName
                     + "'";
        ExecSQL(sql);
        Log.i("yuhao", sql);
    }

    /**
     * 执行SQL命令返回list
     * 
     * @param sql
     * @return
     */
    private ArrayList<UserInfo> ExecSQLForUserInfo(String sql) {
        ArrayList<UserInfo> list = new ArrayList<UserInfo>();
        Log.d("yuhao", "ExecSQLForUserInfo----------------");
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
        	UserInfo info = new UserInfo();
//            info._id = c.getInt(c.getColumnIndex("_id"));
            info.userIdName = c.getString(c.getColumnIndex("user_id_name"));
            info.userPwd = c.getString(c.getColumnIndex("user_password"));
            info.userName = c.getString(c.getColumnIndex("user_name"));
            info.userSex = c.getString(c.getColumnIndex("user_sex"));
            info.userBrithday = c.getString(c.getColumnIndex("user_birthday"));
            info.userHeight = c.getFloat(c.getColumnIndex("user_high"));
            info.userWeight = c.getFloat(c.getColumnIndex("user_weight"));
            list.add(info);
        }
        c.close();
        return list;
    }
    
    private ArrayList<WeightInfo> ExecSQLForWeightInfo(String sql) {
        ArrayList<WeightInfo> list = new ArrayList<WeightInfo>();
        Log.d("yuhao", "ExecSQLForWeightInfo----------------");
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
        	WeightInfo info = new WeightInfo();
//            info._id = c.getInt(c.getColumnIndex("_id"));
            info.userIdName = c.getString(c.getColumnIndex("user_id_name"));
            info.userName = c.getString(c.getColumnIndex("user_name"));
            info.userWeight = c.getFloat(c.getColumnIndex("user_weight"));
            info.time = c.getString(c.getColumnIndex("record_time"));
            info.gTime = c.getLong(c.getColumnIndex("g_time"));
            list.add(info);
        }
        c.close();
        return list;
    }

    /**
     * 执行一个SQL语句
     * 
     * @param sql
     */
    private void ExecSQL(String sql) {
        try {
            db.execSQL(sql);
            Log.i("execSql: ", sql);
        } catch (Exception e) {
            Log.e("ExecSQL Exception", e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 执行SQL，返回一个游标
     * 
     * @param sql
     * @return
     */
    private Cursor ExecSQLForCursor(String sql) {
    	Log.d("yuhao", "ExecSQLForCursor----------------0");
        Cursor c = db.rawQuery(sql, null);
        Log.d("yuhao", "ExecSQLForCursor----------------1");
        return c;
    }

    public void closeDB() {
        db.close();
    }
    
    public Cursor query(String query) {
    	return db.rawQuery(query, null);
    }
}
