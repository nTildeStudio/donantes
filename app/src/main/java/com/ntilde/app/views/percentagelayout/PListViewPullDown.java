package com.ntilde.app.views.percentagelayout;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class PListViewPullDown extends PListView implements OnScrollListener{

    private ListViewTouchEventListener mTouchListener;
    private ListViewScaleEventListener mScaleListener;
    
    private ScaleGestureDetector mScaleGestureDetector;
    private boolean pulledDown;
    private int mLastFirstVisibleItem = 0;
    private int mLastLastVisibleItem = getLastVisiblePosition();
    private int scrollState = OnScrollListener.SCROLL_STATE_IDLE;
    private float lastY;
    //private float scale = 1f;
    private Context mContext;
    private boolean pulledDownNotified = false;
    private boolean notifyScale = true;

    public PListViewPullDown(Context context) {
        super(context);
        init(context);
    }

    public PListViewPullDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PListViewPullDown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    	mContext = context;
        setOnScrollListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleListener());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float newY = ev.getRawY();
            setPulledDown((newY - lastY) > 0);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mTouchListener != null) {
                        if (isPulledDown()) {
                            mTouchListener.onListViewPulledDown();
                            pulledDownNotified = false;
                            setPulledDown(false);
                        }
                    }
                }
            }, 400);
            lastY = newY;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            lastY = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

    /*
     * Interfaces to callback. Setters and getters included.
     */
    public interface ListViewTouchEventListener{
        public void onListViewPulledDown();
        public void onListViewNormalScroll();
    }
    
    public interface ListViewScaleEventListener{
    	public void onListViewScale();
    }

    public void setListViewTouchListener(ListViewTouchEventListener touchListener) {
        this.mTouchListener = touchListener;
    }

    public ListViewTouchEventListener getListViewTouchListener(){
        return mTouchListener;
    }
    
    public void setListViewScaleListener(ListViewScaleEventListener touchListener){
    	this.mScaleListener = touchListener;
    }
    
    public ListViewScaleEventListener getListViewScaleListener(){
    	return mScaleListener;
    }
    
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        setPulledDown(false);
        
          final int currentFirstVisibleItem = getFirstVisiblePosition();
          final int currentLastVisibleitem = getLastVisiblePosition();
          
          if (currentFirstVisibleItem != mLastFirstVisibleItem || currentLastVisibleitem != mLastLastVisibleItem){
        	  if (!pulledDownNotified){
        		  mTouchListener.onListViewNormalScroll();
        		  pulledDownNotified = true;
        	  }
          }
          
          mLastFirstVisibleItem = currentFirstVisibleItem;
          mLastLastVisibleItem = currentLastVisibleitem;
          
    }
          

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	this.scrollState = scrollState;
    }

    public boolean isPulledDown() {
        return pulledDown;
    }

    public void setPulledDown(boolean pulledDown) {
        this.pulledDown = pulledDown;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
       super.onTouchEvent(ev);
       mScaleGestureDetector.onTouchEvent(ev);
       return true;
    }
    
    public void notifyScale(){
    	notifyScale = true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    	
    	@Override
	    public void onScaleEnd(ScaleGestureDetector detector) {
	       //scale *= detector.getScaleFactor();
	       //scale = Math.max(0.1f, Math.min(scale, 5.0f));
	       Log.i("STR", "SCALE!!");
	       if (notifyScale){
	    	   notifyScale = false;
	    	   mScaleListener.onListViewScale();
	       }
    	}
	}
 
}