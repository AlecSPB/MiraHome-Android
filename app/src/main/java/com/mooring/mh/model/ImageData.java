package com.mooring.mh.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mooring.mh.adapter.HorizontalDataAdapter;

/**
 * 用于menu中横向滑动用户的model
 * <p/>
 * Created by Will on 16/4/7.
 */
public class ImageData implements HorizontalDataAdapter.AdapterData, Parcelable {
    private String title;
    private String thumbnailUrl;

    public ImageData(String title, String thumbnailUrl) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thumbnailUrl);
    }

    public static final Parcelable.Creator<ImageData> CREATOR = new Parcelable.Creator<ImageData>() {
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    private ImageData(Parcel in) {
        title = in.readString();
        thumbnailUrl = in.readString();
    }
}

