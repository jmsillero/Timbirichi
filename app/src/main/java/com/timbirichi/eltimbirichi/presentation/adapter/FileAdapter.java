package com.timbirichi.eltimbirichi.presentation.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
import com.timbirichi.eltimbirichi.data.model.Meta;
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


    public void setFileAt(Meta date, int pos){
        this.files.get(pos).setFilename(this.files.get(pos).getFilename() + "\nActualización: " + date.getStrDate());
        this.files.get(pos).setDateTime(date.getTimestamp());
      //  sortDatabaseFiles();
        notifyDataSetChanged();
    }

    private void sortDatabaseFiles(){
        int verif;
        do {
            verif = -1;
            for (int i = 0; i < files.size() - 1; i++){

                if (files.get(i).getDateTime() < files.get(i + 1).getDateTime()){
                    FileItem temp = files.get(i);
                    files.set(i, files.get(i + 1));
                    files.set( i + 1, temp);
                    verif ++;
                }
            }
        } while (verif != -1);
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

        final String filename;

       // if(type == UpdateActivity.FILE)
        filename = files.get(position).getFilename();
        final String path = files.get(position).getPath();


        setAnimation(holder.itemView, position);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFileItemClickListener.onItemClick(type, position,filename, path);
            }
        });

        boolean lasted = false;
        if (files.get(position).getDateTime() != 0 && position == 0){
            lasted = true;
        }
        holder.fillUi(filename, type, lasted);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class FilesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_layout)
        ConstraintLayout mLayout;

        @BindView(R.id.img_file)
        ImageView mFileImg;

        @BindView(R.id.tv_file_name)
        TextView mTvFilename;

        @BindView(R.id.iv_lasted)
        ImageView ivLasted;

        @BindView(R.id.tv_lasted)
        TextView tvLasted;

        public FilesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind( this, itemView);
        }

        public void fillUi(String filename, int type, boolean lasted){
             if(lasted){
                 tvLasted.setVisibility(View.VISIBLE);
                 ivLasted.setVisibility(View.VISIBLE);
             }

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

                 case UpdateActivity.AUTOMATIC_SEARCH:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_find_in_page));
                     break;

                 case UpdateActivity.DOWNLOAD:
                     mFileImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_file_download));
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
