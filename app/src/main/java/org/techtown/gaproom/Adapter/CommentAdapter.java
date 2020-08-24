package org.techtown.gaproom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.gaproom.Model.Comment;
import org.techtown.gaproom.Model.TimeDescending_Comment;
import org.techtown.gaproom.Model.TimeString;
import org.techtown.gaproom.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> comments;
    private Context context;

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        this.context = context;
        this.comments = comments;
    }
    public void addComment(Comment comment){
        comments.add(comment);
        comments.sort(new TimeDescending_Comment());
        notifyDataSetChanged();
    }
    public void clear(){
        comments.clear();
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        TextView text_comment = holder.text_comment;
        TextView text_date = holder.text_date;
        ImageView profile = holder.imageView;

        profile.setImageResource(R.drawable.profile);
        text_comment.setText(comments.get(position).getComment());
        text_date.setText(TimeString.formatTimeString(comments.get(position).getCurrenttime()));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView text_comment;
        TextView text_date;
        ImageView imageView;
         private CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile);
            text_comment = itemView.findViewById(R.id.comment);
            text_date = itemView.findViewById(R.id.text_date);
        }
    }
}
