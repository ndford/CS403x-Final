package com.fbm.nkd.cs403x_final;

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

    FeedFragment feed;

    public GetNearbyPhotosTask(FeedFragment feedFragment) {
        feed = feedFragment;
    }

    @Override
    protected List<GeoPhoto> doInBackground(Location... location) {
        if (location[0] != null) {
            return GeoImageStore.getInstance().getNearbyPhotos(location[0]);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<GeoPhoto> geoPhotos) {
        if (geoPhotos != null) {
            int childIndex = 0;
            LinearLayout listlayout = (LinearLayout) feed.getView().findViewById(R.id.feedListLayout);
            Log.d("DEBUG", "got the photos!");
            System.out.println("GOT THE PHOTOS");
            for (GeoPhoto geophoto : geoPhotos) {
                Bitmap bitmapPhoto = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                try {
                    bitmapPhoto = BitmapFactory.decodeStream(
                            (InputStream) new URL("http://geoimagestore.appspot.com/serve/?blobKey="+geophoto.getFileKey()).getContent(),
                            null,
                            options);
                } catch (MalformedURLException url_e){
                    Log.e("PHOTO_URL_ERROR", url_e.getMessage());
                    continue;
                } catch (IOException io_e){
                    Log.e("PHOTO_URL_ERROR", io_e.getMessage());
                    continue;
                }
                Log.d("DEBUG", "GOT A PHOTO!!!!!!");
                listlayout.addView(feed.getActivity().getLayoutInflater().inflate(R.layout.feeditem, feed.getViewgroup(), false), childIndex);
                View feedItem = listlayout.getChildAt(childIndex);
                ((ImageView) feedItem.findViewById(R.id.feedItemImage)).setImageBitmap(bitmapPhoto);
                ((TextView) feedItem.findViewById(R.id.imageTextView)).setText(geophoto.getName());

                childIndex++;
            }
        }
    }
}
