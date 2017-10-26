package cn.hfiti.toiletapp.util;

import cn.hfiti.toiletapp.db.DBManager;

public class Define {
	
	public static String DB_NAMES="toiletinfo.db";
	public static final int  DB_VERSION=1;
	public static final String USER_TABLE_NAME="userinfo";
	public static final String WEIGHT_TABLE_NAME="weightinfo";
	
	public static final String USER_TABLE_DROP="DROP TABLE IF EXISTS " + USER_TABLE_NAME;
	public static final String WEIGHT_TABLE_DROP="DROP TABLE IF EXISTS " + WEIGHT_TABLE_NAME;
	public static final String USER_TABLE_CREATE="CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
			" (_id integer primary key autoincrement,"
			+ "user_id_name,"
			+ "user_password,"
			+ "user_name,"
			+ "user_sex,"
			+ "user_birthday,"
			+ "user_high,"
			+ "user_weight)";
	public static final String WEIGHT_TABLE_CREATE="CREATE TABLE IF NOT EXISTS " + WEIGHT_TABLE_NAME +
			" (_id integer primary key autoincrement,"
			+"user_id_name,"
			+ "user_name,"
			+ "record_time,"
			+ "user_weight,"
			+ "g_time)";
	
	public static String USER_ID_NAME;
	public static String USER_NAME;
	public static DBManager dbManager;
	public static boolean LOGIN_SUCCESS = false;
	
	//注册信息保存
	public static int WEIGHT = 0;
	public static long CREAT_TIME = 0;
	
	//参数保存
	public static int SET_SEAT_TEMP = 2;
	public static int SET_WATER_TEMP = 2;
	public static int SET_WATER_STH = 2;
	public static int SET_DRY_TEMP = 2;
	
	//功能开关状态保存
	public static boolean DRY_STATE = false;
	public static boolean FLUSH_STATE = false;
	public static boolean COVER_STATE = false;
	public static boolean SMELL_STATE = false;
	public static boolean POWER_STATE = false;
	
	//马桶控制指令集
	public static String TEST="5500aa";
	public static String HIP_CLEAN="550101aa";
	public static String LADY_CLEAN="550102aa";
	public static String NOZZLE_CLEAN="550141aa";
	public static String CHILDREN_CLEAN="550103aa";
	public static String HIP_BATH="550104aa";
	public static String HIP_MASSAGE="550105aa";
	public static String DRY="550106aa";
	public static String N_FRONT="550107aa";
	public static String N_BACK="550108aa";
	public static String FLUSH="550109aa";
	public static String COVER="550110aa";
	public static String LIGHT_ON="550111aa";
	public static String LIGHT_OFF="550112aa";
	
	public static String SET_SEAT_TEMP_1="550113aa";
	public static String SET_SEAT_TEMP_2="550114aa";
	public static String SET_SEAT_TEMP_3="550115aa";
	public static String SET_SEAT_TEMP_4="550116aa";
	public static String SET_WATER_TEMP_1="550117aa";
	public static String SET_WATER_TEMP_2="550118aa";
	public static String SET_WATER_TEMP_3="550119aa";
	public static String SET_WATER_TEMP_4="550120aa";
	public static String SET_DRY_TEMP_1="550121aa";
	public static String SET_DRY_TEMP_2="550122aa";
	public static String SET_DRY_TEMP_3="550123aa";
	public static String SET_DRY_TEMP_4="550124aa";
	public static String SET_WATER_STH_1="550125aa";
	public static String SET_WATER_STH_2="550126aa";
	public static String SET_WATER_STH_3="550127aa";
	public static String SET_WATER_STH_4="550128aa";
	
	public static String AUTO_DRY_OPEN="550129aa";
	public static String AUTO_DRY_CLOSE="550130aa";
	public static String AUTO_FLUSH_OPEN="550131aa";
	public static String AUTO_FLUSH_CLOSE="550132aa";
	public static String AUTO_COVER_OPEN="550133aa";
	public static String AUTO_COVER_CLOSE="550134aa";
	public static String AUTO_POWER_OPEN="550135aa";
	public static String AUTO_POWER_CLOSE="550136aa";
	public static String AUTO_SMELL_OPEN="550137aa";
	public static String AUTO_SMELL_CLOSE="550138aa";	
	public static String POWER_CONTROL="550139aa";
	public static String END_INQ="550140aa";
	
	public static String SEARCH_SEAT_TEMP="550203aa";
	public static String SEARCH_WEIGHT="550201aa";
	public static String SEARCH_HEART_RATE="550204aa";
	
}
