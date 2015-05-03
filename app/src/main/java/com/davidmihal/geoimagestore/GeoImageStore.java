package com.davidmihal.geoimagestore;

/**
 * Created by David Mihal on 4/30/2015.
 */
public class GeoImageStore {
    private static GeoImageStore instance = null;

    private GeoImageStore(){}

    public static GeoImageStore getInstance(){
        if (instance == null){
            instance = new GeoImageStore();
        }
        return instance;
    }
}
