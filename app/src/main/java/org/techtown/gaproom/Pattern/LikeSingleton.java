package org.techtown.gaproom.Pattern;

public class LikeSingleton {
    private Long LikeNumber;
    private static LikeSingleton ourInstance;

    public static LikeSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new LikeSingleton();
        }
        return ourInstance;
    }

    public void setLikeNumber(Long commentSize){
        this.LikeNumber = commentSize;
    }
    public void addNumber(){
        LikeNumber++;
    }

    public Long getLikeNumber(){
        return LikeNumber;
    }
}
