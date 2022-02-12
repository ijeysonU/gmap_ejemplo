package com.example.gmap_ejemplo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class InfoWindowCustom implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemSelectedListener {
    Context ctx;
    LayoutInflater inflater;
    private List<MarkerInfo> mkrList;
    public InfoWindowCustom(Context context){
        this.ctx = context;
    }

    public InfoWindowCustom(Context mCtx, List<MarkerInfo> mkrs){
        this.mkrList = mkrs;
        ctx = mCtx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.echo_info_window, null);

        TextView title = (TextView)v.findViewById(R.id.title);
        TextView stitle = (TextView)v.findViewById(R.id.stitle);
        ImageView img = (ImageView)v.findViewById(R.id.imgUrl);
        title.setText(marker.getTitle());
        stitle.setText(marker.getSnippet());
        MarkerInfo mi = (MarkerInfo)marker.getTag();
        try {
            Picasso.get()
                    .load(mi.getImg())
                    //.placeholder(R.drawable.portada)
                    //.error(R.drawable.caratula)
                    .into(img, new MarkerCallback(marker));
        }catch(Exception ex){

        }


        return v;
    }
    public class MarkerCallback implements Callback {
        private Marker markerToRefresh;

        public MarkerCallback(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            if (markerToRefresh != null && markerToRefresh.isInfoWindowShown()) {
                markerToRefresh.hideInfoWindow();
                markerToRefresh.showInfoWindow();
            }
        }

        @Override
        public void onError(Exception e) {
            System.out.println("errorPicasso:" + e.getMessage());
        }
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
