package com.fbm.nkd.cs403x_final;

import android.location.Location;
import android.os.AsyncTask;

import com.davidmihal.geoimagestore.GeoImageStore;
import com.davidmihal.geoimagestore.GeoPhoto;

import java.util.List;

/**
 * Created by David Mihal on 5/3/2015.
 */
public class GetNearbyPhotosTask extends AsyncTask<Location, Void, List<GeoPhoto>> {
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
        //Todo: do something with photos
        if (geoPhotos != null){
            geoPhotos.hashCode();
        }
    }
}
