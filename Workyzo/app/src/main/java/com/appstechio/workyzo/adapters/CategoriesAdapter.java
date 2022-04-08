package com.appstechio.workyzo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appstechio.workyzo.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

  Context context;
  private ArrayList<String> Category_titles = new ArrayList<>();
  int[] Category_icons;
  int Details_icon;
  private  OnCategoryClickListener categoryClickListener;


    public  CategoriesAdapter (Context c ,ArrayList<String > category_titles, int[] category_icons, int details_icon,OnCategoryClickListener onCategoryClickListener ){
        this.context = c;
        this.Category_titles = category_titles;
        this.Category_icons = category_icons;
        this.Details_icon = details_icon;
        this.categoryClickListener = onCategoryClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categoriesitems_rcv , parent, false);
        return new ViewHolder(view, categoryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Categorytitle.setText(Category_titles.get(position));
        holder.Categoryicon.setImageResource(Category_icons[position]);

    }

    @Override
    public int getItemCount() {
        return  Category_titles.size();
    }

    public  interface OnCategoryClickListener {
        void onCategoryClick (int position);

    }


    public static  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Categorytitle;
        ImageView Categoryicon;
        OnCategoryClickListener onCategoryClickListener;


        public ViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);

            Categorytitle = itemView.findViewById(R.id.Category_title);
            Categoryicon = itemView.findViewById(R.id.Category_icon);
            this.onCategoryClickListener = onCategoryClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onCategoryClickListener.onCategoryClick(getBindingAdapterPosition());
        }
    }
}
