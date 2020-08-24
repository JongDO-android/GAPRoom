package org.techtown.gaproom.Pattern;


public class DataSingleton {
    private static DataSingleton ourInstance;
    private String nickname;
    private String gender;

    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getNickname(){
        return nickname;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return gender;
    }


    public static DataSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new DataSingleton();
        }
        return ourInstance;
    }

}
