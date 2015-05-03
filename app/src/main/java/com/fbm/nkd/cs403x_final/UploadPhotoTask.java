package com.fbm.nkd.cs403x_final;

import android.os.AsyncTask;

import com.davidmihal.geoimagestore.GeoImageStore;
import com.davidmihal.geoimagestore.GeoPhoto;

/**
 * Created by David Mihal on 5/3/2015.
 */
public class UploadPhotoTask extends AsyncTask<GeoPhoto, Void, Void> {

    @Override
    protected Void doInBackground(GeoPhoto... photos) {
        for (GeoPhoto photo : photos){
            GeoImageStore.getInstance().addPhoto(photo);
        }
        return null;
    }
}
