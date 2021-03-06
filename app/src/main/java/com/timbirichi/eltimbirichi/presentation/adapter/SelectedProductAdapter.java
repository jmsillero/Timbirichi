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
import com.timbirichi.eltimbirichi.data.model.Product;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedProductAdapter  extends RecyclerView.Adapter<SelectedProductAdapter.SelectedProductViewHolder> {

    Context context;
    List<Product> products;

    @NonNull
    SelectProductAdapterCallback selectProductAdapterCallback;

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
        final Product product = products.get(position);

        String image = null;
        if(product.getImages() != null && product.getImages().get(0) != null){
            image = product.getImages().get(0).getBase64Img();
        }
        holder.setValues(image, product.getPrice(), product.getTitle());

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProductAdapterCallback.onProductClick(product);
            }
        });

    }

    public void setSelectProductAdapterCallback(@NonNull SelectProductAdapterCallback selectProductAdapterCallback) {
        this.selectProductAdapterCallback = selectProductAdapterCallback;
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

        @BindView(R.id.cv_main)
        public CardView cvMain;


        public SelectedProductViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }


        public void setValues(String base64Img, double price, String description){

            if(base64Img != null){
                GlideApp.with(context)
                        .load( Base64.decode(base64Img, Base64.DEFAULT))
                        .centerCrop()
                        .override(300, 300)
                        .into(ivImage);
            } else{
                ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.no_imagen));
            }


            tvPrice.setText(Double.toString(price));
            tvDescription.setText(description);
        }
    }

    public interface SelectProductAdapterCallback{
        void onProductClick(Product product);
    }
}
