package com.kynangso.net.mysmile_jokes.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable{
    private int mId;
    private String mEnTitle;
    private String mViTitle;
    private String mEnContent;
    private String mViContent;

    public Story(int mId, String mEnTitle, String mViTitle, String mEnContent, String mViContent) {
        this.mId = mId;
        this.mEnTitle = mEnTitle;
        this.mViTitle = mViTitle;
        this.mEnContent = mEnContent;
        this.mViContent = mViContent;
    }

    protected Story(Parcel in) {
        mId = in.readInt();
        mEnTitle = in.readString();
        mViTitle = in.readString();
        mEnContent = in.readString();
        mViContent = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmEnTitle() {
        return mEnTitle;
    }

    public void setmEnTitle(String mEnTitle) {
        this.mEnTitle = mEnTitle;
    }

    public String getmViTitle() {
        return mViTitle;
    }

    public void setmViTitle(String mViTitle) {
        this.mViTitle = mViTitle;
    }

    public String getmEnContent() {
        return mEnContent;
    }

    public void setmEnContent(String mEnContent) {
        this.mEnContent = mEnContent;
    }

    public String getmViContent() {
        return mViContent;
    }

    public void setmViContent(String mViContent) {
        this.mViContent = mViContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mEnTitle);
        dest.writeString(mViTitle);
        dest.writeString(mEnContent);
        dest.writeString(mViContent);
    }
}
