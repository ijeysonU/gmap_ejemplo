package com.example.gmap_ejemplo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {
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

        try {
            Glide.with(ctx)
                    .load(img)
                    .into((ImageView)v.findViewById(R.id.imgUrl))
            ;//(Drawable("https://evaladmin.uteq.edu.ec/adminimg/unknown.png"));
        }catch(Exception ex){

        }


        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
