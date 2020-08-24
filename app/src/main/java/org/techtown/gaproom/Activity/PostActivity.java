package org.techtown.gaproom.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.gaproom.Adapter.CommentAdapter;
import org.techtown.gaproom.Model.Comment;
import org.techtown.gaproom.Model.CustomActionBar;
import org.techtown.gaproom.Model.CustomActionBar_main;
import org.techtown.gaproom.Pattern.LikeSingleton;
import org.techtown.gaproom.Pattern.PostSingleton;
import org.techtown.gaproom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    private DatabaseReference mRef;
    private ChildEventListener childEventListener;
    private DatabaseReference mCommentRef;
    private TextView text_like_number;
    private TextView text_comment_size;

    /*Comment List -Recycler View*/
    private RecyclerView commentView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comment_list = new ArrayList<>();
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        /*TabLayout tabLayout = findViewById(R.id.tab_layout);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_tablayout, null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout2, null);
        View v3 = LayoutInflater.from(this).inflate(R.layout.custom_tablayout3, null);


        tabLayout.getTabAt(0).setCustomView(v);
        tabLayout.getTabAt(1).setCustomView(v2).select();
        tabLayout.getTabAt(2).setCustomView(v3);*/



        commentView = findViewById(R.id.comment_recyclerView);
        initCommentList();
        setActionBar();
        initView();
        PostSingleton.getInstance().setCommentSize( Long.valueOf(Objects.requireNonNull(getIntent().getStringExtra("commentNumber"))));
        LikeSingleton.getInstance().setLikeNumber( Long.valueOf(Objects.requireNonNull(getIntent().getStringExtra("likeNumber"))));
        initDatabase();

        EditText edit_comment = findViewById(R.id.edit_review);
        ImageButton send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(edit_comment.getText())){
                    Comment comment = new Comment();

                    comment.setComment(edit_comment.getText().toString());
                    comment.setCurrentTime();

                    PostSingleton.getInstance().addSize();
                    mRef.child("commentNumber").setValue(PostSingleton.getInstance().getCommentSize());
                    mRef.child("comment").push().setValue(comment);

                    text_comment_size.setText(String.valueOf(PostSingleton.getInstance().getCommentSize()));

                    edit_comment.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),"댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRef.child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentAdapter.clear();
                for(DataSnapshot commentData : dataSnapshot.getChildren()){
                    Comment comment = commentData.getValue(Comment.class);

                    commentAdapter.addComment(comment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button add_like_btn = findViewById(R.id.add_like_btn);
        add_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeSingleton.getInstance().addNumber();

                text_like_number.setText(String.valueOf(LikeSingleton.getInstance().getLikeNumber()));
                mRef.child("likeNumber").setValue(LikeSingleton.getInstance().getLikeNumber());

            }
        });

    }
    public void initDatabase(){
        mRef = FirebaseDatabase.getInstance().getReference("/Posts/").child(title);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);
    }

    public void initCommentList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentView.setLayoutManager(linearLayoutManager);
        commentView.setHasFixedSize(true);
        commentAdapter = new CommentAdapter(this, comment_list);
        commentView.setAdapter(commentAdapter);
    }

    public void initView(){
        TextView text_title = findViewById(R.id.text_title);
        TextView text_content = findViewById(R.id.text_content);
        TextView text_time = findViewById(R.id.text_date);
        text_comment_size = findViewById(R.id.text_comment_size);
        text_like_number = findViewById(R.id.text_like_number);

        text_title.setText(getIntent().getStringExtra("title"));
        title = text_title.getText().toString();

        text_content.setText(getIntent().getStringExtra("content"));
        text_time.setText(getIntent().getStringExtra("date"));
        text_comment_size.setText(getIntent().getStringExtra("commentNumber"));
        text_like_number.setText(getIntent().getStringExtra("likeNumber"));

    }
    private void setActionBar(){
        CustomActionBar ca = new CustomActionBar(this, getSupportActionBar());
        ca.setActionBar();
    }
}
