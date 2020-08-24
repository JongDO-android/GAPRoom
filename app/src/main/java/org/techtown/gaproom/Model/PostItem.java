package org.techtown.gaproom.Model;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PostItem {
    private String title;
    private String content;
    private String time;
    private Long currentT;
    private Long commentSize;
    private Long like_number;

    public PostItem(String title, String content ) {
        this.title = title;
        this.content = content;
        commentSize = 0L;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }

    public void setCommentSize(Long commentSize){
        this.commentSize = commentSize;
    }
    public Long getCommentSize(){
        return commentSize;
    }
    public String getContent(){
        return content;
    }

    @Exclude
    public Object toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("content", content);
        result.put("time", time);
        result.put("commentNumber", commentSize);
        result.put("likeNumber", like_number);
        result.put("realTime", currentT);

        return result;
    }

    public Long getLike_number() {
        return like_number;
    }

    public void setLike_number(Long like_number) {
        this.like_number = like_number;
    }

    public Long getCurrentT() {
        return currentT;
    }

    public void setCurrentT(Long currentT) {
        this.currentT = currentT;
    }
}
