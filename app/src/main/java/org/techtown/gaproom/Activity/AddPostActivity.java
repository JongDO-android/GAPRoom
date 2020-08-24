package org.techtown.gaproom.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.gaproom.Model.CustomActionBar_login;
import org.techtown.gaproom.Model.CustomActionBar_main;
import org.techtown.gaproom.Model.PostItem;
import org.techtown.gaproom.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mRef = FirebaseDatabase.getInstance().getReference();
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        View v = LayoutInflater.from(this).inflate(R.layout.custom_tablayout, null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout2, null);
        View v3 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout3, null);

        tabLayout.getTabAt(0).setCustomView(v);
        tabLayout.getTabAt(1).setCustomView(v2).select();
        tabLayout.getTabAt(2).setCustomView(v3);

        setActionBar();

        EditText edit_title = findViewById(R.id.edit_title);
        EditText edit_content = findViewById(R.id.edit_content);

        Button conform_btn = findViewById(R.id.OK_btn);
        conform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edit_title.getText()) || TextUtils.isEmpty(edit_content.getText())){
                    Toast.makeText(getApplicationContext(), "입력하신 내용과 제목을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    PostItem postItem = new PostItem(edit_title.getText().toString(), edit_content.getText().toString());
                    long now = System.currentTimeMillis();

                    Date date = new Date(now);
                    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
                    String time = mFormat.format(date);


                    postItem.setTime(time);
                    postItem.setCurrentT(now);
                    postItem.setLike_number(0L);

                    Map<String, Object> childUpdates = new HashMap<>();
                    Object postValue = null;

                    postValue = postItem.toMap();
                    childUpdates.put("/Posts/" + postItem.getTitle(), postValue);
                    mRef.updateChildren(childUpdates);
                    finish();
                }
            }
        });

        Button cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setActionBar(){
        CustomActionBar_main ca = new CustomActionBar_main(this, getSupportActionBar());
        ca.setActionBar();
    }
}
