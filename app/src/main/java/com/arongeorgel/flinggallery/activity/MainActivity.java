package com.arongeorgel.flinggallery.activity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.arongeorgel.flinggallery.R;
import com.arongeorgel.flinggallery.adapter.GalleryAdapter;
import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.BusProvider;
import com.arongeorgel.flinggallery.network.event.LoadPhotosEvent;
import com.arongeorgel.flinggallery.network.event.PhotosLoadedEvent;
import com.arongeorgel.flinggallery.persistance.ImageBean;
import com.arongeorgel.flinggallery.widget.PreCachingLayoutManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static android.content.Context.*;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends ActionBarActivity implements EventListener {
    private RecyclerView mPhotoRecyclerView;
    private GalleryAdapter mAdapter;
    private PreCachingLayoutManager mLayoutManager;

    private List<ImageBean> mImageList;
    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBus = BusProvider.getInstance();
        this.mImageList = new ArrayList<ImageBean>();
        setupUi();

        mBus.register(this);
        mBus.post(new LoadPhotosEvent());
    }

    private void setupUi() {
        this.mPhotoRecyclerView = (RecyclerView) findViewById(R.id.gallery_image_list);

        this.mLayoutManager = new PreCachingLayoutManager(this);
        this.mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.mLayoutManager.setDefaultExtraLayoutSpace(getScreenHeight());

        this.mPhotoRecyclerView.setLayoutManager(mLayoutManager);

        this.mAdapter = new GalleryAdapter(this, this.mImageList);
        this.mPhotoRecyclerView.setAdapter(this.mAdapter);
    }

    @Subscribe
    public void onPhotosLoaded(PhotosLoadedEvent event) {
        Log.wtf("TAG", "we have event in activity");
        this.mImageList.clear();
        this.mImageList.addAll(event.getImageList());
        mAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    private int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
