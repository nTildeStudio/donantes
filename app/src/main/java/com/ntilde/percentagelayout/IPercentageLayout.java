package com.ntilde.percentagelayout;

public interface IPercentageLayout{

	public int getPHeight();
	
	public int getPWidth();
	
	public float getPercentageHeight();
	
	public float getPercentageWidth();
	
	public boolean pixelExtraHeight();
	
	public boolean pixelExtraWidth();
	
	public void setPercentageParameters(float w, float h, float p);
	
	public void setPercentageParameters(float w, float h, float lp, float tp, float rp, float bp);
	
	public void applyStyles();
	
}
