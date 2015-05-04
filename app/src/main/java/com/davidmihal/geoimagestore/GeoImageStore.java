package com.davidmihal.geoimagestore;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Mihal on 4/30/2015.
 */
public class GeoImageStore {
    private static final String BASE_URL = "http://geoimagestore.appspot.com/";
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");

    private final OkHttpClient client = new OkHttpClient();

    private static GeoImageStore instance = null;

    private GeoImageStore(){}

    public static GeoImageStore getInstance(){
        if (instance == null){
            instance = new GeoImageStore();
        }
        return instance;
    }
    public void addPhoto(GeoPhoto photo)
    {
        String uploadUrl = getUploadUrl();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.getImage().compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] img = stream.toByteArray();

        try {
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"title\""),
                            RequestBody.create(null, photo.getName()))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"lat\""),
                            RequestBody.create(null, Double.toString(photo.getLatitude())))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"lng\""),
                            RequestBody.create(null, Double.toString(photo.getLongitude())))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"upload.jpg\""),
                            RequestBody.create(MEDIA_TYPE_JPG, img))
                    .build();

            Request request = new Request.Builder()
                    .url(uploadUrl)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Log.d("GeoImageStore", response.body().string());

        } catch (Exception  e) {
            e.printStackTrace();
        }
    }
    public List<GeoPhoto> getNearbyPhotos(Location location)
    {
        List<GeoPhoto> photos = new ArrayList<GeoPhoto>();

        String url = BASE_URL + "nearby?lat=" + location.getLatitude() +
                "&lng=" + location.getLongitude();

        JSONArray json;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

            json = new JSONArray(response.body().string());
            for (int i=0; i < json.length(); i++){
                JSONObject obj = json.getJSONObject(i);
                GeoPhoto photo = GeoPhoto.fromJSON(obj);
                photos.add(photo);
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return photos;
    }
    private String getUploadUrl()
    {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "upload_url")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
