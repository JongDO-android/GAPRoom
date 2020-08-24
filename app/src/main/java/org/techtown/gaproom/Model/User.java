package org.techtown.gaproom.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Objects;

@IgnoreExtraProperties
public class User {
    private String nickname;
    private String gender;
    private Room room;
    private String phone_number;
    public User(){

    }
    public User(String nickname, String gender){
        this.nickname = nickname;
        this.gender = gender;
    }

    public String getNickname(){
        return nickname;
    }
    public String getGender(){
        return gender;
    }

    public void setRoom(Room room){
        this.room = room;
    }
    public Room getRoom(){
        return room;
    }

    public void setPhone_number(String phone_number){
        this.phone_number = phone_number;
    }
    public String getPhone_number(){
        return phone_number;
    }

    @Exclude
    public Object toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("nickname", nickname);
        result.put("gender", gender);
        result.put("Room", room);
        result.put("phone_number", phone_number);

        return result;
    }
}
