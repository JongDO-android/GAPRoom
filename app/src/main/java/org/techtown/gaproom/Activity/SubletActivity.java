package org.techtown.gaproom.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.Pattern.DataSingleton;
import org.techtown.gaproom.R;
import org.techtown.gaproom.Adapter.ViewPagerAdapter;

import java.util.ArrayList;

public class SubletActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            initView();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(context,
                    "권한 허용을 하지 않으면 서비스를 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublet);

        context = this.getBaseContext();
        checkPermissions();
        setActionBar();

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(context)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("앱을 이용하기 위해서는 위치 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                            //android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            //android.Manifest.permission.WRITE_EXTERNAL_STORAGE // 기기, 사진, 미디어, 파일 엑세스 권한
                    })
                    .check();

        } else {
            initView();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

    }

    private void initView(){
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        ViewPager viewPager;
        ViewPagerAdapter pagerAdapter;

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tablayout, null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout2, null);
        View v3 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout3, null);

        tabLayout.getTabAt(0).setCustomView(v);
        tabLayout.getTabAt(1).setCustomView(v2);
        tabLayout.getTabAt(2).setCustomView(v3);
        final String nickname = getIntent().getStringExtra("nickname");
        final String gender = getIntent().getStringExtra("gender");
        DataSingleton.getInstance().setNickname(nickname);
        DataSingleton.getInstance().setGender(gender);

    }

    private void setActionBar(){
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }

}
