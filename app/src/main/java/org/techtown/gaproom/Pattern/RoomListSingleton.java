package org.techtown.gaproom.Pattern;

import org.techtown.gaproom.Model.MarkerItem;

import java.util.ArrayList;

public class RoomListSingleton {
    private static RoomListSingleton ourInstance;
    private ArrayList<MarkerItem> markerItems;

    public static RoomListSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new RoomListSingleton();
        }
        return ourInstance;
    }
    public void initializeMarkerItems(){
        markerItems = new ArrayList<>();
    }

    public void addMarkerItem(MarkerItem markerItem){
        markerItems.add(markerItem);
    }
    public void clearMarkerItem(){
        markerItems.clear();
    }
    public int getLength(){
        return markerItems.size();
    }

    public MarkerItem getMarkerItem(int i){
        if(markerItems != null){
            return markerItems.get(i);
        }
        return null;
    }
}
