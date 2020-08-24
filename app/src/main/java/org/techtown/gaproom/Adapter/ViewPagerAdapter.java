package org.techtown.gaproom.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.techtown.gaproom.Fragment.sub_Fragment_recommendation;
import org.techtown.gaproom.Fragment.sublet_Fragment_Home;
import org.techtown.gaproom.Fragment.sublet_Fragment_Board;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        items.add(new sublet_Fragment_Home());
        items.add(new sublet_Fragment_Board());
        items.add(new sub_Fragment_recommendation());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

}
