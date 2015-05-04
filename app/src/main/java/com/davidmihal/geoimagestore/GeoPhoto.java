package com.davidmihal.geoimagestore;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by David Mihal on 4/29/2015.
 */
public class GeoPhoto {
    private String name;
    private Date date;
    private String account;
    private double latitude;
    private double longitude;
    private String fileKey;

    private Bitmap image;

    public static GeoPhoto fromJSON(JSONObject obj) throws JSONException{
        GeoPhoto photo = new GeoPhoto();
        photo.setFileKey(obj.getString("File"));
        photo.setLatitude(obj.getJSONObject("Location").getDouble("Lat"));
        photo.setLongitude(obj.getJSONObject("Location").getDouble("Lng"));
        photo.setName(obj.getString("Name"));
        try {
            photo.setDate(fromISODateString(obj.getString("Date")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photo;
    }

    private static String FORMAT_DATE_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ";

    private static Date fromISODateString(String isoDateString) throws Exception {
        isoDateString = isoDateString.replaceFirst("(.*):(..)", "$1$2");
        DateFormat f = new SimpleDateFormat(FORMAT_DATE_ISO);
        f.setTimeZone(TimeZone.getTimeZone("Zulu"));
        return f.parse(isoDateString);
    }


    public String getName() {
        return name != null ? name : "Untitled";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
