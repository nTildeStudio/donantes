package com.ntilde.app.views.percentagelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ntilde.app.R;

public class PercentageLayout{
	
	private ViewGroup view;
	private float pPaddingLeft;
	private float pPaddingTop;
	private float pPaddingRight;
	private float pPaddingBottom;
	private float pHeight;
	private float pWidth;
	
	private int processedHeight=-1;
	private int processedWidth=-1;
	
	private int unusedPixelsHeight=-1;
	private int unusedPixelsWidth=-1;
	
	public PercentageLayout(Context context, ViewGroup view){
		this.view=view;
		initView(context, null);
	}
	
	public PercentageLayout(Context context, AttributeSet attrs, ViewGroup view){
		this.view=view;
		initView(context, attrs);
	}
	
	public PercentageLayout(Context context, AttributeSet attrs, int defStyle, ViewGroup view){
        this.view=view;
        initView(context, attrs);
    }
	
	private void initView(Context context, AttributeSet attrs){
		if(context==null||attrs==null){
			return;
		}
		
		TypedArray styledAttrs=null;
		if(view instanceof PLinearLayout){
			styledAttrs=context.getTheme().obtainStyledAttributes(attrs,R.styleable.PLinearLayout,0,0);
		}
		else if(view instanceof PRelativeLayout){
			styledAttrs=context.getTheme().obtainStyledAttributes(attrs,R.styleable.PRelativeLayout,0,0);
		}
		else if(view instanceof PScrollView){
			styledAttrs=context.getTheme().obtainStyledAttributes(attrs,R.styleable.PScrollView,0,0);
		}
		else if(view instanceof PHorizontalScrollView){
			styledAttrs=context.getTheme().obtainStyledAttributes(attrs,R.styleable.PHorizontalScrollView,0,0);
		}
		else if(view instanceof PListView){
			styledAttrs=context.getTheme().obtainStyledAttributes(attrs,R.styleable.PListView,0,0);
		}
		else if(view instanceof PGridView){
			styledAttrs=context.getTheme().obtainStyledAttributes(attrs,R.styleable.PGridView,0,0);
		}
		
		try{
			if(view instanceof PLinearLayout){
				float pPadding=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_padding, -1);
				pPaddingLeft=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_padding_left, pPadding);
				pPaddingTop=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_padding_top, pPadding);
				pPaddingRight=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_padding_right, pPadding);
				pPaddingBottom=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_padding_bottom, pPadding);
				
				pHeight=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_height, -1);
				pWidth=styledAttrs.getFloat(R.styleable.PLinearLayout_pl_percentage_width, -1);
			}
			else if(view instanceof PRelativeLayout){
				float pPadding=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_padding, -1);
				pPaddingLeft=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_padding_left, pPadding);
				pPaddingTop=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_padding_top, pPadding);
				pPaddingRight=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_padding_right, pPadding);
				pPaddingBottom=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_padding_bottom, pPadding);
				
				pHeight=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_height, -1);
				pWidth=styledAttrs.getFloat(R.styleable.PRelativeLayout_pr_percentage_width, -1);
			}
			else if(view instanceof PScrollView){
				float pPadding=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_padding, -1);
				pPaddingLeft=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_padding_left, pPadding);
				pPaddingTop=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_padding_top, pPadding);
				pPaddingRight=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_padding_right, pPadding);
				pPaddingBottom=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_padding_bottom, pPadding);
				
				pHeight=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_height, -1);
				pWidth=styledAttrs.getFloat(R.styleable.PScrollView_psv_percentage_width, -1);
            }
			else if(view instanceof PHorizontalScrollView){
				float pPadding=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_padding, -1);
				pPaddingLeft=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_padding_left, pPadding);
				pPaddingTop=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_padding_top, pPadding);
				pPaddingRight=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_padding_right, pPadding);
				pPaddingBottom=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_padding_bottom, pPadding);
				
				pHeight=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_height, -1);
				pWidth=styledAttrs.getFloat(R.styleable.PHorizontalScrollView_phsv_percentage_width, -1);
			}
			else if(view instanceof PListView){
				float pPadding=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_padding, -1);
				pPaddingLeft=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_padding_left, pPadding);
				pPaddingTop=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_padding_top, pPadding);
				pPaddingRight=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_padding_right, pPadding);
				pPaddingBottom=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_padding_bottom, pPadding);
				
				pHeight=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_height, -1);
				pWidth=styledAttrs.getFloat(R.styleable.PListView_plv_percentage_width, -1);
			}
			else if(view instanceof PGridView){
				float pPadding=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_padding, -1);
				pPaddingLeft=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_padding_left, pPadding);
				pPaddingTop=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_padding_top, pPadding);
				pPaddingRight=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_padding_right, pPadding);
				pPaddingBottom=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_padding_bottom, pPadding);
				
				pHeight=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_height, -1);
				pWidth=styledAttrs.getFloat(R.styleable.PGridView_gv_percentage_width, -1);
			}
		}
		finally{
			styledAttrs.recycle();
		}
	}
	
	protected void applyStyleable(){
	    //Esto deber�a solucionar los nullpointerexception en los giros
	    if(getRealParent() == null){
	        return;
	    }
	    
		prepareUnusedPixelHeight();
		prepareUnusedPixelWidth();
		
		//Altura y anchura
		int newHeight=getProcessedHeight();
		int newWidth=getProcessedWidth();
		
		//Obtenemos los padings
		int paddingPxLeft=pPaddingLeft==-1?view.getPaddingLeft():(int)(newWidth*pPaddingLeft/100);
		int paddingPxTop=pPaddingTop==-1?view.getPaddingTop():(int)(newHeight*pPaddingTop/100);
		int paddingPxRight=pPaddingRight==-1?view.getPaddingRight():(int)(newWidth*pPaddingRight/100);
		int paddingPxBottom=pPaddingBottom==-1?view.getPaddingBottom():(int)(newHeight*pPaddingBottom/100);
		
		//Fijamos los parametros del layout
		view.getLayoutParams().height=newHeight;
		view.getLayoutParams().width=newWidth;
		view.setPadding(paddingPxLeft, paddingPxTop, paddingPxRight, paddingPxBottom);
		
		view.invalidate();
		view.requestLayout();
		
		for(int i=0;i<view.getChildCount();i++){
			if(view.getChildAt(i) instanceof IPercentageLayout){
				if(view.getChildAt(i) instanceof PScrollView ||
						view.getChildAt(i) instanceof PHorizontalScrollView){
					ViewGroup vg=(ViewGroup)((ViewGroup)view.getChildAt(i)).getChildAt(0);
					for(int j=0;j<vg.getChildCount();j++){
						if(vg.getChildAt(j) instanceof IPercentageLayout){
							((IPercentageLayout)vg.getChildAt(j)).applyStyles();
						}
					}
				}
				else{
					((IPercentageLayout)view.getChildAt(i)).applyStyles();
				}
			}
		}
	}
	
	public int getPHeight(){
		return processedHeight;
	}
	
	private int getProcessedHeight(){
		if(pHeight==-1){
			return -2;
		}
		if(processedHeight>1){
			return processedHeight;
		}
		ViewParent parent=getRealParent();
		int parentHeight=0;
		if(parent instanceof IPercentageLayout){
			parentHeight=pHeight==-1?view.getHeight():((IPercentageLayout)parent).getPHeight();
		}
		else{
			parentHeight=pHeight==-1?view.getHeight():((ViewGroup)view.getParent()).getHeight();
		}
		int newHeight=pHeight==-1?view.getHeight():(int)(parentHeight*pHeight/100);
		float dif=(parentHeight*pHeight/100)-((int)(parentHeight*pHeight/100));
		if(dif!=0 && getRealParent() instanceof IPercentageLayout){
			if(((IPercentageLayout)getRealParent()).pixelExtraHeight()){
				newHeight++;
			}
		}
		processedHeight=newHeight;
		return newHeight;
	}

	public int getPWidth(){
		return processedWidth;
	}
	
	private int getProcessedWidth(){
		if(processedWidth>1){
			return processedWidth;
		}
		ViewParent parent=getRealParent();
		int parentWidth=0;
		if(parent instanceof IPercentageLayout){
			parentWidth=pWidth==-1?view.getWidth():((IPercentageLayout)parent).getPWidth();
		}
		else{
			parentWidth=pWidth==-1?view.getWidth():((ViewGroup)view.getParent()).getWidth();
		}
		int newWidth=pWidth==-1?view.getWidth():(int)(parentWidth*pWidth/100);
		float dif=(parentWidth*pWidth/100)-((int)(parentWidth*pWidth/100));
		if(dif!=0 && getRealParent() instanceof IPercentageLayout){
			if(((IPercentageLayout)getRealParent()).pixelExtraWidth()){
				newWidth++;
			}
		}
		processedWidth=newWidth;
		return newWidth;
	}
	
	private ViewParent getRealParent(){
	    try{
    		if(view.getParent()!=view.getRootView()&&view.getParent().getParent() instanceof PScrollView){
    			return view.getParent().getParent();
    		}else if(view.getParent()!=view.getRootView()&&view.getParent().getParent() instanceof PHorizontalScrollView){
    			return view.getParent().getParent();
    		}		
    		else if(view.getParent()!=view.getRootView()&&view.getParent().getParent() instanceof PDrawerLayout){
    			return view.getParent().getParent();
    		}
    		else{
    			return view.getParent();
    		}
	    }catch(Exception ex){
	        Log.e(getClass().getName(), "getRealParent", ex);
	        return null;
	    }
	}
	
	public float getPercentageHeight(){
		return pHeight;
	}
	
	public float getPercentageWidth(){
		return pWidth;
	}
	
	private void prepareUnusedPixelHeight(){
		int newHeight=getProcessedHeight();
		
		if(view.getChildCount()>0&&newHeight>1&&unusedPixelsHeight==-1){
			float unusedSize=0;
			float vActSize;
			View vAct;
			for(int i=0;i<view.getChildCount();i++){
				vAct=view.getChildAt(i);
				if(vAct instanceof IPercentageLayout){
					vActSize=((IPercentageLayout)vAct).getPercentageHeight()*newHeight/100;
					unusedSize+=vActSize-(int)vActSize;
				}
			}
			unusedPixelsHeight=Math.round(unusedSize);
		}
	}
	
	private void prepareUnusedPixelWidth(){
		int newWidth=getProcessedWidth();
		
		if(view.getChildCount()>0&&newWidth>1&&unusedPixelsWidth==-1){
			float unusedSize=0;
			float vActSize;
			View vAct;
			for(int i=0;i<view.getChildCount();i++){
				vAct=view.getChildAt(i);
				if(vAct instanceof IPercentageLayout){
					vActSize=((IPercentageLayout)vAct).getPercentageWidth()*newWidth/100;
					unusedSize+=vActSize-(int)vActSize;
				}
			}
			unusedPixelsWidth=Math.round(unusedSize);
		}
	}
	
	public boolean pixelExtraHeight(){
		if(unusedPixelsHeight>0){
			unusedPixelsHeight--;
			return true;
		}
		return false;
	}
	
	public boolean pixelExtraWidth(){
		if(unusedPixelsWidth>0){
			unusedPixelsWidth--;
			return true;
		}
		return false;
	}
	
	public void setPercentageHeight(float height){
		setPercentageHeight(height, true);
	}
	
	private void setPercentageHeight(float height, boolean unique){
		processedHeight=height!=-1?0:processedHeight;
		pHeight=height!=-1?height:pHeight;
		if(unique){
			applyStyleable();
		}
	}
	
	public void setPercentageWidth(float width){
		setPercentageWidth(width, true);
	}
	
	private void setPercentageWidth(float width, boolean unique){
		processedWidth=width!=-1?0:processedWidth;
		pWidth=width!=-1?width:pWidth;
		if(unique){
			applyStyleable();
		}
	}
	
	public void setPercentageParameters(float w, float h, float lp, float tp, float rp, float bp){
		setPercentageHeight(h, false);
		setPercentageWidth(w, false);
		pPaddingLeft=lp!=-1?lp:pPaddingLeft;
		pPaddingTop=tp!=-1?tp:pPaddingTop;
		pPaddingRight=rp!=-1?rp:pPaddingRight;
		pPaddingBottom=bp!=-1?bp:pPaddingBottom;
		applyStyleable();
	}

}
