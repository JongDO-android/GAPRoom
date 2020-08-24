package org.techtown.gaproom.Model;

import java.util.Comparator;

public class TimeDescending_Comment implements Comparator<Comment> {
    @Override
    public int compare(Comment comment, Comment t1) {
        return t1.getCurrenttime() <= comment.getCurrenttime() ? -1 : 1;
    }
}
