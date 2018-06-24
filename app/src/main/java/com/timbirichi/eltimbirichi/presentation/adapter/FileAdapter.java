package com.timbirichi.eltimbirichi.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.presentation.model.FileItem;
import com.timbirichi.eltimbirichi.presentation.view.activity.UpdateActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JM on 11/16/2017.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FilesViewHolder>{

    Context context;
    List<FileItem> files;
    OnFileItemClickListener onFileItemClickListener;




    public FileAdapter(Context context) {
        this.context = context;
        files = new ArrayList<>();
    }


    public void setFiles(List<FileItem> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    public FileItem getTopFile(){
        if (files.size() > 0){
            return files.get(0);
        }
        return null;
    }

    public void setOnFileItemClickListener(OnFileItemClickListener onFileItemClickListener) {
        this.onFileItemClickListener = onFileItemClickListener;
    }

    @Override
    public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);

        FilesViewHolder viewHolder = new FilesViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilesViewHolder holder, final int position) {
        final int type = files.get(position).getType();
        final String filename = files.get(position).getFilename();
        final String path = files.get(position).getPath();


        setAnimation(holder.itemView, position);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFileItemClickListener.onItemClick(type, position,filename, path);
            }
        });

        holder.fillUi(filename, type);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class FilesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_layout)
        LinearLayout mLayout;

        @BindView(R.id.img_file)
        ImageView mFileImg;

        @BindView(R.id.tv_file_name)
        TextView mTvFilename;

        public FilesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind( this, itemView);


        }

        public void fillUi(String filename, int type){
             mTvFilename.setText(filename);
             switch (type){
                 case UpdateActivity.GO_BACK:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_directory_up));
                     break;

                 case UpdateActivity.FILE:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_file_icon));
                     break;

                 case UpdateActivity.FOLDER:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_folder));
                     break;

                 case UpdateActivity.INTERNAL_STORAGE:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_phone));
                     break;

                 case UpdateActivity.EXTERNAL_STORAGE:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sd_storage));
                     break;
             }

        }
    }

    public void setAnimation(View viewToAnimate, int position){
       Animation animation = new ScaleAnimation(1f, 1f, .6f, 1f);
       viewToAnimate.startAnimation(animation);
    }

    public interface OnFileItemClickListener{
        void onItemClick(int fileType, int pos, String filename, String path);
    }
}
