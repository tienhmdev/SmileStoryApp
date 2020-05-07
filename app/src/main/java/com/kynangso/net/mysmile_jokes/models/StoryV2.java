package com.kynangso.net.mysmile_jokes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StoryV2 implements Parcelable {
    private int id;
    private String title;
    private String content;

    public StoryV2(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public StoryV2() {
    }

    protected StoryV2(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<StoryV2> CREATOR = new Creator<StoryV2>() {
        @Override
        public StoryV2 createFromParcel(Parcel in) {
            return new StoryV2(in);
        }

        @Override
        public StoryV2[] newArray(int size) {
            return new StoryV2[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
    }
}
