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
import com.timbirichi.eltimbirichi.data.model.Product;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedProductAdapter  extends RecyclerView.Adapter<SelectedProductAdapter.SelectedProductViewHolder> {

    Context context;
    List<Product> products;

    public SelectedProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public SelectedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.item_cover_page_product, parent, false);

        SelectedProductViewHolder holder = new SelectedProductViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedProductViewHolder holder, int position) {
        Product product = products.get(position);

        byte [] image = null;
        if(product.getImages() != null && product.getImages().get(0) != null){
            image = product.getImages().get(0).getImage();
        }
        holder.setValues(image, product.getPrice(), product.getTitle());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public final class SelectedProductViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_image)
        ImageView ivImage;

        @BindView(R.id.tv_price)
        TextView tvPrice;

        @BindView(R.id.tv_description)
        TextView tvDescription;


        public SelectedProductViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


        public void setValues(byte [] image, double price, String description){

            if(image != null){
                GlideApp.with(context)
                        .load(image)
                        .centerCrop()
                        .override(300, 300)
                        .into(ivImage);
            } else{
                ivImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher_round));
            }


            tvPrice.setText(Double.toString(price));
            tvDescription.setText(description);
        }




    }
}
