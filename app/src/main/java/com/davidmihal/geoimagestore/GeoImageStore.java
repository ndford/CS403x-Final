package com.davidmihal.geoimagestore;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David Mihal on 4/30/2015.
 */
public class GeoImageStore {
    private static final String BASE_URL = "http://geoimagestore.appspot.com/";

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
        URL uploadUrl = getUploadUrl();
        //TODO: Upload the photo
    }
    private URL getUploadUrl()
    {
        try {
            HttpURLConnection httpUrlConnection = null;
            URL url = new URL(BASE_URL + "upload_url");
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("GET");

            String response = getResponse(httpUrlConnection);
            httpUrlConnection.disconnect();
            URL uploadURL = new URL(response);
            return uploadURL;
        } catch (Exception  e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getResponse(HttpURLConnection conn) throws IOException {
        StringBuilder response  = new StringBuilder();

        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String strLine = null;
        while ((strLine = input.readLine()) != null) {
            response.append(strLine);
        }
        input.close();
        return response.toString();
    }
}
