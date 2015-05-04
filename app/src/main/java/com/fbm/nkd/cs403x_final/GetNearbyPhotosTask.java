package com.fbm.nkd.cs403x_final;

import android.os.AsyncTask;

import com.davidmihal.geoimagestore.GeoImageStore;
import com.davidmihal.geoimagestore.GeoPhoto;

import java.util.List;

/**
 * Created by David Mihal on 5/3/2015.
 */
public class GetNearbyPhotosTask extends AsyncTask<Void, Void, List<GeoPhoto>> {
    @Override
    protected List<GeoPhoto> doInBackground(Void... voids) {
        return GeoImageStore.getInstance().getNearbyPhotos();
    }

    @Override
    protected void onPostExecute(List<GeoPhoto> geoPhotos) {
        //Todo: do something with photos
        geoPhotos.hashCode();
    }
}
