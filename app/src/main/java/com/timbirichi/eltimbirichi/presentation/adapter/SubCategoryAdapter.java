package com.timbirichi.eltimbirichi.presentation.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.SubCategory;

import java.util.List;

public class SubCategoryAdapter extends BaseExpandableListAdapter {
    Context context;
    List<Category> categories;

    TypedArray menuIcons;

    public SubCategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;

        menuIcons = context.getResources().obtainTypedArray(R.array.dynamic_categories_menu);
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categories.get(groupPosition).getSubCategories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSubCategories().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = ((Category) getGroup(groupPosition)).getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_category_header, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);


    ImageView ivCat = convertView.findViewById(R.id.iv_cat);
    ivCat.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_categories_prod));

    ivCat.setImageDrawable(menuIcons.getDrawable((int) categories.get(groupPosition).getId() - 1));


    listTitleTextView.setTypeface(null, Typeface.BOLD);
    listTitleTextView.setText(listTitle);
    return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = ((SubCategory) getChild(groupPosition, childPosition)).getName();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_list_category, null);
        }

        TextView expandedListTextView =  convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
