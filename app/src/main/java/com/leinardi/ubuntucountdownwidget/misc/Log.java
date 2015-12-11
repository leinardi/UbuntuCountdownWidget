package com.leinardi.ubuntucountdownwidget.misc;

public class Log
{
	private final static String LOGTAG = "UCW";

	public static final int NONE = 0;
	public static final int ERRORS_ONLY = 1;
	public static final int ERRORS_WARNINGS = 2;
	public static final int ERRORS_WARNINGS_INFO = 3;
	public static final int ERRORS_WARNINGS_INFO_DEBUG = 4;
	public static final int ERRORS_WARNINGS_INFO_DEBUG_VERBOSE = 5;

	private static final int LOGGING_LEVEL = ERRORS_WARNINGS_INFO_DEBUG_VERBOSE;

//	public static void wtf(String TAG, String logMe, Throwable ex){
//		if(LOGGING_LEVEL >= 1) android.util.Log.wtf(LOGTAG, TAG + ": " + logMe, ex);
//	}
//	
//	public static void wtf(String TAG, String logMe){
//		if(LOGGING_LEVEL >= 1) android.util.Log.wtf(LOGTAG, TAG + ": " + logMe);
//	}
	
	public static void e(String TAG, String logMe, Throwable ex){
		if(LOGGING_LEVEL >= 1) android.util.Log.e(LOGTAG, TAG + ": " + logMe, ex);
	}
	
	public static void e(String TAG, String logMe){
		if(LOGGING_LEVEL >= 1) android.util.Log.e(LOGTAG, TAG + ": " + logMe);
	}
	
	public static void w(String TAG, String logMe){
		if(LOGGING_LEVEL >= 2)  android.util.Log.w(LOGTAG, TAG + ": " + logMe);
	}
	
	public static void i(String TAG, String logMe){
		if(LOGGING_LEVEL >= 3) android.util.Log.i(LOGTAG, TAG + ": " + logMe);
	}

	public static void d(String TAG, String logMe){
		if(LOGGING_LEVEL >= 4) android.util.Log.d(LOGTAG, TAG + ": " + logMe);
	}
	
	public static void v(String TAG, String logMe){
		if(LOGGING_LEVEL >= 5) android.util.Log.v(LOGTAG, TAG + ": " + logMe);
	}
}