package com.example.gmap_ejemplo;

import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MarkerInfo {
    private String title;
    private String stitle;
    private String img;
    private Double lat;
    private Double lon;
    private int ngms;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStitle() {
        return stitle;
    }

    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNgms() {
        return ngms;
    }

    public void setNgms(int ngms) {
        this.ngms = ngms;
    }

    public MarkerInfo(JSONObject a){
        try
        {
            title = a.getString("fac").toString();
            stitle ="Facultad: "+ a.getString("dir").toString() + "\n "+
                    "Decano: "+a.getString("dec")+ "\n"+
                    "Web: "+a.getString("web").toString();
            img = a.getString("logo").toString();
        }catch (JSONException e)
        {
            System.out.println("Error: "+e.toString());
        }
    }

    public static ArrayList<MarkerInfo> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<MarkerInfo> mrkVal = new ArrayList<>();

        for (int i = 0; i < datos.length() ; i++) {
            mrkVal.add(new MarkerInfo(datos.getJSONObject(i)));
        }
        return mrkVal;
    }
}
