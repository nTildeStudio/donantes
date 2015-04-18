package com.ntilde.percentagelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.GridView;

public class PGridView extends GridView implements IPercentageLayout{
	//Interfaces
	public interface GridViewTouchEventListener{
        public void onPulledDown();
        public void onPullUp();
        public void onScale(boolean up);
        public void onScaleUpChild(int overposition);
    }
   
    //Classes
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    	public static final float LIM_SCALE_UP = 1;
    	public static final float LIM_SCALE_DOWN = 0.9f;
    	
    	public void onScaleEnd(ScaleGestureDetector detector){
    		if(mTouchListener!=null){
    			if(detector.getScaleFactor()>=LIM_SCALE_UP){
//    				if(getNumColumns() ==1){
//    					if(detector.getFocusX() < getWidth()/2){
//    						mTouchListener.onScaleUpChild(getItemInPoint(detector.getFocusY()));
//    					}else{
//    						mTouchListener.onScaleUpChild(getItemInPoint(detector.getFocusY())+1);
//    					}
//    				}else{
    					mTouchListener.onScale(false);
//    				}
    			}else if(detector.getScaleFactor()<= LIM_SCALE_DOWN){
    				mTouchListener.onScale(true);
    			}
    		}
    	}
	}

    private static final int LIM_PULL_Y = 50;
    
	//Eventos
	private GridViewTouchEventListener mTouchListener;
	
    //Privates
    private ScaleGestureDetector mScaleGestureDetector;
	private PercentageLayout pLayout;
	private float lastY = 0;
	
	
	//Getters y Setters
	public void setGridViewTouchListener(GridViewTouchEventListener touchListener) {
        this.mTouchListener = touchListener;
    }

    public GridViewTouchEventListener getGridViewTouchListener(){
        return mTouchListener;
    }
    
	//Constructores
	public PGridView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		pLayout=new PercentageLayout(context, attrs, defStyle, this);
		mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
		
	}

	public PGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		pLayout=new PercentageLayout(context, attrs, this);
		mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	public PGridView(Context context) {
		super(context);
		pLayout = new PercentageLayout(context, this);
		mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
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
	
	//Scale
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
       super.onTouchEvent(ev);
       mScaleGestureDetector.onTouchEvent(ev);
       return true;
       
    }
	
	//PullDown
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		//Si hay mas de un dedo, no es scroll
		if(ev.getPointerCount()>1){
			return super.dispatchTouchEvent(ev);
		}
		
		if (ev.getAction() == MotionEvent.ACTION_DOWN){
			lastY = ev.getRawY();
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE){
			float newY = ev.getRawY();
			float distance = Math.abs(newY-lastY);
			
			if (mTouchListener != null){
				if(distance > LIM_PULL_Y){
					if(newY > lastY){
						if(getTopOfFristChild()>=0){
							mTouchListener.onPulledDown();
						}
					}
					else{
						mTouchListener.onPullUp();
					}
					lastY = newY;
				}
			}
		} else if (ev.getAction() == MotionEvent.ACTION_UP){
			lastY = 0;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	/**
	 * Because getScrollY() doesn�t work, this method returns position of first Child
	 * @return top position of first child
	 */
	public int getTopOfFristChild()
	{
	   int itemY = 0;
	   View view;

	   view = getChildAt(0);

	   if(view != null)
	      itemY = view.getTop();
	   return itemY;
	}
	
	private int getItemInPoint(float y){
		int position = -1;
		for(int i = 0; i< getChildCount(); i++){
			View v = getChildAt(i);
			if(v.getTop()<= y && (v.getTop()+ v.getHeight())>= y){
				return (Integer) v.getTag();
			}
		}
		return position;
	}
	
	
	
	
}
