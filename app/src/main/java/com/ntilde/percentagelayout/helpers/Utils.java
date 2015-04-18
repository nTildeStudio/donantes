package com.ntilde.percentagelayout.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

public class Utils{
	/*
	 *  Identify if the device is a tablet or not
	 */
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	/*
	 * Configure screen orientation for activity depending device type
	 */
	public static void configureScreenOrientation(Activity activity){
		if (Utils.isTablet(activity)){
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}else{
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	

	

	
	
}
