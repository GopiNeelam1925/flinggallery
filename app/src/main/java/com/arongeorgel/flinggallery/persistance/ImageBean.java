package com.arongeorgel.flinggallery.persistance;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * model for image table
 * Created by arongeorgel on 29/01/2015.
 */
@Table(name = "images")
public class ImageBean extends Model {
    @Column(name = "remote_id")
    private int mRemoteId;
    @Column(name = "image_id")
    private int mImageId;
    @Column(name = "title")
    private String mTitle;
    @Column(name = "user_id")
    private int mUserId;
    @Column(name = "user_name")
    private String mUserName;

    public ImageBean() {
    }

    public ImageBean(int remoteId, int imageId, String title, int userId, String userName) {
        mRemoteId = remoteId;
        mImageId = imageId;
        mTitle = title;
        mUserId = userId;
        mUserName = userName;
    }

    public int getRemoteId() {
        return mRemoteId;
    }

    public void setRemoteId(int remoteId) {
        mRemoteId = remoteId;
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

    public static List<ImageBean> getAllImages() {
        return new Select().from(ImageBean.class).orderBy("id ASC").execute();
    }
}
