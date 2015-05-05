package com.fbm.nkd.cs403x_final;


import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidmihal.geoimagestore.GeoImageStore;
import com.davidmihal.geoimagestore.GeoPhoto;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by David Mihal on 5/3/2015.
 */
public class GetNearbyPhotosTask extends AsyncTask<Location, Void, List<GeoPhoto>> {

   MainActivity mainActivity;
    String IMG_URL = "http://geoimagestore.appspot.com/serve/?blobKey=";
    private Location myLocation;

    public GetNearbyPhotosTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<GeoPhoto> doInBackground(Location... location) {
        if (location[0] != null) {
            myLocation = location[0];
            List<GeoPhoto> photos = GeoImageStore.getInstance().getNearbyPhotos(myLocation);
            for (GeoPhoto geoPhoto : photos){
                if(geoPhoto.getFileKey().equals("")){
                    photos.remove(geoPhoto);
                    continue;
                }
                Bitmap bitmapPhoto = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                try {
                    bitmapPhoto = BitmapFactory.decodeStream(
                            (InputStream) new URL(IMG_URL+geoPhoto.getFileKey()).getContent(),
                            null, options);
                } catch (MalformedURLException url_e){
                    Log.e("PHOTO_URL_ERROR", url_e.getMessage());
                    continue;
                } catch (IOException io_e){
                    Log.e("PHOTO_URL_ERROR", io_e.getMessage());
                    continue;
                }
                geoPhoto.setImage(bitmapPhoto);
            }

            return photos;
        } else {
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<GeoPhoto> geoPhotos) {
        mainActivity.setPhotoList(geoPhotos);
        FeedFragment feedFrag = (FeedFragment) mainActivity.getAdapter().getItem(0);
        MapFrag mapFrag = (MapFrag) mainActivity.getAdapter().getItem(2);
        feedFrag.populateFeed();
        mainActivity.getAdapter().refreshMapFrag();
    }
}
