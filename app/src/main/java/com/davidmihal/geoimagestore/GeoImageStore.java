package com.davidmihal.geoimagestore;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
