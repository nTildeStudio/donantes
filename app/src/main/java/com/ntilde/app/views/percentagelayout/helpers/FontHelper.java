package com.ntilde.app.views.percentagelayout.helpers;

import android.content.Context;
import android.graphics.Typeface;

import com.ntilde.app.R;

public class FontHelper {
	public static Typeface defaultFont; 
	
	public static Typeface getDefaultTypeface(Context context)
	{
		if(defaultFont == null){
            defaultFont = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.default_asset_font));
		}
		return defaultFont;
	}
}
