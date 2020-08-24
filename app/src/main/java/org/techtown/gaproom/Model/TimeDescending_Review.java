package org.techtown.gaproom.Model;

import java.util.Comparator;

public class TimeDescending_Review implements Comparator<ReviewItem> {
    @Override
    public int compare(ReviewItem reviewItem, ReviewItem t1) {
        return t1.getCurrentTime() <= reviewItem.getCurrentTime() ? -1 : 1;
    }
}
