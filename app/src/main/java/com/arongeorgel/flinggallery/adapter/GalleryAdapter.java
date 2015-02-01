package com.arongeorgel.flinggallery.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arongeorgel.flinggallery.FlingGalleryApplication;
import com.arongeorgel.flinggallery.R;
import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.NetworkConstants;
import com.arongeorgel.flinggallery.widget.FontCache;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter used for image gallery list.
 * Implemented using new cards view
 * <p/>
 * Created by arongeorgel on 26/01/2015.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoHolder> {
    public static final int BATCH_SIZE = 6;
    protected List<FlingImage> mObjects;
    protected Context mContext;
    protected static AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(
            FlingGalleryApplication.getInstance().getApplicationContext(), R.animator.flip_right_out);
    protected static AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(
            FlingGalleryApplication.getInstance().getApplicationContext(), R.animator.flip_left_in);

    public GalleryAdapter(Context context, List<FlingImage> objects) {
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
        FlingImage image = mObjects.get(position);
        holder.mPhotoTitle.setText(image.getTitle());
        holder.mPhotoTitleAndAuthor.setText(String.format(
                mContext.getString(R.string.photo_title_author),
                image.getTitle(), image.getUserName()));

        String imgUrl = NetworkConstants.BASE_URL + NetworkConstants.PHOTO_PATH + "/" + image.getImageId();
        Log.i(NetworkConstants.TAG, "Loading image " + imgUrl);
        /**
         * NOTE: With Picasso we can use various options for downloading
         * photos and displaying the photo.
         *
         * What these mean:
         * fit - wait until the ImageView has been measured and resize the image to exactly match its size.
         * centerCrop - scale the image honoring the aspect ratio until it fills the size.
         *      Crop either the top and bottom or left and right so it matches the size exactly.
         * center inside - show full image (no crop)
         *
         * My personal choice would be to crop the image and on click show it larger at full size
         * But for this project i have left it with #centerInside() since onClick we have card flip animation
         */

        Picasso.with(mContext).load(imgUrl)
                .stableKey(imgUrl).tag(holder.mPhotoView)
                .fit().centerInside()
                .into(holder.mPhotoView);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mPhotoTitle;
        public TextView mPhotoTitleAndAuthor;
        public ImageView mPhotoView;
        public RelativeLayout mFrontPhotoLayout;
        public RelativeLayout mBackPhotoLayout;
        private boolean mBackVisible;

        public PhotoHolder(View view) {
            super(view);
            mFrontPhotoLayout = (RelativeLayout) view.findViewById(R.id.front_photo_layout);
            mBackPhotoLayout = (RelativeLayout) view.findViewById(R.id.back_photo_layout);
            mPhotoTitle = (TextView) view.findViewById(R.id.photo_title);
            mPhotoView = (ImageView) view.findViewById(R.id.photo);
            mPhotoTitleAndAuthor = (TextView) view.findViewById(R.id.photo_title_and_user);
            mPhotoTitleAndAuthor.setTypeface(FontCache.get("journal.ttf",
                    FlingGalleryApplication.getInstance().getBaseContext()));

            mFrontPhotoLayout.setOnClickListener(this);
            mBackVisible = false;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.front_photo_layout:
                    if(!mBackVisible) {
                        setRightOut.setTarget(mFrontPhotoLayout);
                        setLeftIn.setTarget(mBackPhotoLayout);
                        setRightOut.start();
                        setLeftIn.start();
                        mBackVisible = true;
                    } else {
                        setRightOut.setTarget(mBackPhotoLayout);
                        setLeftIn.setTarget(mFrontPhotoLayout);
                        setRightOut.start();
                        setLeftIn.start();
                        mBackVisible = false;
                    }
            }
        }
    }


}
