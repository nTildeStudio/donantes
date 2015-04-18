package com.ntilde.percentagelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.ntilde.donantes.R;
import com.ntilde.percentagelayout.helpers.FontHelper;

public class PEditText extends EditText{
	public void setPHeight(int resDimen){
		TypedValue tv=new TypedValue();
		getResources().getValue(resDimen, tv, true);
		setPHeight(tv.getFloat());
	}
	
	public void setPHeight(float pHeight){
		if(pHeight<0){
			return;
		}
		calcHeight(pHeight);
	}
	
	public void setAssetFont(String assetFont){
		try{
			Typeface font = Typeface.createFromAsset(getContext().getAssets(), assetFont);
			setTypeface(font);
		}
		catch(Exception ex){
			Log.v(getClass().getName(),"No existe la fuente asignada", ex);
			try{
				setTypeface(FontHelper.getDefaultTypeface(getContext()));
			}catch(Exception e){
				Log.v(getClass().getName(),"No existe la fuente por defecto", e);
			}
		}
	}
	
	
	public PEditText(Context context){
		super(context);
		init(null);
	}
	
	public PEditText(Context context, AttributeSet attrs){
		super(context, attrs);
		init(attrs);
	}
	
	public PEditText(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs);
    }
	
	private void init(AttributeSet attrs){
		if(attrs==null){
			return;
		}
        TypedArray attrsArray=getContext().obtainStyledAttributes(attrs,R.styleable.PEditText);
		float pHeight=attrsArray.getFloat(R.styleable.PEditText_pet_height, 0);
		String assetFont = attrsArray.getString(R.styleable.PEditText_pet_font_asset);
		
		setAssetFont(assetFont);
		calcHeight(pHeight);
		attrsArray.recycle();
	}
	
	private void calcHeight(float pHeight){
		if(pHeight!=0){
			setIncludeFontPadding(false);
			WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display=wm.getDefaultDisplay();
			Point screenSize=new Point(0,0);
			display.getSize(screenSize);
			float newPixlesHeight=pHeight*screenSize.y/100;
			float scaledDensity=getContext().getResources().getDisplayMetrics().scaledDensity;
			setTextSize(newPixlesHeight/scaledDensity);
		}
		
	}
}