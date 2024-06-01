package com.example.hachikocoffee.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

public class CategoryDomain implements Parcelable {
    @PropertyName("Title")
    private String title;
    @PropertyName("CategoryID")
    private int id;
    @PropertyName("ImageURL")
    private String picUrl;

    public CategoryDomain() {}

    public CategoryDomain(int id, String picUrl, String title) {
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
    }

    protected CategoryDomain(Parcel in) {
        title = in.readString();
        id = in.readInt();
        picUrl = in.readString();
    }

    public static final Creator<CategoryDomain> CREATOR = new Creator<CategoryDomain>() {
        @Override
        public CategoryDomain createFromParcel(Parcel in) {
            return new CategoryDomain(in);
        }

        @Override
        public CategoryDomain[] newArray(int size) {
            return new CategoryDomain[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryID() {
        return id;
    }

    public void setCategoryID(int id) {
        this.id = id;
    }

    public String getImageURL() {
        return picUrl;
    }

    public void setImageURL(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeString(picUrl);
    }
}

