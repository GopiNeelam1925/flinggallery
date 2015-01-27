package com.arongeorgel.flinggallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.arongeorgel.flinggallery.adapter.GalleryAdapter;
import com.arongeorgel.flinggallery.model.FlingImage;
import com.arongeorgel.flinggallery.network.FlingService;
import com.arongeorgel.flinggallery.network.NetworkConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;


public class MainActivity extends ActionBarActivity {
    private RecyclerView mPhotoRecyclerView;
    private GalleryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<FlingImage> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mImageList = new ArrayList<FlingImage>();
        setupUi();

        new NetworkTask().execute();
    }

    private void setupUi() {
        this.mPhotoRecyclerView = (RecyclerView) findViewById(R.id.gallery_image_list);

        this.mLayoutManager = new LinearLayoutManager(this);
        this.mPhotoRecyclerView.setLayoutManager(mLayoutManager);

        this.mAdapter = new GalleryAdapter(this.mImageList);
        this.mPhotoRecyclerView.setAdapter(this.mAdapter);

    }

    private class NetworkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("TAG", "connecting to net");
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(NetworkConstants.BASE_URL)
                    .build();
            FlingService service = adapter.create(FlingService.class);
            List<FlingImage> images = service.listImages();
            for(FlingImage img : images) {
                Log.wtf("TAG", img.getTitle());
                mImageList.add(img);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
