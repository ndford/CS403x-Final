package com.fbm.nkd.cs403x_final;


import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.davidmihal.geoimagestore.GeoPhoto;

import java.util.List;

/**
 * Created by Nate on 4/29/15.
 */
public class FeedFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private ViewGroup viewgroup;
    private int mPage;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewgroup = container;
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //new GetNearbyPhotosTask(this).execute(getMyLocation());
    }

    public void populateFeed(){
        List<GeoPhoto> geoPhotos = ((MainActivity) getActivity()).getPhotoList();
        if (geoPhotos != null) {
            int childIndex = 0;
            LinearLayout listlayout = (LinearLayout) getView().findViewById(R.id.feedListLayout);
            for (GeoPhoto geophoto : geoPhotos) {
                listlayout.addView(getActivity().getLayoutInflater().inflate(R.layout.feeditem, viewgroup, false), childIndex);
                View feedItem = listlayout.getChildAt(childIndex);
                ImageView itemImage = (ImageView) feedItem.findViewById(R.id.feedItemImage);
                itemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                itemImage.setImageBitmap(geophoto.getImage());
                ((TextView) feedItem.findViewById(R.id.imageTextView)).setText(geophoto.getName());
                double distance = geophoto.getDistance(((MainActivity) getActivity()).getMyLocation());
                ((TextView) feedItem.findViewById(R.id.distanceTextView)).setText(String.format("%.2f mi", distance));

                childIndex++;
            }
        }
    }

    public ViewGroup getViewgroup(){
        return viewgroup;
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
