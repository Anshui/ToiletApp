package cn.hfiti.toiletapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.text.TextUtils;

public class DateUtil {
	public static final String PNG = "png";
	public static final String JPG = "jpg";

	/**
	 * 获取当前时间�?
	 * 
	 * @return 时间�?
	 */
	public static long getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取当前时间字符�?
	 * 
	 * @param "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getCurrentTimeWithFormat(String FormatStr) {
		if (FormatStr == "") {
			FormatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(FormatStr);
		return sdf.format(new Date());// new Date()为获取当前系统时�?;
	}

	/**
	 * 获取当前时间字符�?2
	 * 
	 * @param "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getCurrentTimeWithFormatTwo(String FormatStr) {
		if (FormatStr == "") {
			FormatStr = "yyyy-MM-dd HH:mm:ss";
		}
		long time = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat(FormatStr);
		return format.format(new Date(time));
	}

	/**
	 * 获取有时区的字符串格式字�?
	 * 
	 * @param like
	 *            1384171247000+0800
	 * @param "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getTimeFromLikeShiQuTimeStr(String ShiQuTimeStr,
			String FormatStr) {
		if (FormatStr == "") {
			FormatStr = "yyyy-MM-dd HH:mm:ss";
		}
		String time = ShiQuTimeStr.substring(0, ShiQuTimeStr.length() - 5);
		// System.out.println(time);
		// final String timeZone = str.substring(str.length()-5, str.length());
		// System.out.println(timeZone);
		Date date = new Date(Long.parseLong(time));
		SimpleDateFormat format = new SimpleDateFormat(FormatStr);
		// System.out.println(format.format(date));
		return format.format(date);
	}

	/*
	 * 将时间戳转为字符�? ，格式：yyyy-MM-dd HH:mm
	 */
	public static String getStrTime_ymd_hm(String cc_time) {
		String re_StrTime = "";
		if (TextUtils.isEmpty(cc_time) || "null".equals(cc_time)) {
			return re_StrTime;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 格式�?24小时�?<br> 将时间戳转为字符�? ，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getStrTime_ymd_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/**
	 * 格式�?12小时�?<br>
	 * 格式：yyyy-MM-dd hh-mm-ss
	 * 
	 * @param time
	 *            时间
	 * @return
	 */
	public static String format12Time(long time) {
		return format(time, "yyyy-MM-dd hh:mm:ss");
	}

	/**
	 * 格式化时�?,自定义标�?
	 * 
	 * @param time
	 *            时间
	 * @param pattern
	 *            格式化时间用的标�?
	 * @return
	 */
	public static String format(long time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(time));
	}

	/*
	 * 将时间戳转为字符�? ，格式：yyyy.MM.dd
	 */
	public static String getStrTime_ymd(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符�? ，格式：yyyy
	 */
	public static String getStrTime_y(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符�? ，格式：MM-dd
	 */
	public static String getStrTime_md(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符�? ，格式：HH:mm
	 */
	public static String getStrTime_hm(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符�? ，格式：HH:mm:ss
	 */
	public static String getStrTime_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符�? ，格式：MM-dd HH:mm:ss
	 */
	public static String getNewsDetailsDate(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将字符串转为时间�?
	 */
	public static String getTime() {
		String re_time = null;
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d;
		d = new Date(currentTime);
		long l = d.getTime();
		String str = String.valueOf(l);
		re_time = str.substring(0, 10);
		return re_time;
	}

	/*
	 * 将时间戳转为字符�? ，格式：yyyy.MM.dd 星期�?
	 */
	public static String getSection(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  EEEE");
		// 对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十�?月�?�；MM代表月份，如�?11”；
		// yyyy代表年份，如�?2010”；dd代表天，如�??25�?
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	// public static String getTodayDate(){
	// SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	// String nowTime=format.format(new Date());
	// return
	// }
	/**
	 * 利用时间戳取得文件名
	 * 
	 * @param prefixStr
	 *            IMG
	 * @param suffixStr
	 *            png
	 * @return
	 */
	public static String getDateTimeStr4FileName(String prefixStr,
			String suffixStr) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'" + prefixStr
				+ "'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + "." + suffixStr;
	}

	/**
	 * 获取当前�?
	 * 
	 * @return �?
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentSecond() {
		Calendar calendar = Calendar.getInstance();
		return calendar.SECOND;
	}

	/**
	 * 获取当前分钟
	 * 
	 * @return 分钟
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentMinute() {
		Calendar calendar = Calendar.getInstance();
		return calendar.MINUTE;
	}

	/**
	 * 获取当前小时
	 * 
	 * @return 小时
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.HOUR_OF_DAY;
	}

	/**
	 * 获取当前�?
	 * 
	 * @return �?
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.DAY_OF_MONTH;
	}

	/**
	 * 获取当前�?
	 * 
	 * @return �?
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.MONTH;
	}

	/**
	 * 获取当前�?
	 * 
	 * @return �?
	 */
	@SuppressWarnings("static-access")
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.YEAR;
	}

	/**
	 * 计算剩余日期
	 * 
	 * @param remainTime
	 * @return
	 */
	public static String calculationRemainTime(String endTime, long countDown) { // 倒计�?

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			endTime = endTime.replace("/", "-");
			Date now = new Date(System.currentTimeMillis());// 获取当前时间
			Date endData = df.parse(endTime);
			long l = endData.getTime() - countDown - now.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			return day + "�?" + hour + "小时" + min + "�?" + s + "�?";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	// getTimeFromInt这个方法是计算时间差并转换成�?�?要格式的�?
	public static String getTimeFromInt(long time) {
		if (time <= 0) {
			return "已结�?";
		}
		long day = time / (1 * 60 * 60 * 24);
		long hour = time / (1 * 60 * 60) % 24;
		long minute = time / (1 * 60) % 60;
		long second = time / (1) % 60;
		return day + "�?" + hour + "小时" + minute + "�?" + second + "�?";
	}
	
	public static String[] getTimeFromInt_new(long time) {
		String[] hms = new String[3];
		if (time <= 0) {
			hms[0] = "";
			hms[1] = "";
			hms[2] = "";
		}else {
			long hour = time / (1 * 60 * 60);
			long minute = time / (1 * 60) % 60;
			long second = time / (1) % 60;
			if (hour < 10) {
				hms[0] = "0" + hour;
			}else {
				hms[0] = hour + "";
			}
			if (minute < 10) {
				hms[1] = "0" + minute;
			}else {
				hms[1] = minute + "";
			}
			if (second < 10) {
				hms[2] = "0" + second;
			}else {
				hms[2] = second + "";
			}
		}
		
		return hms;
	}
	
	
	public static String getTimeFromString(long time) {
		String hms = "";
		if (time <= 0) {
			hms = "已结�?";
		}else {
			String[] str = new String[4];
			long day = time / (1 * 60 * 60 * 24);
			long hour = time / (1 * 60 * 60) % 24;
			long minute = time / (1 * 60) % 60;
			long second = time / (1) % 60;
			if (day < 10) {
				str[0] = "0" + day;
			}else {
				str[0] = day + "";
			}
			if (hour < 10) {
				str[1] = "0" + hour;
			}else {
				str[1] = hour + "";
			}
			if (minute < 10) {
				str[2] = "0" + minute;
			}else {
				str[2] = minute + "";
			}
			if (second < 10) {
				str[3] = "0" + second;
			}else {
				str[3] = second + "";
			}
			hms = str[0]+":"+str[1]+":"+str[2]+":"+str[3];
		}
		
		return hms;
	}
	
	
	public static String[] getTimeFromInt_day(long time) {
		String[] hms = new String[4];
		if (time <= 0) {
			hms[0] = "";
			hms[1] = "";
			hms[2] = "";
			hms[3] = "";
		}else {
			long day = time / (1 * 60 * 60 * 24);
			long hour = time / (1 * 60 * 60) % 24;
			long minute = time / (1 * 60) % 60;
			long second = time / (1) % 60;
			if (day < 10) {
				hms[0] = "0" + day;
			}else{
				hms[0] = day + "";
			}
			if (hour < 10) {
				hms[1] = "0" + hour;
			}else {
				hms[1] = hour + "";
			}
			if (minute < 10) {
				hms[2] = "0" + minute;
			}else {
				hms[2] = minute + "";
			}
			if (second < 10) {
				hms[3] = "0" + second;
			}else {
				hms[3] = second + "";
			}
		}
		
		return hms;
	}

	public static String calculationRemainTime(String endTime, String nowTime,
			long countDown) { // 倒计�?

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			endTime = endTime.replace("/", "-");
			nowTime = nowTime.replace("/", "-");
			// Date now = new Date(System.currentTimeMillis());// 获取当前时间
			Date endData = df.parse(endTime);
			Date nowData = df.parse(nowTime);
			long l = endData.getTime() - countDown - nowData.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			return day + "�?" + hour + "小时" + min + "�?" + s + "�?";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 计算距离现在时间
	 * 
	 * @param startTime
	 * @param maxDay
	 *            like 5
	 * @return
	 */
	public static String calculationDistanceNowTime(String startTime,
			String nowTime, int maxDay) {
		startTime = startTime.replace("/", "-");// 2015�?8�?28�?14:01:15
		nowTime = nowTime.replace("/", "-");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date nowDate = df.parse(nowTime);
			Date startData = df.parse(startTime);
			long l = nowDate.getTime() - startData.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			if (day > maxDay) {
				SimpleDateFormat df_need = new SimpleDateFormat("yyyy-MM-dd");
				String needStr = df_need.format(startData);
				return needStr;
			} else if (day > 0) {
				return day + "天前";
			} else if (hour > 0) {
				return hour + "小时�?";
			} else if (min > 0) {
				return min + "分钟�?";
			} else {
				return Math.abs(s) + "秒前"; // 如果取得的时间是某些时区（不�?+8时区）的 会影响结果为负数
											// 取绝对�??
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	// http://forensicswiki.org/wiki/Google_Chrome
	public static long getUtcTime4CookieStr() {
		long utcTime = System.currentTimeMillis();
		long utcTimeLong = 116444736;
		utcTimeLong = utcTimeLong * 100;
		//
		utcTime = utcTime + utcTimeLong;
		utcTime = utcTime * 1000000;
		return utcTime;
	}

	public static String getZhongGuoTime() {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String times = format.format(new Date());
		System.out.print("日期格式---->" + times);
		return times;
	}

	/**
	 * if(result==0) {System.out.println("c1相等c2");} else if(result<0)
	 * {System.out.println("c1小于c2");} else {System.out.println("c1大于c2");}
	 * 
	 * @param timeStr01
	 * @param timeStr02
	 */
	public static int compareTime(String timeStr01, String timeStr02) {
		// String timeStr01="2008-01-25 09:12:09";
		// String timeStr02="2008-01-29 09:12:11";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar01 = Calendar.getInstance();
		Calendar calendar02 = Calendar.getInstance();
		try {
			calendar01.setTime(df.parse(timeStr01));
			calendar02.setTime(df.parse(timeStr02));
		} catch (java.text.ParseException e) {
			System.err.println("格式不正�?");
		}
		int result = calendar01.compareTo(calendar02);
		/*
		 * if(result==0) {System.out.println("c1相等c2");} else if(result<0)
		 * {System.out.println("c1小于c2");} else {System.out.println("c1大于c2");}
		 */
		return result;
	}

	/**
	 * like Calendar.HOUR_OF_DAY,10 十小时后 like Calendar.HOUR_OF_DAY,-10 十小时前 like
	 * Calendar.MINUTE,10 十分钟后 like Calendar.MINUTE,-10 十分钟前 like
	 * Calendar.SECOND,10 十秒钟后 like Calendar.SECOND,-10 十秒钟前 * like
	 * ca.add(Calendar.YEAR, -1); // 年份�?1 ca.add(Calendar.MONTH, -1);// 月份�?1
	 * ca.add(Calendar.DATE, -1);// 日期�?1 ...待扩�?
	 * 
	 * @param timeStr
	 */
	public static String getN_Time_Qian_Hou(String timeStr, int CalendarField,
			int CalendarValue) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar Cal = Calendar.getInstance();
		try {
			Cal.setTime(df.parse(timeStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cal.add(CalendarField, CalendarValue);
		System.out.println("date:" + df.format(Cal.getTime()));
		return df.format(Cal.getTime());
	}

	public static String getN_Time_Qian_Hou_4_Now(int CalendarField,
			int CalendarValue) {
		String timeStr = getZhongGuoTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar Cal = Calendar.getInstance();
		try {
			Cal.setTime(df.parse(timeStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cal.add(CalendarField, CalendarValue);
		System.out.println("date:" + df.format(Cal.getTime()));
		return df.format(Cal.getTime());
	}
}
