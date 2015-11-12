package com.ntilde.donantes.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.ntilde.donantes.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DonantesCalendarView extends View{

    private OnSelectedDateChangeListener mOnSelectedDateChangeListener;

    private static final int MONTH_VIEW=1;
    private static final int YEAR_VIEW=2;
    private static final int YEARS_VIEW=3;

    private int actualView=MONTH_VIEW;

    private Canvas c;

    private TreeMap<Long,DonantesCalendarEvent> mEvents;
    private HashMap<Long,Integer> mEventAssociated;

    private Calendar cal, calDaysName;
    private int mWeekCount;
    private int mActualMonth;
    String mMonthName, mYearName;

    private GestureDetector mDetector = new GestureDetector(DonantesCalendarView.this.getContext(), new mListener());

    private boolean mHighlightToday;
    private boolean mDisplayMonthName;
    private boolean mDisplayDaysName;
    private int mDayBoxStrokeWidth;
    private int mFirstDayOfWeekReaded, mFirstDayOfWeek, mBlankDays;

    private Paint mBackground;
    private Paint mMonthText;
    private Paint mSelectedMonthText;
    private Paint mDaysNameText;
    private Paint mEnabledDayText;
    private Paint mEnabledDayBoxFill;
    private Paint mEnabledDayBoxStroke;
    private Paint mSelectedDayBoxFill;
    private Paint mSelectedDayBoxStroke;
    private Paint mSelectedDayBoxCircle;
    private Paint mPreselectedDayBoxCircle;
    private Paint mEventBoxFill;

    private Path triangleLeft, triangleRight;

    private int mDayWidth, mMonthWidth;
    private int mDayHeight, mMonthHeight;

    private int mSelectedDay=-1;
    private Date mSelectedDate=null;
    private int mPreselectedDay=-1;
    private boolean mPreselectedTitle =false;
    private boolean mPreselectedLeft=false;
    private boolean mPreselectedRight=false;

    public DonantesCalendarView(Context context) {
        super(context);

        init(true);
    }

    public DonantesCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DonantesCalendarView, 0, 0);

        try{
            mHighlightToday = a.getBoolean(R.styleable.DonantesCalendarView_highlightToday, false);
            mDisplayMonthName = a.getBoolean(R.styleable.DonantesCalendarView_displayMonthName, true);
            mDisplayDaysName = a.getBoolean(R.styleable.DonantesCalendarView_displayDaysName, true);
            mFirstDayOfWeekReaded = a.getInt(R.styleable.DonantesCalendarView_firstDayOfWeek, 1);
            mDayBoxStrokeWidth=1;
        }
        finally {
            a.recycle();
        }

        init(true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putSerializable("mEvents", mEvents);
        bundle.putSerializable("mEventAssociated", mEventAssociated);
        bundle.putSerializable("cal", cal);
        bundle.putInt("mSelectedDay", mSelectedDay);
        bundle.putSerializable("mSelectedDate", mSelectedDate);
        bundle.putBoolean("mDisplayDaysName", mDisplayDaysName);
        bundle.putBoolean("mDisplayMonthName", mDisplayMonthName);
        bundle.putInt("actualView", actualView);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mEvents = (TreeMap<Long,DonantesCalendarEvent>)bundle.getSerializable("mEvents");
            mEventAssociated = (HashMap<Long, Integer>)bundle.getSerializable("mEventAssociated");
            cal = (Calendar)bundle.getSerializable("cal");
            mSelectedDay = bundle.getInt("mSelectedDay");
            mSelectedDate = (Date) bundle.getSerializable("mSelectedDate");
            mDisplayDaysName = bundle.getBoolean("mDisplayDaysName");
            mDisplayMonthName = bundle.getBoolean("mDisplayMonthName");
            actualView = bundle.getInt("actualView");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
        init(true);
    }

    private  void init(boolean firstInit){
        if(firstInit) {

            if (mEvents == null){
                mEvents = new TreeMap<>();
            }
            if(mEventAssociated==null) {
                mEventAssociated = new HashMap<>();
            }

            switch (mFirstDayOfWeekReaded) {
                case 1:mFirstDayOfWeek=Calendar.MONDAY;break;
                case 2:mFirstDayOfWeek=Calendar.TUESDAY;break;
                case 3:mFirstDayOfWeek=Calendar.WEDNESDAY;break;
                case 4:mFirstDayOfWeek=Calendar.THURSDAY;break;
                case 5:mFirstDayOfWeek=Calendar.FRIDAY;break;
                case 6:mFirstDayOfWeek=Calendar.SATURDAY;break;
                case 7:mFirstDayOfWeek=Calendar.SUNDAY;break;
            }

            if(cal==null) {
                cal = Calendar.getInstance();
            }

            mWeekCount = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
            calDaysName = Calendar.getInstance();
            calDaysName.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);

            mBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBackground.setStyle(Paint.Style.FILL);
            mBackground.setColor(Color.WHITE);

            mMonthText = new Paint(Paint.ANTI_ALIAS_FLAG);
            mMonthText.setColor(Color.BLACK);

            mSelectedMonthText = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSelectedMonthText.setColor(Color.GRAY);

            mDaysNameText = new Paint(Paint.ANTI_ALIAS_FLAG);
            mDaysNameText.setColor(Color.GRAY);

            mEnabledDayText = new Paint(Paint.ANTI_ALIAS_FLAG);
            mEnabledDayText.setColor(Color.BLACK);

            mEnabledDayBoxFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            mEnabledDayBoxFill.setStyle(Paint.Style.FILL);
            mEnabledDayBoxFill.setColor(Color.rgb(230,230,230));

            mEnabledDayBoxStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
            mEnabledDayBoxStroke.setStyle(Paint.Style.STROKE);
            mEnabledDayBoxStroke.setColor(Color.WHITE);
            mEnabledDayBoxStroke.setStrokeWidth(mDayBoxStrokeWidth);

            mSelectedDayBoxFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSelectedDayBoxFill.setStyle(Paint.Style.FILL);
            mSelectedDayBoxFill.setColor(Color.rgb(230, 230, 230));

            mSelectedDayBoxStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSelectedDayBoxStroke.setStyle(Paint.Style.STROKE);
            mSelectedDayBoxStroke.setColor(Color.WHITE);
            mSelectedDayBoxStroke.setStrokeWidth(mDayBoxStrokeWidth);

            mSelectedDayBoxCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSelectedDayBoxCircle.setStyle(Paint.Style.FILL);
            mSelectedDayBoxCircle.setColor(Color.WHITE);

            mPreselectedDayBoxCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPreselectedDayBoxCircle.setStyle(Paint.Style.FILL);
            mPreselectedDayBoxCircle.setColor(Color.rgb(245, 245, 245));

            mEventBoxFill = new Paint(Paint.ANTI_ALIAS_FLAG);
            mEventBoxFill.setStyle(Paint.Style.FILL);
        }
        mActualMonth = cal.get(Calendar.MONTH);
        String monthName = String.format(Locale.getDefault(),"%tB",cal);
        mMonthName = Character.toUpperCase(monthName.charAt(0)) + monthName.substring(1);
        mYearName = cal.get(Calendar.YEAR)+"";
        cal.setFirstDayOfWeek(mFirstDayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void initEventsAssociated(){
        mEventAssociated = new HashMap<>();
        for(DonantesCalendarEvent event:mEvents.values()) {
            Map<Integer, Integer> eventAssociated = event.getRanges();
            if (eventAssociated != null) {
                for (Map.Entry<Integer, Integer> range : eventAssociated.entrySet()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(event.getDate());
                    for (int i = 0; i < range.getValue(); i++) {
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        mEventAssociated.put(c.getTimeInMillis(), range.getKey());
                    }
                }
            }
        }
        invalidate();
    }

    private void drawCenter(Canvas canvas, Paint paint, String text, Rect area) {
        Rect r = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = area.left + area.width() / 2f - r.width() / 2f - r.left;
        float y = area.top + area.height() / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        c=canvas;
        switch (actualView){
            case MONTH_VIEW:
                drawMonth(canvas);
                break;
            case YEAR_VIEW:
                drawYear(canvas);
                break;
            case YEARS_VIEW:
                break;
        }

    }

    private void drawYear(Canvas canvas){

        canvas.drawRect(canvas.getClipBounds(), mBackground);

        cal.set(Calendar.MONTH, 0);

        int left, top=0, right, bottom=mMonthHeight;

        if(mDisplayMonthName){
            Rect area=new Rect(0, top, canvas.getClipBounds().width(), mDayHeight);
            mMonthText.setTextSize(canvas.getClipBounds().width()/17);
            mSelectedMonthText.setTextSize(canvas.getClipBounds().width()/17);
            drawCenter(canvas, mPreselectedTitle ? mSelectedMonthText : mMonthText, mYearName, area);
            top+=mDayHeight;
            bottom+=mMonthHeight;

            if(triangleLeft==null) {
                triangleLeft = new Path();
                triangleLeft.moveTo(mDayWidth * 0.3f, mDayHeight / 2);
                triangleLeft.lineTo(mDayWidth * 0.6f, mDayHeight * 0.3f);
                triangleLeft.lineTo(mDayWidth * 0.6f, mDayHeight * 0.7f);
                triangleLeft.close();
                triangleRight = new Path();
                triangleRight.moveTo(canvas.getClipBounds().width() - mDayWidth * 0.3f, mDayHeight / 2);
                triangleRight.lineTo(canvas.getClipBounds().width() - mDayWidth * 0.6f, mDayHeight * 0.3f);
                triangleRight.lineTo(canvas.getClipBounds().width() - mDayWidth * 0.6f, mDayHeight * 0.7f);
                triangleRight.close();
            }
            canvas.drawPath(triangleLeft, mPreselectedLeft?mSelectedMonthText:mMonthText);
            canvas.drawPath(triangleRight, mPreselectedRight?mSelectedMonthText:mMonthText);
        }

        for(int monthBlock=0;monthBlock<3;monthBlock++) {
            left = 0;
            right = mMonthWidth;
            for (int month = 0; month < 4; month++) {

                canvas.drawRect(left, top, right, bottom, mSelectedDayBoxFill);
                canvas.drawRect(left, top, right, bottom, mSelectedDayBoxStroke);

                Rect area=new Rect(mMonthWidth*month, top, mMonthWidth*(month+1), top+mDayHeight/2);
                String monthName=String.format(Locale.getDefault(), "%tB", cal).substring(0,3).toUpperCase();
                mMonthText.setTextSize(canvas.getClipBounds().width() / 30);
                drawCenter(canvas, mMonthText, monthName, area);

                cal.set(Calendar.DAY_OF_MONTH, 1);
                int actualMonth=cal.get(Calendar.MONTH);
                cal.getTime();
                cal.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);
                int monthWeeks=cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
                for(int week=0;week<=monthWeeks;week++){
                    for(int day=0;day<7;day++){
                        if(cal.get(Calendar.MONTH)==actualMonth) {
                            int l=left+mMonthWidth/7*day;
                            int t=top+mDayHeight/2+(mMonthHeight-mDayHeight/2)/6*week;
                            int r=(left+(mMonthWidth)/7*(day+1));
                            int b=top+mDayHeight/2+(mMonthHeight-mDayHeight/2)/6*(week+1);
                            Rect dayArea=new Rect(l, t, r, b);
                            mMonthText.setTextSize(canvas.getClipBounds().width() / 47);
                            mDaysNameText.setTextSize(canvas.getClipBounds().width() / 47);
                            if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
                                drawCenter(canvas, mDaysNameText, cal.get(Calendar.DAY_OF_MONTH) + "", dayArea);
                            }
                            else {
                                drawCenter(canvas, mMonthText, cal.get(Calendar.DAY_OF_MONTH) + "", dayArea);
                            }
                            if(mEvents.keySet().contains(cal.getTimeInMillis())){
                                DonantesCalendarEvent event=mEvents.get(cal.getTimeInMillis());
                                mEventBoxFill.setColor(event.getColor());
                                canvas.drawRect(l, t + (int) ((mMonthHeight - mDayHeight / 2) / 6 * 0.90), r, b, mEventBoxFill);
                            }
                            else if(mEventAssociated.keySet().contains(cal.getTimeInMillis())){
                                mEventBoxFill.setColor(mEventAssociated.get(cal.getTimeInMillis()));
                                canvas.drawRect(l, t + (int) ((mMonthHeight-mDayHeight/2)/6 * 0.90), r, b, mEventBoxFill);
                            }
                        }
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }

                left+=mMonthWidth+mDayBoxStrokeWidth;
                right+=mMonthWidth+mDayBoxStrokeWidth;
            }
            top+=mMonthHeight+mDayBoxStrokeWidth;
            bottom+=mMonthHeight+mDayBoxStrokeWidth;
        }
        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.MONTH, 0);
    }

    private void drawMonth(Canvas canvas){

        canvas.drawRect(canvas.getClipBounds(), mBackground);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, mFirstDayOfWeek);

        int left, top=0, right, bottom=mDayHeight;

        if(mDisplayMonthName){
            Rect area=new Rect(0, top, canvas.getClipBounds().width(), mDayHeight);
            mMonthText.setTextSize(canvas.getClipBounds().width()/17);
            mSelectedMonthText.setTextSize(canvas.getClipBounds().width()/17);
            drawCenter(canvas, mPreselectedTitle ? mSelectedMonthText : mMonthText, mMonthName, area);
            top+=mDayHeight;
            bottom+=mDayHeight;

            if(triangleLeft==null) {
                triangleLeft = new Path();
                triangleLeft.moveTo(mDayWidth * 0.3f, mDayHeight / 2);
                triangleLeft.lineTo(mDayWidth * 0.6f, mDayHeight * 0.3f);
                triangleLeft.lineTo(mDayWidth * 0.6f, mDayHeight * 0.7f);
                triangleLeft.close();
                triangleRight = new Path();
                triangleRight.moveTo(canvas.getClipBounds().width() - mDayWidth * 0.3f, mDayHeight / 2);
                triangleRight.lineTo(canvas.getClipBounds().width() - mDayWidth * 0.6f, mDayHeight * 0.3f);
                triangleRight.lineTo(canvas.getClipBounds().width() - mDayWidth * 0.6f, mDayHeight * 0.7f);
                triangleRight.close();
            }
            canvas.drawPath(triangleLeft, mPreselectedLeft?mSelectedMonthText:mMonthText);
            canvas.drawPath(triangleRight, mPreselectedRight?mSelectedMonthText:mMonthText);
        }

        if(mDisplayDaysName){
            Rect area=new Rect(0, top, mDayWidth+mDayBoxStrokeWidth,top+mDayHeight/2);
            for(int dia=0;dia<7;dia++){
                String titulo=String.format(Locale.getDefault(),"%ta",calDaysName).substring(0,2);
                titulo=Character.toUpperCase(titulo.charAt(0)) + titulo.substring(1);
                mDaysNameText.setTextSize(canvas.getClipBounds().width()/22);
                drawCenter(canvas, mDaysNameText, titulo, area);
                area.left+=mDayWidth+mDayBoxStrokeWidth;
                area.right+=mDayWidth+mDayBoxStrokeWidth;
                calDaysName.add(Calendar.DAY_OF_MONTH, 1);
            }
            top+=mDayHeight/2;
            bottom+=mDayHeight/2;
        }

        int radius = Math.min(mDayWidth,mDayHeight) / 2;
        mEnabledDayText.setTextSize(canvas.getClipBounds().width() / 20);
        mBlankDays=0;
        for(int week=0;week<=mWeekCount;week++){
            left=0;
            right=mDayWidth;
            for(int day=0;day<7;day++){
                if(cal.get(Calendar.MONTH)==mActualMonth) {
                    if (mSelectedDay != -1 && week * 7 + day == mSelectedDay) {
                        mSelectedDate=cal.getTime();
                        canvas.drawRect(left, top, right, bottom, mSelectedDayBoxFill);
                        canvas.drawRect(left, top, right, bottom, mSelectedDayBoxStroke);
                        canvas.drawCircle(left + mDayWidth/2, top + mDayHeight/2, (int) (radius * .8), mSelectedDayBoxCircle);
                    }
                    else {
                        canvas.drawRect(left, top, right, bottom, mEnabledDayBoxFill);
                        canvas.drawRect(left, top, right, bottom, mEnabledDayBoxStroke);
                        if(mPreselectedDay!=-1 && week * 7 + day == mPreselectedDay){
                            canvas.drawCircle(left + mDayWidth/2, top + mDayHeight/2, (int) (radius * .8), mPreselectedDayBoxCircle);
                        }
                    }
                    drawCenter(canvas, mEnabledDayText, "" + cal.get(Calendar.DAY_OF_MONTH), new Rect(left, top, right, bottom));
                    if(mEvents.keySet().contains(cal.getTimeInMillis())){
                        DonantesCalendarEvent event=mEvents.get(cal.getTimeInMillis());
                        mEventBoxFill.setColor(event.getColor());
                        canvas.drawRect(left, top+(int)(mDayHeight*0.95), right, bottom, mEventBoxFill);
                    }
                    else if(mEventAssociated.keySet().contains(cal.getTimeInMillis())){
                        mEventBoxFill.setColor(mEventAssociated.get(cal.getTimeInMillis()));
                        canvas.drawRect(left, top + (int) (mDayHeight * 0.95), right, bottom, mEventBoxFill);
                    }
                }
                else if(week==0){
                    mBlankDays++;
                }
                left+=mDayWidth+mDayBoxStrokeWidth;
                right+=mDayWidth+mDayBoxStrokeWidth;
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            top+=mDayHeight+mDayBoxStrokeWidth;
            bottom+=mDayHeight+mDayBoxStrokeWidth;
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);
        if (!result){
            if(actualView==MONTH_VIEW) {
                mSelectedDate=null;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int selectedDay = touchedDay(event.getX(), event.getY());
                    if (selectedDay != -1) {
                        mSelectedDay = mSelectedDay == selectedDay ? -1 : selectedDay;
                        if(mSelectedDay!=-1){
                            if(mOnSelectedDateChangeListener!=null) {
                                Calendar calClone=(Calendar)cal.clone();
                                calClone.set(Calendar.DAY_OF_MONTH, mSelectedDay-mBlankDays+1);
                                mOnSelectedDateChangeListener.OnSelectedDateChange(calClone.getTime(), getEvent(calClone.getTime()));
                            }
                        }
                        else{
                            if(mOnSelectedDateChangeListener!=null){
                                mOnSelectedDateChangeListener.OnSelectedDateChange(null, null);
                            }
                        }
                    } else if (touchedTitle(event.getX(), event.getY())) {
                        actualView = YEAR_VIEW;
                        init(false);
                    } else if (touchedLeft(event.getX(), event.getY())) {
                        cal.add(Calendar.MONTH, -1);
                        mSelectedDay = -1;
                        mSelectedDate = null;
                        init(false);
                    } else if (touchedRight(event.getX(), event.getY())) {
                        cal.add(Calendar.MONTH, 1);
                        mSelectedDay = -1;
                        mSelectedDate = null;
                        init(false);
                    }
                    mPreselectedDay = -1;
                    mPreselectedTitle = false;
                    mPreselectedLeft = false;
                    mPreselectedRight = false;
                    invalidate();
                    result = true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mPreselectedDay = touchedDay(event.getX(), event.getY());
                    mPreselectedTitle = touchedTitle(event.getX(), event.getY());
                    mPreselectedLeft = touchedLeft(event.getX(), event.getY());
                    mPreselectedRight = touchedRight(event.getX(), event.getY());
                    invalidate();
                }
            }
            else if(actualView==YEAR_VIEW){
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int selectedMonth = touchedMonth(event.getX(), event.getY());
                    if (selectedMonth != -1) {
                        cal.set(Calendar.MONTH, selectedMonth);
                        actualView=MONTH_VIEW;
                        mSelectedDay=-1;
                        mSelectedDate=null;
                        init(false);
                    } else if (touchedTitle(event.getX(), event.getY())) {

                    } else if (touchedLeft(event.getX(), event.getY())) {
                        cal.add(Calendar.YEAR, -1);
                        init(false);
                    } else if (touchedRight(event.getX(), event.getY())) {
                        cal.add(Calendar.YEAR, 1);
                        init(false);
                    }
                    mPreselectedDay = -1;
                    mPreselectedTitle = false;
                    mPreselectedLeft = false;
                    mPreselectedRight = false;
                    invalidate();
                    result = true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mPreselectedDay = touchedDay(event.getX(), event.getY());
                    mPreselectedTitle = touchedTitle(event.getX(), event.getY());
                    mPreselectedLeft = touchedLeft(event.getX(), event.getY());
                    mPreselectedRight = touchedRight(event.getX(), event.getY());
                    invalidate();
                }
            }
        }
        return result;
    }

    private int touchedDay(float x, float y){
        int day=-1;
        if(mDisplayMonthName&&y<=mDayHeight||
                !mDisplayMonthName&&mDisplayDaysName&&y<=mDayHeight/2||
                mDisplayMonthName&&mDisplayDaysName&&y<=mDayHeight*1.5){
            return day;
        }
        if(mDisplayMonthName){
            y-=mDayHeight;
        }
        if(mDisplayDaysName){
            y-=mDayHeight/2;
        }
        day=(int)(y/(mDayHeight+mDayBoxStrokeWidth*2))*7;
        day+=(int)(x/(mDayWidth+mDayBoxStrokeWidth*2));
        return day;
    }

    private int touchedMonth(float x, float y) {
        int month=-1;

        if(y<=mDayHeight){
            return month;
        }
        y-=mDayHeight;
        month=(int)(y/(mMonthHeight+mDayBoxStrokeWidth*2))*4;
        month+=(int)(x/(mMonthWidth+mDayBoxStrokeWidth*2));
        return month;
    }

    private boolean touchedLeft(float x, float y){
        return x<=mDayWidth&&y<=mDayHeight;
    }

    private boolean touchedRight(float x, float y){
        return x>=c.getClipBounds().width()-mDayWidth&&y<=mDayHeight;
    }

    private boolean touchedTitle(float x, float y){
        return actualView==MONTH_VIEW&&x>=mDayWidth&&x<=c.getClipBounds().width()-mDayWidth&&y<=mDayHeight;
    }

    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mDayWidth=w/7-mDayBoxStrokeWidth;
        //mDayHeight=mDayWidth;
        mDayHeight=(h-(mDisplayMonthName?mDayWidth:0)-(mDisplayDaysName?mDayWidth/2:0))/6;
        mMonthWidth=w/4-mDayBoxStrokeWidth;
        mMonthHeight=(h-mDayHeight)/3-mDayBoxStrokeWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float  heightInWeeks=6;
        heightInWeeks+=mDisplayMonthName?1:0;
        heightInWeeks+=mDisplayDaysName?0.5:0;

        int desiredWidth = (int)((heightSize+mDayBoxStrokeWidth+1)/heightInWeeks*7);
        int desiredHeight = (int)((widthSize+mDayBoxStrokeWidth+1)/7*heightInWeeks);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    //GETTERS AND SETTERS

    public void setOnSelectedDateChangeListener(OnSelectedDateChangeListener onSelectedDateChangeListener){
        mOnSelectedDateChangeListener = onSelectedDateChangeListener;
    }

    public DonantesCalendarEvent getSelectedEvent(){
        if(mSelectedDate!=null) {
            return mEvents.get(mSelectedDate.getTime());
        }
        else{
            return null;
        }
    }

    public DonantesCalendarEvent getEvent(Date date){
        if(date!=null) {
            return mEvents.get(date.getTime());
        }
        else{
            return null;
        }
    }

    public boolean isHighlightToday(){
        return mHighlightToday;
    }

    public void setHighlightToday(boolean highlightToday){
        mHighlightToday = highlightToday;
        invalidate();
        requestLayout();
    }

    public boolean isDisplayMonthName() {
        return mDisplayMonthName;
    }

    public void setDisplayMonthName(boolean displayMonthName){
        mDisplayMonthName = displayMonthName;
        invalidate();
        requestLayout();
    }

    public boolean isDisplayDaysName() {
        return mDisplayDaysName;
    }

    public void setDisplayDaysName(boolean displayDaysName){
        mDisplayDaysName = displayDaysName;
        invalidate();
        requestLayout();
    }

    public Date getSelectedDate(){
        return mSelectedDate;
    }

    public Map<Long,DonantesCalendarEvent> getEvents(){
        return mEvents;
    }

    public void setEvents(TreeMap<Long,DonantesCalendarEvent> events){
        mEvents=events;
        initEventsAssociated();
    }

    public void addEvent(DonantesCalendarEvent event){
        if(mEvents==null){
            mEvents = new TreeMap<>();
        }
        if(event.getDate()!=null) {
            mEvents.put(event.getDate().getTime(), event);
            initEventsAssociated();
        }
    }

    public interface OnSelectedDateChangeListener{
        void OnSelectedDateChange(Date selectedDate, DonantesCalendarEvent event);
    }
}
