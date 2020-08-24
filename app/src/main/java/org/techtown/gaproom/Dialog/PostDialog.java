package org.techtown.gaproom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.gaproom.Model.Comment;
import org.techtown.gaproom.Model.PostItem;
import org.techtown.gaproom.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PostDialog {
    private Context context;
    private DatabaseReference mRef;


    public PostDialog(Context context){
        this.context = context;
    }

    public void callFunction(){
        Dialog dialog = new Dialog(context);

        mRef = FirebaseDatabase.getInstance().getReference();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.show();

        EditText edit_title = dialog.findViewById(R.id.edit_title);
        EditText edit_content = dialog.findViewById(R.id.edit_content);

        Button conform_btn = dialog.findViewById(R.id.OK_btn);
        conform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edit_title.getText()) || TextUtils.isEmpty(edit_content.getText())){
                    Toast.makeText(context, "입력하신 내용과 제목을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    PostItem postItem = new PostItem(edit_title.getText().toString(), edit_content.getText().toString());
                    long now = System.currentTimeMillis();

                    Date date = new Date(now);
                    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
                    String time = mFormat.format(date);

                    postItem.setTime(time);

                    Map<String, Object> childUpdates = new HashMap<>();
                    Object postValue = null;

                    postValue = postItem.toMap();
                    childUpdates.put("/Posts/" + postItem.getTitle(), postValue);
                    mRef.updateChildren(childUpdates);
                    dialog.dismiss();
                }
            }
        });
        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


}
