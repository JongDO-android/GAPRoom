package org.techtown.gaproom.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.Model.CustomActionBar_main;
import org.techtown.gaproom.Model.Room;
import org.techtown.gaproom.Model.User;
import org.techtown.gaproom.Pattern.DataSingleton;
import org.techtown.gaproom.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddRoomActivity extends AppCompatActivity {
    private TextView text_address;
    private TextView text_Load;
    private static int REQUEST_CODE = 1;
    private static int ADDRESS_OK = 2;
    private static int PICK_IMAGE = 5;
    private TextView start_day;
    private TextView end_day;
    private ImageView imageView;

    private Uri filepath;

    private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        TextView text_room_form = findViewById(R.id.text_room_form);

        imageView = findViewById(R.id.add_image);
        EditText edit_price = findViewById(R.id.edit_price);
        EditText edit_floor = findViewById(R.id.edit_floor);
        EditText edit_width = findViewById(R.id.edit_width);
        EditText edit_description = findViewById(R.id.edit_description);
        Spinner spinner = findViewById(R.id.spinner);

        setActionBar();

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        View v = LayoutInflater.from(this).inflate(R.layout.custom_tablayout, null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout2, null);
        View v3 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout3, null);


        tabLayout.getTabAt(0).setCustomView(v);
        tabLayout.getTabAt(1).setCustomView(v2);
        tabLayout.getTabAt(2).setCustomView(v3);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PICK_IMAGE);

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                text_room_form.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        text_address = findViewById(R.id.text_address);
        text_Load = findViewById(R.id.jibun);
        mRef = FirebaseDatabase.getInstance().getReference();
        final EditText phonenum = findViewById(R.id.editText_phone);
        ImageButton start_btn = findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate_start();
            }
        });

        start_day = findViewById(R.id.start_day);
        end_day = findViewById(R.id.end_day);

        ImageButton end_btn = findViewById(R.id.end_btn);
        end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate_end();
            }
        });

        Button search_address_btn = findViewById(R.id.search_btn);
        search_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRoomPop_Activity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        Button cancel_btn = findViewById(R.id.cancel_button);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(text_room_form.getText())){
                    Toast.makeText(getApplicationContext(), "주거 형태를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(edit_floor.getText())){
                    Toast.makeText(getApplicationContext(), "층수를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(edit_width.getText())){
                    Toast.makeText(getApplicationContext(), "평수를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phonenum.getText())){
                    Toast.makeText(getApplicationContext(), "연락처를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(text_address.getText())){
                    Toast.makeText(getApplicationContext(), "주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(start_day.getText()) || TextUtils.isEmpty(end_day.getText())){
                    Toast.makeText(getApplicationContext(), "임대 기간을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(edit_description.getText())){
                    Toast.makeText(getApplicationContext(), "집 소개를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(edit_price.getText())){
                    Toast.makeText(getApplicationContext(), "가격을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    Room room = new Room();
                    room.setAddress(text_address.getText().toString());
                    room.setStart_day(start_day.getText().toString());
                    room.setEnd_day(end_day.getText().toString());
                    room.setRoom_form(text_room_form.getText().toString());
                    room.setFloor(edit_floor.getText().toString());
                    room.setWidth(edit_width.getText().toString());
                    room.setDescription(edit_description.getText().toString());
                    room.setPrice(edit_price.getText().toString());
                    room.setTime(System.currentTimeMillis());


                    Map<String, Object> childUpdates = new HashMap<>();
                    Object postValue = null;

                    User user = new User(DataSingleton.getInstance().getNickname(),
                            DataSingleton.getInstance().getGender());
                    user.setRoom(room);
                    user.setPhone_number(phonenum.getText().toString());

                    postValue = user.toMap();
                    childUpdates.put("/added_room_list/" + text_address.getText().toString(), postValue);
                    mRef.updateChildren(childUpdates);
                    uploadFile(room.getAddress());
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == ADDRESS_OK){
                text_Load.setText(data.getStringExtra("result"));
                text_address.setText(data.getStringExtra("result2"));
            }
        }

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                filepath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);*/
            }
        }
    }


    private void showDate_start(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m = m+1;
                start_day.setText(y + "." + m + "." + d);
            }
        }, 2020, 1, 11);

        datePickerDialog.show();
    }
    private void showDate_end(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m = m+1;
                end_day.setText(y + "." + m + "." + d);
            }
        }, 2020, 1, 11);

        datePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private void uploadFile(String address){
        if(filepath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            String imagedata = address +".png";
            StorageReference sRef = firebaseStorage.getReferenceFromUrl("gs://gaproom-7365a.appspot.com/")
                    .child("image/" + imagedata);

            sRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "업로드 성공!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "방 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "업로드 실패!!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void setActionBar(){
        CustomActionBar_main ca = new CustomActionBar_main(this, getSupportActionBar());
        ca.setActionBar();
    }
}
