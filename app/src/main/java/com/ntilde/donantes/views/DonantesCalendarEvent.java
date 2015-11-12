package com.ntilde.donantes.views;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DonantesCalendarEvent implements Serializable{

    private Object mEventInfo;
    private Date mDate;
    private int mColor;
    private HashMap<Integer, Integer> mRange;

//    public static final Parcelable.Creator<DonantesCalendarEvent> CREATOR = new Parcelable.Creator<DonantesCalendarEvent>()
//    {
//        @Override
//        public DonantesCalendarEvent createFromParcel(Parcel source)
//        {
//            return new DonantesCalendarEvent(source);
//        }
//
//        @Override
//        public DonantesCalendarEvent[] newArray(int size)
//        {
//            return new DonantesCalendarEvent[size];
//        }
//    };

//    public DonantesCalendarEvent(Parcel in){
//        mEventInfo = in.readSerializable();
//        mDate = (Date)in.readSerializable();
//        mColor = in.readInt();
//        mRange = (HashMap<Integer, Integer>)in.readSerializable();
//    }
//
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeSerializable((Serializable)mEventInfo);
//        dest.writeSerializable(mDate);
//        dest.writeInt(mColor);
//        dest.writeSerializable(mRange);
//    }

    public DonantesCalendarEvent(Object eventInfo, Date date, int color){
        mEventInfo = eventInfo;
        mDate = date;
        mColor = color;
    }

    public DonantesCalendarEvent(Object eventInfo, Date date, int color, int... ranges){
        mEventInfo = eventInfo;
        mDate = date;
        mColor = color;
        mRange=new HashMap<>();
        for(int i=0;i<ranges.length-ranges.length%2;i+=2){
            mRange.put(ranges[i], ranges[i + 1]);
        }
    }

    public DonantesCalendarEvent(Object eventInfo, int year, int month, int day, int color){
        mEventInfo = eventInfo;
        mDate = new Date();
        Calendar c= Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        mDate.setTime(c.getTimeInMillis());
        mColor = color;
        //SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //Log.e("XXX", "Fecha: " + format1.format(mDate));
    }

    public DonantesCalendarEvent(Object eventInfo, int year, int month, int day, int color, int... ranges){
        mEventInfo = eventInfo;
        mDate = new Date();
        Calendar c= Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        mDate.setTime(c.getTimeInMillis());
        mColor = color;
        //SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        //Log.e("XXX", "Fecha: " + format1.format(mDate));
        mRange=new HashMap<>();
        for(int i=0;i<ranges.length-ranges.length%2;i+=2){
            mRange.put(ranges[i], ranges[i + 1]);
            //Log.e("XXX","Rango: color="+ranges[i]+" dias:"+ranges[i+1]);
        }
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public Map<Integer, Integer> getRanges() {
        return mRange;
    }

    public void setRanges(HashMap<Integer, Integer> ranges) {
        this.mRange = ranges;
    }

    public void addRange(Integer color, Integer days){
        if(mRange ==null){
            mRange =new HashMap<>();
        }
        mRange.put(color, days);
    }

    public Object getEventInfo() {
        return mEventInfo;
    }

    public void setEventInfo(Object eventInfo) {
        mEventInfo = eventInfo;
    }
}
