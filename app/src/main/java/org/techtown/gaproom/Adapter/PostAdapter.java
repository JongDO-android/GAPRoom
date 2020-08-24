package org.techtown.gaproom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.gaproom.Activity.PostActivity;
import org.techtown.gaproom.Model.PostItem;
import org.techtown.gaproom.Model.TimeString;
import org.techtown.gaproom.Pattern.PostSingleton;
import org.techtown.gaproom.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<PostItem> postItems;
    private Context context;

    public PostAdapter(Context context, ArrayList<PostItem> postItems){
        this.context = context;
        this.postItems = postItems;
    }
    public void addPostItem(PostItem postItem){
        postItems.add(postItem);
    }

    public void clear(){
        postItems.clear();
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        TextView text_title = holder.text_title;
        TextView text_content = holder.text_content;
        TextView text_date = holder.text_date;
        TextView text_comment_size = holder.text_comment_size;
        TextView text_like_number = holder.text_like_number;

        text_title.setText(postItems.get(position).getTitle());
        text_content.setText(postItems.get(position).getContent());
        text_comment_size.setText(String.valueOf(postItems.get(position).getCommentSize()));
        text_date.setText(TimeString.formatTimeString(postItems.get(position).getCurrentT()));
        text_like_number.setText(String.valueOf(postItems.get(position).getLike_number()));

    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView text_title;
        private TextView text_content;
        private TextView text_date;
        private TextView text_comment_size;
        private TextView text_like_number;

        private PostViewHolder(@NonNull View itemView) {
            super(itemView);

            text_title = itemView.findViewById(R.id.text_title);
            text_content = itemView.findViewById(R.id.text_content);
            text_date = itemView.findViewById(R.id.text_date);
            text_comment_size = itemView.findViewById(R.id.text_comment_size);
            text_like_number = itemView.findViewById(R.id.text_like_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("title", text_title.getText());
                    intent.putExtra("content", text_content.getText());
                    intent.putExtra("date", text_date.getText());
                    intent.putExtra("commentNumber", text_comment_size.getText());
                    intent.putExtra("likeNumber", text_like_number.getText());

                    context.startActivity(intent);
                }
            });
        }
    }
}
