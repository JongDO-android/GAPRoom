package org.techtown.gaproom.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.gaproom.Activity.AddPostActivity;
import org.techtown.gaproom.Adapter.PostAdapter;
import org.techtown.gaproom.Dialog.PostDialog;
import org.techtown.gaproom.Model.PostItem;
import org.techtown.gaproom.Model.TimeDescending;
import org.techtown.gaproom.Model.TimeString;
import org.techtown.gaproom.Pattern.PostSingleton;
import org.techtown.gaproom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class sublet_Fragment_Board extends Fragment {
    /*게시판*/
    private PostAdapter postAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostItem> postItems = new ArrayList<>();

    /*파이어베이스*/
    private DatabaseReference mRef;
    private DatabaseReference commentReference;
    private ChildEventListener childEventListener;
    private Long commentNumber;
    private Long likeNumber;
    private String title;

    public sublet_Fragment_Board(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sublet_board, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postAdapter = new PostAdapter(getActivity(), postItems);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(postAdapter);

        initDatabase();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postAdapter.clear();
                for(DataSnapshot postData : dataSnapshot.getChildren()){
                    title = (String) postData.child("title").getValue();
                    String content = (String) postData.child("content").getValue();
                    String time = (String) postData.child("time").getValue();
                    commentNumber = (Long) postData.child("commentNumber").getValue();
                    likeNumber = (Long) postData.child("likeNumber").getValue();
                    Long Current = (Long) postData.child("realTime").getValue();

                    PostItem postItem = new PostItem(title, content);
                    postItem.setCurrentT(Current);
                    postItem.setTime(time);
                    postItem.setCommentSize(commentNumber);
                    postItem.setLike_number(likeNumber);

                    postAdapter.addPostItem(postItem);
                    postItems.sort(new TimeDescending());
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageButton write_post_btn = v.findViewById(R.id.post_btn);
        write_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
    private void initDatabase() {
        mRef = FirebaseDatabase.getInstance().getReference("/Posts/");
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
}
