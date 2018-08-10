package com.timbirichi.eltimbirichi.presentation.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

    @NonNull
    SelectedCategoryCallback selectedCategoryCallback;

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
        final SubCategory category = categories.get(position);
        holder.setValues(category.getBase64Img(), category.getName());

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryCallback.onCategoryClick(category);
            }
        });
    }

    public void setSelectedCategoryCallback(@NonNull SelectedCategoryCallback selectedCategoryCallback) {
        this.selectedCategoryCallback = selectedCategoryCallback;
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

        @BindView(R.id.cv_main)
        public CardView cvMain;


        public SelectedProductViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


        public void setValues(String base64Img, String description){

            if(base64Img != null){
                GlideApp.with(context)
                        .load(Base64.decode(base64Img, Base64.DEFAULT))
                        .override(1000, 406)
                        .fitCenter()
                        .centerCrop()
                        .into(ivImage);
            } else {
                ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.no_imagen));
            }


            tvDescription.setText(description);
        }
    }

    public interface SelectedCategoryCallback{
        void onCategoryClick(SubCategory cat);
    }
}
