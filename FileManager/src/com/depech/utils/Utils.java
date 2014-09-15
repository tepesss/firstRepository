package com.depech.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

public class Utils {
	public static boolean isServiceRuning(Context context, Class<?> serviceType){
		ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		final String serviceName = serviceType.getName();
		  for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (serviceName.equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		return false;
	}

}
