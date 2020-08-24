package org.techtown.gaproom.Pattern;

public class PostSingleton {
    private Long commentSize;
    private static PostSingleton ourInstance;

    public static PostSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new PostSingleton();
        }
        return ourInstance;
    }

    public void setCommentSize(Long commentSize){
        this.commentSize = commentSize;
    }
    public void addSize(){
        commentSize++;
    }

    public Long getCommentSize(){
        return commentSize;
    }
}
