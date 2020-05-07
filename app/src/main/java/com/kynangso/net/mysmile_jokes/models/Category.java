package com.kynangso.net.mysmile_jokes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{
    private int mCategoryId;
    private String mCategoryTitle;
    private String mTableName;

    public Category() {
    }

    public Category(int mCategoryId, String mCategoryTitle, String mTableName) {
        this.mCategoryId = mCategoryId;
        this.mCategoryTitle = mCategoryTitle;
        this.mTableName = mTableName;


    }

    protected Category(Parcel in) {
        mCategoryId = in.readInt();
        mCategoryTitle = in.readString();
        mTableName = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getmCategoryTitle() {
        return mCategoryTitle;
    }

    public void setmCategoryTitle(String mCategoryTitle) {
        this.mCategoryTitle = mCategoryTitle;
    }

    public String getmTableName() {
        return mTableName;
    }

    public void setmTableName(String mTableName) {
        this.mTableName = mTableName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCategoryId);
        dest.writeString(mCategoryTitle);
        dest.writeString(mTableName);
    }
}
