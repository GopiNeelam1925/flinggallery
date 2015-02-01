package com.arongeorgel.flinggallery.activity;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arongeorgel.flinggallery.FlingGalleryApplication;
import com.arongeorgel.flinggallery.R;
import com.arongeorgel.flinggallery.adapter.GalleryAdapter;
import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.BusProvider;
import com.arongeorgel.flinggallery.network.NetworkConstants;
import com.arongeorgel.flinggallery.network.event.ApiErrorEvent;
import com.arongeorgel.flinggallery.network.event.LoadPhotosEvent;
import com.arongeorgel.flinggallery.network.event.NetworkStateChanged;
import com.arongeorgel.flinggallery.network.event.PhotosLoadedEvent;
import com.arongeorgel.flinggallery.persistence.ImageBean;
import com.arongeorgel.flinggallery.widget.PreCachingLayoutManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends ActionBarActivity implements EventListener, View.OnClickListener {
    private RecyclerView mPhotoRecyclerView;
    private GalleryAdapter mAdapter;
    private PreCachingLayoutManager mLayoutManager;
    private RelativeLayout mConnectionLayout;
    private TextView mRetryConnection;
    private ProgressBar mRetryProgress;

    private List<FlingImage> mImageList;
    private Bus mBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mBus = BusProvider.getInstance();
        this.mImageList = new ArrayList<FlingImage>();
        setupUi();

        mBus.register(this);
        if(FlingGalleryApplication.getInstance().isConnectedToInternet()) {
            mBus.post(new LoadPhotosEvent());
        } else {
            mConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setupUi() {
        this.mPhotoRecyclerView = (RecyclerView) findViewById(R.id.gallery_image_list);
        this.mConnectionLayout = (RelativeLayout) findViewById(R.id.connection_layout);
        this.mRetryConnection = (TextView) findViewById(R.id.connection_retry);
        this.mRetryProgress = (ProgressBar) findViewById(R.id.progress_retry);

        this.mLayoutManager = new PreCachingLayoutManager(this);
        this.mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.mLayoutManager.setDefaultExtraLayoutSpace(getScreenHeight());

        this.mPhotoRecyclerView.setLayoutManager(mLayoutManager);

        this.mAdapter = new GalleryAdapter(this, this.mImageList);
        this.mPhotoRecyclerView.setAdapter(this.mAdapter);

        this.mRetryConnection.setOnClickListener(this);
    }

    @Subscribe
    public void onPhotosLoaded(PhotosLoadedEvent event) {
        this.mImageList.addAll(event.getImageList());
        // since we have only one activity we can use this trick for showing a "splash screen"
        getSupportActionBar().show();
        getWindow().getDecorView().setBackgroundColor(
                getResources().getColor((R.color.white)));
    }

    @Subscribe
    public void onNetworkChanged(NetworkStateChanged event) {
        if(event.isConnected()) {
            mConnectionLayout.setVisibility(View.GONE);
        } else {
            mConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        if(FlingGalleryApplication.getInstance().isConnectedToInternet()) {
            Toast.makeText(this, getString(R.string.api_error), Toast.LENGTH_SHORT).show();
        } else {
            // failed because of network
            mRetryConnection.setVisibility(View.VISIBLE);
            mRetryProgress.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_data:
                printStatisticsMap(ImageBean.generateStatistics());
                break;
            case R.id.action_about:
                showAboutDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    private int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connection_retry:
                // retry to fetch photos
                mBus.post(new LoadPhotosEvent());
                mRetryConnection.setVisibility(View.GONE);
                mRetryProgress.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void printStatisticsMap(Map<Integer, String> map) {
        Log.i(NetworkConstants.TAG, "printing statistics map");
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Log.i("FlingStatistics", (String) pairs.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    /**
     * custom scroll listener for recycler view in order to load data from database
     * in batches. Use this scroll in for loading images in batches
     */
    private class EndlessScroll extends RecyclerView.OnScrollListener {
        private int previousTotal = 0;
        private boolean loading = true;
        private int visibleThreshold = 3;
        private int firstVisibleItem;
        private int visibleItemCount;
        private int totalItemCount;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = mPhotoRecyclerView.getChildCount() - 3;
            totalItemCount = mLayoutManager.getItemCount();
            firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // Fetch here information from database and add it for our list
                mAdapter.notifyDataSetChanged();
                loading = true;
            }
        }
    }

    private void showAboutDialog() {
        PackageInfo pInfo = null;
        String version = getString(R.string.n_a);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(NetworkConstants.TAG, "failed to fetch package info");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_about));
        builder.setMessage(String.format(getString(R.string.about_message), version));
        builder.setPositiveButton(getString(R.string.ok), null);
        builder.create();
        builder.show();
    }
}
