package com.arongeorgel.flinggallery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * Created by aron georgel on 25/01/2015.
 */
@SuppressWarnings("ALL")
public class FlingImage implements Serializable {
    @SerializedName("ID")
    private int mId;
    @SerializedName("ImageID")
    private int mImageId;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("UserID")
    private int mUserId;
    @SerializedName("UserName")
    private String mUserName;

    public FlingImage() {
    }

    public FlingImage(int id, int imageId, String title, int userId, String userName) {
        mId = id;
        mImageId = imageId;
        mTitle = title;
        mUserId = userId;
        mUserName = userName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlingImage that = (FlingImage) o;

        if (mId != that.mId) return false;
        if (mImageId != that.mImageId) return false;
        if (mUserId != that.mUserId) return false;
        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        if (!mUserName.equals(that.mUserName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mImageId;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + mUserId;
        result = 31 * result + mUserName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FlingImage{" +
                "mId=" + mId +
                ", mImageId=" + mImageId +
                ", mTitle='" + mTitle + '\'' +
                ", mUserId=" + mUserId +
                ", mUserName='" + mUserName + '\'' +
                '}';
    }
}
