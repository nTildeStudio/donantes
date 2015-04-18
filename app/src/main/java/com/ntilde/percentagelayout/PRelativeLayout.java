package com.ntilde.percentagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class PRelativeLayout extends RelativeLayout implements IPercentageLayout{

	private PercentageLayout pLayout;
	
	public PRelativeLayout(Context context){
		super(context);
		pLayout=new PercentageLayout(context, this);
	}
	
	public PRelativeLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		pLayout=new PercentageLayout(context, attrs, this);
	}
	
	public PRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        pLayout=new PercentageLayout(context, attrs, defStyle, this);
    }
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		getLayoutParams().height=1;
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w,h,oldw,oldh);		
		pLayout.applyStyleable();
    }
	
    public int getPHeight(){
    	return pLayout.getPHeight();
    }
    
    public int getPWidth(){
    	return pLayout.getPWidth();
    }
    
	public float getPercentageHeight(){
		return pLayout.getPercentageHeight();
	}

	public float getPercentageWidth(){
		return pLayout.getPercentageWidth();
	}
	
	public boolean pixelExtraHeight(){
		return pLayout.pixelExtraHeight();
	}
	
	public boolean pixelExtraWidth(){
		return pLayout.pixelExtraWidth();
	}
	
	public void setPercentageParameters(float w, float h, float p){
		setPercentageParameters(w, h, p, p, p, p);
	}
	
	public void setPercentageParameters(float w, float h, float lp, float tp, float rp, float bp){
		pLayout.setPercentageParameters(w, h, lp, tp, rp, bp);
	}
	
	public void applyStyles(){
		pLayout.applyStyleable();
	}

}
