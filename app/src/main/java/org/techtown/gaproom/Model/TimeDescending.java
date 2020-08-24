package org.techtown.gaproom.Model;

import java.util.Comparator;

public class TimeDescending implements Comparator<PostItem> {
    @Override
    public int compare(PostItem item, PostItem t1) {
        return t1.getCurrentT() <= item.getCurrentT() ? -1 : 1;
    }
}
