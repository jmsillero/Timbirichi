package com.timbirichi.eltimbirichi.presentation.model;

/**
 * Created by JM on 11/16/2017.
 */

public class FileItem {
    String filename;
    String path;
    int type;
    int level;
    String updateDate;
    long dateTime = 0;

    public FileItem(String filename, String path, int type, int level) {
        this.filename = filename;
        this.path = path;
        this.type = type;
        this.level = level;
        this.updateDate = "No compatible";
        this.dateTime = 0;
    }

    public FileItem() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
