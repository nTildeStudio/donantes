package com.ntilde.app.views.calendar;

import java.io.Serializable;
import java.util.ArrayList;

public class DonantesCalendarEvent implements Serializable{

    private Object mEventInfo;
    private DonantesCalendarRange mEvent;
    private ArrayList<DonantesCalendarRange> mRange;

    public DonantesCalendarEvent(Object eventInfo, DonantesCalendarRange event){
        this(eventInfo, event, new DonantesCalendarRange[]{});
    }

    public DonantesCalendarEvent(Object eventInfo, DonantesCalendarRange event, DonantesCalendarRange... ranges){
        mEventInfo = eventInfo;
        mEvent = event;
        mRange=new ArrayList<>();
        if(ranges != null) {
            for (DonantesCalendarRange range : ranges) {
                mRange.add(range);
            }
        }
    }

    public DonantesCalendarRange getEvent() {
        return mEvent;
    }

    public void setEvent(DonantesCalendarRange event) {
        mEvent = event;
    }

    public ArrayList<DonantesCalendarRange> getRanges() {
        return mRange;
    }

    public void setRanges(ArrayList<DonantesCalendarRange> ranges) {
        mRange = ranges;
    }

    public void addRange(DonantesCalendarRange range){
        if(mRange ==null){
            mRange =new ArrayList<>();
        }
        mRange.add(range);
    }

    public Object getEventInfo() {
        return mEventInfo;
    }

    public void setEventInfo(Object eventInfo) {
        mEventInfo = eventInfo;
    }
}
