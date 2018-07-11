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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.BaseProductViewHolder>{

    Context context;
    List<Product> products;
    boolean loadMore;

    public static final int PROGRESS_VIEW_TYPE = 2001;
    public static final int PRODUCT_VIEW_TYPE = 2002;

    @NonNull
    ProductCallback productCallback;
    int lastPosition = 0;

    public ProductAdapter(Context context, List<Product> products, boolean loadMore) {
        this.context = context;
        this.products = products;
        this.loadMore = loadMore;
    }

    public void addProducts(List<Product> products,  boolean loadMore){
        this.products.addAll(products);
        notifyItemChanged(this.products.size() - products.size());
        this.loadMore = loadMore;
    }

    public void removeAllProducts(){
        loadMore = false;
        this.products.clear();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public BaseProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = null;
        BaseProductViewHolder holder = null;
        if (viewType == PRODUCT_VIEW_TYPE){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);
            holder = new ProductHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_progress, parent, false);
            holder = new BaseProductViewHolder(v);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseProductViewHolder holder, int position) {
        if(holder instanceof ProductHolder){
            final Product prod = products.get(position);
          //  setAnimation(holder.itemView, position);

            ((ProductHolder)holder).setValues(prod.getImages() != null ? prod.getImages().get(0).getBase64Img() : null,
                    prod.getTitle(),
                    prod.getPrice(),
                    prod.getViews(),
                    prod.isNewProduct() ? ProductState.NEW : ProductState.NO_NEW,
                    prod.getProvince() != null ? prod.getProvince().getName() : "",
                    prod.isFavorite());

            ((ProductHolder)holder).cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productCallback.onItemClick(prod);
                }
            });
        }
    }


    public void setAnimation(View viewToAnimate, int position){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemViewType(int position) {
        if(products != null && position < products.size()){
            return PRODUCT_VIEW_TYPE;
        }
        return PROGRESS_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        if (products != null){
            return loadMore ? products.size() + 1   : products.size();
        }
        return 0;
    }

    public void setProductCallback(ProductCallback productCallback) {
        this.productCallback = productCallback;
    }


    public class BaseProductViewHolder extends RecyclerView.ViewHolder{
        public BaseProductViewHolder(View itemView) {
            super(itemView);
        }
    }


    public final class ProductHolder extends BaseProductViewHolder{
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

//        @BindView(R.id.btn_favorite)
//        ImageButton btnFavorite;

        @BindView(R.id.main_card)
        public CardView cvMain;



        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValues(String base64Img, String title, double price,
                              int views, ProductState state, String province, boolean favorite){

                if (base64Img == null){
                    ivProduct.setImageDrawable(context.getResources().getDrawable(R.drawable.no_imagen));
                } else{
                    GlideApp.with(context)
                            .load(Base64.decode(base64Img, Base64.DEFAULT))
                            .override(100, 100)
                            .centerInside()
                            .into(ivProduct);
                }

                tvTitle.setText(title);
                tvPrice.setText(Double.toString(price));
                tvViews.setText(Integer.toString(views));
                tvState.setText(state == ProductState.NEW ? context.getString(R.string.new_product) : context.getString(R.string.used_product)   );
                tvProvince.setText(province);
        }
    }


    public interface ProductCallback{
        void onItemClick(Product prod);
    }
}
