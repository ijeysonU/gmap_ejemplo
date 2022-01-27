package com.example.gmap_ejemplo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {
    RequestQueue rq;
    GoogleMap map;
    Fragment frg;
    ArrayList<MarkerInfo> lstMarkers;
    public static final String URL1 = "https://my-json-server.typicode.com/ijeysonU/datamaps/db";
    Integer tipoVista;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        tipoVista = 1;
        rq = Volley.newRequestQueue(this);
        jsonObjectRequest();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLng CampusCentral = new LatLng(-1.011722,-79.468068);
        LatLng CampusLaMaria = new LatLng(-1.0803162, -79.5014542);

            map.setOnInfoWindowClickListener(this);
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {

                    View row = getLayoutInflater().inflate(R.layout.echo_info_window,null);
                    TextView tt = (TextView)row.findViewById(R.id.title);
                    TextView st = (TextView)row.findViewById(R.id.stitle);
                    ImageView img = (ImageView)row.findViewById(R.id.imgUrl);
                    tt.setText(marker.getTitle());
                    st.setText(marker.getSnippet());

                    return row;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });

    }

    public void ConfigurarMapa(View v){
        map.setMapType(tipoVista);
        tipoVista = tipoVista<4?tipoVista+1:1;
        LatLng ubicacion = new LatLng(-1.011722,-79.468068);
        CameraUpdate cm1 =
                CameraUpdateFactory.newLatLngZoom(ubicacion,10);
        map.moveCamera(cm1);
        jsonObjectRequest();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "HOLA MAPA",Toast.LENGTH_LONG).show();
    }

    private void jsonObjectRequest(){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL1,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                           JSONArray jsonArray =response.getJSONArray("data");
                           lstMarkers = MarkerInfo.JsonObjectsBuild(jsonArray);
                           InfoWindowCustom iwc = new InfoWindowCustom(MainActivity.this, lstMarkers);

                       }catch (JSONException ex){
                       System.out.println("Error: "+ex.toString());
                       //Toast.makeText(ex.getMessage(),Toast.LENGTH_LONG);
                       }
                       try {
                           View row = getLayoutInflater().inflate(R.layout.echo_info_window,null);
                           JSONArray jsonArray = response.getJSONArray("data");
                           int x =jsonArray.length();
                           for (int i = 0; i< x; i++){
                               JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                               String Fac = jsonObject.getString("fac");
                               Double lat = jsonObject.getDouble("lat");
                               Double lon = jsonObject.getDouble("lon");
                               String dir = jsonObject.getString("dir");
                               String web = jsonObject.getString("web");
                               String img = jsonObject.getString("logo");
                               String dec = jsonObject.getString("dec");

                               LatLng dd = new LatLng(lat, lon);
                               System.out.println("Fac: "+Fac+" - Lat: "+lat + "- Lon: "+lon);
                               marker = map.addMarker(new MarkerOptions()
                                       .position(dd)
                                       .draggable(true)
                                       .title(Fac)
                                       .snippet("Dirección: "+dir+
                                               "\nPágina web: "+web+
                                               "\nDecano: "+dec)

                                       );
                               try {
                                   Glide.with(MainActivity.this)
                                           .load(img)
                                           .into((ImageView)row.findViewById(R.id.imgUrl));
                               }catch(Exception ex){

                               }

                           }
                       }catch (JSONException ex){
                           System.out.println("Error: "+ex.toString());
                           //Toast.makeText(ex.getMessage(),Toast.LENGTH_LONG);
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error.toString());
            }
        }
        );

        rq.add(jsonArrayRequest);
    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}