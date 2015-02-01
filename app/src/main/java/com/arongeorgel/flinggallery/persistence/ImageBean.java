package com.arongeorgel.flinggallery.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * model for image table
 * Created by arongeorgel on 29/01/2015.
 */
@SuppressWarnings("UnusedDeclaration")
@Table(name = "images")
public class ImageBean extends Model {
    @Column(name = "remote_id", unique = true)
    private int mRemoteId;
    @Column(name = "image_id")
    private int mImageId;
    @Column(name = "title")
    private String mTitle;
    @Column(name = "user_id")
    private int mUserId;
    @Column(name = "user_name")
    private String mUserName;
    @Column(name = "image_size")
    private float mImageSize;
    @Column(name = "image_width")
    private int mImageWidth;

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

    public float getImageSize() {
        return mImageSize;
    }

    public void setImageSize(float imageSize) {
        mImageSize = imageSize;
    }

    public int getImageWidth() {
        return mImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        mImageWidth = imageWidth;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public static List<ImageBean> getAllImages() {
        return new Select().from(ImageBean.class).orderBy("id ASC").execute();
    }

    public static List<ImageBean> getBatchImages(int startRow, int limit) {
        return new Select().from(ImageBean.class).offset(startRow).limit(limit).execute();
    }

    /**
     * get an image by remote id
     * @param remoteId id from server side
     * @return ImageBean if found, null otherwise
     */
    public static ImageBean getImage(int remoteId) {
        List<ImageBean> list = new Select().from(ImageBean.class).where("remote_id = ? ", remoteId).execute();
        if(list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * get an image based on it's id
     * @param imageId id of the image
     * @return ImageBean if found, null otherwise
     */
    public static ImageBean getImageById(int imageId) {
        List<ImageBean> list = new Select().from(ImageBean.class).where("remote_id = ? ", imageId).execute();
        if(list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static Map<Integer, String> generateStatistics() {
        Map<Integer, String> mapStats = new HashMap<>();
        SQLiteDatabase db = Cache.openDatabase();
        String query = "SELECT DISTINCT user_id, user_name, COUNT(user_id) AS result FROM images " +
                "GROUP BY user_id";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                String stat = "";
                int count = cursor.getInt(2);
                int userId = cursor.getInt(0);
                stat += cursor.getString(1) + " has " + count + " photos";

                String avgQuery = "SELECT AVG(image_size) FROM images WHERE user_id = " + userId;
                String maxQuery = "SELECT MAX(image_width) FROM images WHERE user_id = " + userId;
                Cursor avgCursor = db.rawQuery(avgQuery, null);
                Cursor maxCursor = db.rawQuery(maxQuery, null);
                if(avgCursor != null) {
                    avgCursor.moveToFirst();
                    float avgSize = avgCursor.getFloat(0);
                    stat += ". Average image size: " + avgSize + " bytes";
                    avgCursor.close();
                }

                if(maxCursor != null) {
                    maxCursor.moveToFirst();
                    int maxWidth = maxCursor.getInt(0);
                    stat += ". Greatest photo width is " + maxWidth;
                    maxCursor.close();
                }

                mapStats.put(cursor.getInt(0), stat);
            }
            cursor.close();
        }


        return mapStats;
    }

}
