package com.fbm.nkd.cs403x_final;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davidmihal.geoimagestore.GeoPhoto;
import com.davidmihal.geoimagestore.GeoImageStore;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Nate on 4/29/15.
 */
public class MapFrag extends Fragment {

    private static View view;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private static GoogleMap mMap;
    private static Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);
        // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
        latitude = 42.274681;
        longitude = -71.808833;

        setUpMapIfNeeded(); // For setting up the MapFragment

        return view;
    }



    /***** Sets up the map if it is possible to do so *****/
    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.location_map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                GeoImageStore imageStore = GeoImageStore.getInstance();
                List<GeoPhoto> photos = imageStore.getNearbyPhotos(getMyLocation());

                if (photos != null) {

                    for (GeoPhoto geophoto : photos) {
                        Bitmap bitmapPhoto = null;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        try {
                            bitmapPhoto = BitmapFactory.decodeStream(
                                    (InputStream) new URL("http://geoimagestore.appspot.com/serve/?blobKey=" + geophoto.getFileKey()).getContent(),
                                    null,
                                    options);
                        } catch (MalformedURLException url_e) {
                            Log.e("PHOTO_URL_ERROR", url_e.getMessage());
                            continue;
                        } catch (IOException io_e) {
                            Log.e("PHOTO_URL_ERROR", io_e.getMessage());
                            continue;
                        }

                        double latitude = geophoto.getLatitude();
                        double longitude = geophoto.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);

                        Bitmap icon = Bitmap.createScaledBitmap(bitmapPhoto, 200, 200, false);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("MyImage")
                                .icon(BitmapDescriptorFactory.fromBitmap(icon)));

                    }
                }
            }

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
   private static void setUpMap() {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                longitude), 16.0f));


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    //    Log.d("OnDestroyView", "OnDestroyView Called");
      if (mMap != null) {
          getChildFragmentManager().beginTransaction()
                   .remove(getChildFragmentManager().findFragmentById(R.id.location_map)).commitAllowingStateLoss();
          mMap = null;
        }

    }

    @Override
    public void onResume(){
        super.onResume();



    }

    private Location getMyLocation() {
        // Get location from GPS if it's available
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);
        }

        return myLocation;
    }

}
