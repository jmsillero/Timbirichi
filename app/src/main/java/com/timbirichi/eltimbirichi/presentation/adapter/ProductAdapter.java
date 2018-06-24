package com.timbirichi.eltimbirichi.presentation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    Context context;
    List<Product> products;

    @NonNull
    ProductCallback productCallback;
    int lastPosition = 0;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void addProducts(List<Product> products){
        this.products.addAll(products);
        notifyItemChanged(this.products.size() - products.size());
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        final Product prod = products.get(position);

        setAnimation(holder.itemView, position);

        holder.setValues(prod.getImages() != null ? prod.getImages().get(0).getImage() : null,
                prod.getTitle(),
                prod.getPrice(),
                prod.getViews(),
                prod.isNewProduct() ? ProductState.NEW : ProductState.NO_NEW,
                "CAMBIAR",
                prod.isFavorite());

        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productCallback.onItemClick(prod);
            }
        });
    }


    public void setAnimation(View viewToAnimate, int position){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProductCallback(ProductCallback productCallback) {
        this.productCallback = productCallback;
    }

    public final class ProductHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_product)
        ImageView ivProduct;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_price)
        TextView tvPrice;

        @BindView(R.id.tv_views)
        TextView tvViews;

        @BindView(R.id.tv_state)
        TextView tvState;

        @BindView(R.id.tv_province)
        TextView tvProvince;

        @BindView(R.id.btn_favorite)
        ImageButton btnFavorite;

        @BindView(R.id.main_card)
        public CardView cvMain;



        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValues(byte [] image, String title, double price,
                              int views, ProductState state, String province, boolean favorite){

                if (image == null){
                    ivProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.no_imagen));
                } else{
                    GlideApp.with(context)
                            .load(image)
                            .override(60, 60)
                            .centerInside()
                            .into(ivProduct);
                }

                tvTitle.setText(title);
                tvPrice.setText(Double.toString(price));
                tvViews.setText(Integer.toString(views));
                tvState.setText(state == ProductState.NEW ? context.getString(R.string.new_product) : context.getString(R.string.used_product)   );
                tvProvince.setText(province);
                btnFavorite.setImageDrawable(favorite ? context.getResources().getDrawable(R.drawable.ic_favorites_full) : context.getResources().getDrawable(R.drawable.ic_favorites_empty));


        }
    }

    public interface ProductCallback{
        void onItemClick(Product prod);
    }
}
