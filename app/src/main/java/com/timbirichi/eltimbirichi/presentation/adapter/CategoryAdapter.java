package com.timbirichi.eltimbirichi.presentation.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    List<Category> categories;
    Context context;

    TypedArray menuIcons;

    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        menuIcons = context.getResources().obtainTypedArray(R.array.dynamic_categories_menu);

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public final class CategoryViewHolder extends RecyclerView.ViewHolder{
        public CategoryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
