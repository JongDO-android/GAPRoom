package org.techtown.gaproom.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.Model.CustomActionBar_main;
import org.techtown.gaproom.Model.MarkerItem;
import org.techtown.gaproom.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String nickname, gender;
    Context context = this;

    private ArrayList<MarkerItem> markerItems = new ArrayList<>();
    private static int MARKER_LIST = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        gender = intent.getStringExtra("gender");
        setActionBar();

        Button LogOut_btn = findViewById(R.id.logout);
        LogOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        startActivityForResult(intent, MARKER_LIST);
                    }
                });
            }
        });

        Button sublet_btn = findViewById(R.id.sublet);
        sublet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubletActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });

        Button lodgement_btn = findViewById(R.id.lodgement);
        lodgement_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LodgementActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });
    }
    private void setActionBar(){
        CustomActionBar_main ca = new CustomActionBar_main(this, getSupportActionBar());
        ca.setActionBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MARKER_LIST){
            if(resultCode == RESULT_OK){
                data.getParcelableArrayListExtra("");
            }
        }
    }
}
