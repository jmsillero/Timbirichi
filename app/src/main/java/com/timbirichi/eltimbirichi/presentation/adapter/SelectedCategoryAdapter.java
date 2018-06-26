package com.timbirichi.eltimbirichi.presentation.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedCategoryAdapter extends RecyclerView.Adapter<SelectedCategoryAdapter.SelectedProductViewHolder> {

    Context context;
    List<SubCategory> categories;

    public SelectedCategoryAdapter(Context context, List<SubCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public SelectedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.item_selected_category, parent, false);

        SelectedProductViewHolder holder = new SelectedProductViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedProductViewHolder holder, int position) {
        SubCategory category = categories.get(position);
        holder.setValues(category.getImage(), category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public final class SelectedProductViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_image)
        ImageView ivImage;

        @BindView(R.id.tv_category)
        TextView tvDescription;


        public SelectedProductViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


        public void setValues(byte [] image, String description){

            if(image != null){
                GlideApp.with(context)
                        .load(image)
                        .override(300, 300)
                        .centerCrop()
                        .into(ivImage);
            } else {
                ivImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher_round));
            }


            tvDescription.setText(description);
        }




    }
}
