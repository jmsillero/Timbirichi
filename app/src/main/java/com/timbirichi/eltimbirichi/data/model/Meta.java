package com.timbirichi.eltimbirichi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Meta implements Parcelable{

    public static final String TABLE_META = "meta";
    public static final String COL_META_ID = "id";
    public static final String COL_META_TIMESTAMP = "timestamp";
    public static final String COL_META_CODE = "code";
    public static final String COL_META_DATE = "date";



    long timestamp;
    String code;
    String strDate;



    public Meta(long timestamp, String code, String strDate) {
        this.timestamp = timestamp;
        this.code = code;
        this.strDate = strDate;
    }

    public Meta() {
    }


    public Meta(Parcel parcel) {
        timestamp = parcel.readLong();
        code = parcel.readString();
        strDate = parcel.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeString(code);
        dest.writeString(strDate);
    }

    public static final Parcelable.Creator<Meta> CREATOR = new Parcelable.Creator<Meta>(){
        @Override
        public Meta createFromParcel(Parcel source) {
            return new Meta(source);
        }

        @Override
        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }
}
