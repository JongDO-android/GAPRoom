package org.techtown.gaproom.Model;

public class Room {
    private String address;

    private String roomStyle;
    private String start_day;
    private String end_day;
    private String room_form;
    private String floor;
    private String width;
    private Long time;

    private String price;
    private String description;


    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }

    public void setRoomStyle(String roomStyle){
        this.roomStyle = roomStyle;
    }
    public String getRoomStyle(){
        return roomStyle;
    }

    public void setStart_day(String start_day){
        this.start_day = start_day;
    }
    public String getStart_day(){
        return start_day;
    }

    public void setEnd_day(String end_day){
        this.end_day = end_day;
    }
    public String getEnd_day(){
        return end_day;
    }

    public String getRoom_form() {
        return room_form;
    }

    public void setRoom_form(String room_form) {
        this.room_form = room_form;
    }

    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getWidth() {
        return width;
    }
    public void setWidth(String width) {
        this.width = width;
    }


    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
