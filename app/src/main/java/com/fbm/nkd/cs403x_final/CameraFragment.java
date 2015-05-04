package com.fbm.nkd.cs403x_final;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.davidmihal.geoimagestore.GeoPhoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nate on 4/29/15.
 */
public class CameraFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    int REQUEST_CODE = 0;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
    private static final String TAG = "CallCamera";
    Uri fileUri = null;
    ImageView photoImage = null;
    ImageView xImage = null;
    private Button postBtn;
    private Bitmap currentPhoto;
    private EditText caption;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        xImage = (ImageView) view.findViewById(R.id.trashPhotoButton);
        xImage.setVisibility(View.GONE);
        xImage.setOnClickListener(new View.OnClickListener() {
            //photoImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearPreview();
            }
        });

        photoImage = (ImageView) view.findViewById(R.id.photoImage);
        photoImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                takePhoto();
            }
        });

        postBtn = (Button) view.findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storePhoto();
            }
        });

        caption = (EditText)view.findViewById(R.id.photoCaption);

        return view;
    }


    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getOutputPhotoFile();
        fileUri = Uri.fromFile(getOutputPhotoFile());
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
    }

    protected File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getActivity().getPackageName());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_"
                + timeStamp + ".jpg");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    // A known bug here! The image should have saved in fileUri
                    Toast.makeText(getActivity(), "Image saved successfully",
                            Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(getActivity(), "Image saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
                showPhoto(photoUri);
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Callout for image capture failed!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void showPhoto(Uri photoUri) {
        getView().findViewById(R.id.trashPhotoButton).setVisibility(View.VISIBLE);
        File imageFile = new File(photoUri.getPath());
        if (imageFile.exists()){
            currentPhoto = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), currentPhoto);
            photoImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            photoImage.setImageDrawable(drawable);
            xImage.setVisibility(View.VISIBLE);
            postBtn.setEnabled(true);
        }
    }

    private void storePhoto() {
        if (currentPhoto != null) {
            GeoPhoto photo = new GeoPhoto();
            photo.setLocation(getMyLocation());
            String capt = caption.getText().toString();
            if (capt.length() > 0){
                photo.setName(capt);
            }
            photo.setImage(currentPhoto);
            new UploadPhotoTask().execute(photo);

            clearPreview();
        }
    }

    private void clearPreview() {
        currentPhoto = null;
        caption.setText("");
        int id = getResources().getIdentifier("camera_icon", "drawable", getActivity().getPackageName());
        photoImage.setImageResource(id);
        xImage.setVisibility(View.GONE);
        postBtn.setEnabled(false);
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
