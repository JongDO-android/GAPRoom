package org.techtown.gaproom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.Model.CustomActionBar_main;
import org.techtown.gaproom.R;

import java.util.Objects;

public class RoomItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_item);

        setActionBar();
        ImageView image_room = findViewById(R.id.image_room);
        ImageView image_gender = findViewById(R.id.image_gender);

        TextView text_price = findViewById(R.id.text_price);
        TextView text_phone = findViewById(R.id.text_phone);
        TextView text_address = findViewById(R.id.text_address);
        TextView text_gender = findViewById(R.id.text_gender);
        TextView text_room_form = findViewById(R.id.text_room_form);
        TextView text_room_floor = findViewById(R.id.text_room_floor);
        TextView text_room_width = findViewById(R.id.text_room_width);
        TextView text_start = findViewById(R.id.text_start_date);
        TextView text_end = findViewById(R.id.text_end_date);

        TextView text_description = findViewById(R.id.text_description);


        if(getIntent() != null){
            String address;
            address = getIntent().getStringExtra("address");
            text_address.setText(address);
            text_price.setText(getIntent().getStringExtra("price"));

            text_gender.setText(getIntent().getStringExtra("gender"));
            if(text_gender.getText().equals("남자 거주")){
                image_gender.setColorFilter(R.color.colorSkyBlue);
            }
            else{
                image_gender.setColorFilter(R.color.colorLightPink);
            }

            text_room_form.setText(getIntent().getStringExtra("form"));
            text_room_floor.setText(getIntent().getStringExtra("floor"));
            text_room_width.setText(getIntent().getStringExtra("width"));
            text_start.setText(getIntent().getStringExtra("period_start"));
            text_end.setText(getIntent().getStringExtra("period_end"));

            text_description.setText(getIntent().getStringExtra("description"));
            text_phone.setText(getIntent().getStringExtra("phone"));

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference ref = firebaseDatabase.getReference().child("/added_room_list/");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(Objects.equals(data.child("Room").child("address").getValue(), address)){
                            text_phone.setText(Objects.requireNonNull(data.child("phone_number").getValue()).toString());
                            text_description.setText(Objects.requireNonNull(data.child("Room").child("description").getValue()).toString());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference sRef = storage.getReference().child("image/"+text_address.getText()+".png");
        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(image_room);
            }
        });


        Button btn_call = findViewById(R.id.btn_call);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = text_phone.getText().toString();
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                startActivity(tel);
            }
        });

    }
    private void setActionBar(){
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }
}
