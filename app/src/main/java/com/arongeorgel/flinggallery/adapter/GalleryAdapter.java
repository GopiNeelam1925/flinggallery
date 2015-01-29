package com.arongeorgel.flinggallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arongeorgel.flinggallery.R;
import com.arongeorgel.flinggallery.network.NetworkConstants;
import com.arongeorgel.flinggallery.persistance.ImageBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter used for image gallery list.
 * Implemented using new cards view
 * <p/>
 * Created by arongeorgel on 26/01/2015.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {
    private List<ImageBean> mObjects;
    private Context mContext;

    public GalleryAdapter(Context context, List<ImageBean> objects) {
        mObjects = objects;
        mContext = context;
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
        ImageBean image = mObjects.get(position);
        holder.mPhotoTitle.setText(image.getTitle());

        String imgUrl = NetworkConstants.BASE_URL + "/photos/" + image.getImageId();
        Log.i(NetworkConstants.TAG, "Loading image " + imgUrl);

        Picasso.with(mContext).load(imgUrl).fit().into(holder.mPhotoView);
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
