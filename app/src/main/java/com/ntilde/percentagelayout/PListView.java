package com.ntilde.percentagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PListView extends ListView implements IPercentageLayout {
	
	private PercentageLayout pLayout;

	public PListView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
        pLayout=new PercentageLayout(context, attrs, defStyle, this);
	}

	public PListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		pLayout=new PercentageLayout(context, attrs, this);
	}

	public PListView(Context context) {
		super(context);
		pLayout=new PercentageLayout(context, this);
	}

	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		getLayoutParams().height=1;
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w,h,oldw,oldh);
		pLayout.applyStyleable();
    }
	
	@Override
	public int getPHeight() {
		return pLayout.getPHeight();
	}

	@Override
	public int getPWidth() {
		return pLayout.getPWidth();
	}

	@Override
	public float getPercentageHeight() {
		return pLayout.getPercentageHeight();
	}

	@Override
	public float getPercentageWidth() {
		return pLayout.getPercentageWidth();
	}

	@Override
	public boolean pixelExtraHeight() {
		return pLayout.pixelExtraHeight();
	}

	@Override
	public boolean pixelExtraWidth() {
		return pLayout.pixelExtraWidth();
	}

	@Override
	public void setPercentageParameters(float w, float h, float p) {
		setPercentageParameters(w, h, p, p, p, p);
	}

	@Override
	public void setPercentageParameters(float w, float h, float lp, float tp, float rp, float bp) {
		pLayout.setPercentageParameters(w, h, lp, tp, rp, bp);
	}

	@Override
	public void applyStyles() {
		pLayout.applyStyleable();
	}


}
