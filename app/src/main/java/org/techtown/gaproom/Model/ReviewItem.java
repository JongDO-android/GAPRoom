package org.techtown.gaproom.Model;

public class ReviewItem {
    private String gender;
    private String address;
    private String period_start;
    private String period_end;
    private String form;
    private String floor;
    private String width;
    private String price;
    private String description;
    private String phone;
    
    private Long currentTime;

    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return gender;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


    public String getPeriod_end() {
        return period_end;
    }

    public void setPeriod_end(String period_end) {
        this.period_end = period_end;
    }

    public String getPeriod_start() {
        return period_start;
    }
    public void setPeriod_start(String period_start) {
        this.period_start = period_start;
    }

    public String getForm() {
        return form;
    }
    public void setForm(String form) {
        this.form = form;
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


    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }
}
