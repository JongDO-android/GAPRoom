package org.techtown.gaproom.Model;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Comment {
    private String comment;
    private String time;
    private String realtime;
    private Long currenttime;

    public Comment(){

    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return comment;
    }

    public void setCurrentTime(){
        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat mFormat2 = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);

        currenttime = now;
        time = mFormat2.format(date);
    }
    public void setTime(String time){
        this.time = time;
    }


    public void setRealtime(String realtime) {
        this.realtime = realtime;
    }

    public Long getCurrenttime() {
        return currenttime;
    }
}
