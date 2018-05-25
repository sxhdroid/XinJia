package com.hnxjgou.xinjia.utils;


import com.hnxjgou.xinjia.BuildConfig;

/**
 * 日志输出
 */
public class LogUtil {
	
	public static final boolean isDebug = BuildConfig.DEBUG;
	
	/** Debug输出Log日志 **/
	public static void d(String tag, String msg) {
		if (isDebug) {
			android.util.Log.d(tag, msg);
		}
	}

	/** Error输出Log日志 **/
	public static void e(String tag, String msg) {
		if (isDebug) {
			android.util.Log.e(tag, msg);
		}
	}
	
	/** Info输出Log日志 **/
	public static void i(String tag, String msg) {
		if (isDebug) {
			android.util.Log.i(tag, msg);
		}
	}

	/**Send a VERBOSE log message*/
	public static void v(String tag, String msg) {
		if (isDebug) {
			android.util.Log.v(tag, msg);
		}
	}
}
