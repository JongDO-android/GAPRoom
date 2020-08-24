package org.techtown.gaproom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.techtown.gaproom.Activity.RoomItemActivity;
import org.techtown.gaproom.Model.ReviewItem;
import org.techtown.gaproom.Model.TimeDescending_Review;
import org.techtown.gaproom.Model.TimeString;
import org.techtown.gaproom.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<ReviewItem> reviewItems;
    private Context context;

    public ReviewAdapter(Context context, ArrayList<ReviewItem> reviewItems){
        this.context = context;
        this.reviewItems = reviewItems;
    }
    public void addItem(ReviewItem reviewItem){
        reviewItems.add(reviewItem);
        reviewItems.sort(new TimeDescending_Review());
        notifyDataSetChanged();
    }
    public void clear(){
        reviewItems.clear();
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_list, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        TextView text_gender = holder.text_gender;
        TextView text_address = holder.text_address;
        TextView period_start = holder.period_start;
        TextView period_end = holder.period_end;
        TextView text_room_form = holder.text_room_form;
        TextView text_room_floor = holder.text_floor;
        TextView text_room_width = holder.text_width;
        TextView text_price = holder.text_price;
        TextView text_time = holder.text_time;


        ImageView image_gender = holder.image_gender;
        ImageView image = holder.imageView;

        int color_male = ContextCompat.getColor(context, R.color.colorSkyBlue);
        int color_female = ContextCompat.getColor(context, R.color.colorLightPink);
        if(reviewItems.get(position).getGender().equals("male")) {
            text_gender.setText("남자 거주");
            image_gender.setBackgroundColor(context.getResources().getColor(R.color.colorSkyBlue));
        }
        else{
            text_gender.setText("여자 거주");
            image_gender.setColorFilter(context.getResources().getColor(R.color.colorSkyBlue));
        }

        period_start.setText(reviewItems.get(position).getPeriod_start());
        period_end.setText(reviewItems.get(position).getPeriod_end());
        text_address.setText(reviewItems.get(position).getAddress());
        text_room_form.setText(reviewItems.get(position).getForm());
        text_room_floor.setText(reviewItems.get(position).getFloor() +  " 층");
        text_room_width.setText(reviewItems.get(position).getWidth() + " 평");

        int price = Integer.parseInt(reviewItems.get(position).getPrice());
        int thousand = (price%10000)/1000;
        int million = price/10000;
        if(thousand == 0){
            text_price.setText(million + "만");
        }
        else if(million == 0){
            text_price.setText(thousand + "천");
        }
        else{
            text_price.setText(million + "만" + " " + thousand + "천");
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference sRef = storage.getReference().child("image/"+text_address.getText()+".png");

        image.setClipToOutline(true);
        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(image);
            }
        });

        text_time.setText(TimeString.formatTimeString(reviewItems.get(position).getCurrentTime()));

    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }
    public ReviewItem getReviewItem(int index){
        return reviewItems.get(index);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView text_gender;
        private TextView text_address;
        private TextView period_start;
        private TextView period_end;
        private TextView text_room_form;
        private TextView text_floor;
        private TextView text_width;
        private TextView text_price;
        private TextView text_time;

        private ImageView image_gender;
        private ImageView imageView;


        private ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            text_gender = itemView.findViewById(R.id.text_gender);
            text_address = itemView.findViewById(R.id.text_address);
            period_start = itemView.findViewById(R.id.period_start);
            period_end = itemView.findViewById(R.id.period_after);
            image_gender = itemView.findViewById(R.id.image_gender);
            text_room_form = itemView.findViewById(R.id.room_form);
            text_floor = itemView.findViewById(R.id.text_room_floor);
            text_width = itemView.findViewById(R.id.text_room_width);
            text_price = itemView.findViewById(R.id.price);

            text_time = itemView.findViewById(R.id.text_time);

            imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RoomItemActivity.class);
                    intent.putExtra("price", text_price.getText());
                    intent.putExtra("address", text_address.getText());
                    intent.putExtra("gender", text_gender.getText());
                    intent.putExtra("form", text_room_form.getText());
                    intent.putExtra("floor", text_floor.getText());
                    intent.putExtra("width", text_width.getText());
                    intent.putExtra("period_start", period_start.getText());
                    intent.putExtra("period_end", period_end.getText());

                    context.startActivity(intent);
                }
            });
        }
    }
}
