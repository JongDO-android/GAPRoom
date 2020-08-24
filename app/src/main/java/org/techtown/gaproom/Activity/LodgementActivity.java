package org.techtown.gaproom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import org.techtown.gaproom.Adapter.ViewPagerAdapter;
import org.techtown.gaproom.R;

public class LodgementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodgement);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("홈");
        tabLayout.getTabAt(1).setText("리뷰");
        tabLayout.getTabAt(3).setText("마이페이지");
    }
}
