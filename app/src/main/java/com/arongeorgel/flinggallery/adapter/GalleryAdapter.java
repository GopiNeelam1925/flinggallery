package com.arongeorgel.flinggallery.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arongeorgel.flinggallery.FlingGalleryApplication;
import com.arongeorgel.flinggallery.R;
import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.NetworkConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *
 * Created by arongeorgel on 26/01/2015.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {
    private List<FlingImage> mObjects;
    private ImageLoader mImageLoader;

    public GalleryAdapter(List<FlingImage> objects) {
        mObjects = objects;
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view_gallery, parent, false);

        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        try {
            FlingImage image = mObjects.get(position);
            holder.mPhotoTitle.setText(image.getTitle());

            String imgUrl = NetworkConstants.BASE_URL + "/photos/" + image.getImageId();
            mImageLoader.displayImage(imgUrl, holder.mPhotoView,
                    FlingGalleryApplication.getInstance().getDefaultImageOptions());
        } catch (NullPointerException e) {
            Log.wtf("TAG", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder {
        public TextView mPhotoTitle;
        public ImageView mPhotoView;

        public PhotoHolder(View view) {
            super(view);
            mPhotoTitle = (TextView) view.findViewById(R.id.photo_title);
            mPhotoView = (ImageView) view.findViewById(R.id.photo);

        }
    }
}
