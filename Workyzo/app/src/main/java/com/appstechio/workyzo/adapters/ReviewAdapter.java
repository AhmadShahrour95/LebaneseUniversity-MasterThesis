package com.appstechio.workyzo.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.appstechio.workyzo.R;
import com.appstechio.workyzo.databinding.ReviewsitemsRcvBinding;
import com.appstechio.workyzo.utilities.Constants;
import com.appstechio.workyzo.utilities.PreferenceManager;
import com.google.android.material.transition.Hold;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {


    private ArrayList<HashMap> Review_List = new ArrayList<HashMap>();

    private PreferenceManager preferenceManager;

    public ReviewAdapter(ArrayList<HashMap> review_List) {
        Review_List = review_List;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(ReviewsitemsRcvBinding.inflate(LayoutInflater.from(parent.getContext()),parent
                ,false));
    }



    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

       // holder.reviewsitemsRcvBinding.ProfilepicReview.setImageBitmap(Review_List.get(position).get(Constants.KEY_REVIEW_USERNAME).toString());
        holder.reviewsitemsRcvBinding.ByUsernameTxt.setText(Review_List.get(position).get(Constants.KEY_REVIEW_USERNAME).toString());
        String rate = Review_List.get(position).get(Constants.KEY_REVIEW_RATING).toString();
        float f=Float.parseFloat(rate);
        holder.reviewsitemsRcvBinding.ratingBar.setRating(f);
        holder.reviewsitemsRcvBinding.RatingvalueTxt.setText(new StringBuilder().append(f).append(" out of 5").toString());
        holder.reviewsitemsRcvBinding.ReviewSummarytxt.setText(Review_List.get(position).get(Constants.KEY_REVIEW_CONTENT).toString());

        if (Review_List.get(position).get(Constants.KEY_REVIEW_PROFILE_PIC).toString() == null) {
            holder.reviewsitemsRcvBinding.ProfilepicReview.setImageResource(R.drawable.avatar_man);
        } else {
            //CONVERT STRING BASE 64 TO BITMAP
            byte[] decodedString = Base64.decode(Review_List.get(position).get(Constants.KEY_REVIEW_PROFILE_PIC).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.reviewsitemsRcvBinding.ProfilepicReview.setImageBitmap(decodedByte);
        }

    }



    @Override
    public int getItemCount() {
        return Review_List.size();
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder{

        private final ReviewsitemsRcvBinding reviewsitemsRcvBinding;


        public ReviewViewHolder(ReviewsitemsRcvBinding reviewsitemsRcvBinding1) {
            super(reviewsitemsRcvBinding1.getRoot());
            this.reviewsitemsRcvBinding = reviewsitemsRcvBinding1;
        }



    }
}
