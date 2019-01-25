package com.example.tonykogias.navigator;

import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class Traffic extends AppCompatActivity implements HttpAsyncResponse{

    ImageButton traffLow;
    ImageButton traffNorm;
    ImageButton traffBig;
    Button cancelButt;
    Button okButt;

    String trafficIMGid;
    String latString;
    String longString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        traffLow = findViewById(R.id.traffLow);
        traffNorm = findViewById(R.id.traffNorm);
        traffBig = findViewById(R.id.traffBig);

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        traffLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Low Traffic Conditions", R.drawable.rsz_trafficlow);
                //trafficIMGid = traffLow.getTag().toString();
            }
        });

        traffNorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Normal Traffic Conditions", R.drawable.rsz_trafficnormal);
                //trafficIMGid = traffNorm.getTag().toString();
            }
        });

        traffBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Dense Traffic Conditions", R.drawable.rsz_trafficdense);
                //trafficIMGid = traffBig.getTag().toString();
            }
        });

        latString = Double.toString(latitude);
        longString = Double.toString(longitude);
    }

    public void showDialog(String title, int img) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Traffic.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.trafdialog_layout, null);
        builder.setView(view);

        TextView dialogtitle = view.findViewById(R.id.dialogTitle);
        dialogtitle.setText(title);

        ImageView dialogimage = view.findViewById(R.id.dialogImage);
        dialogimage.setImageResource(img);

        cancelButt = view.findViewById(R.id.cancelButt);
        okButt = view.findViewById(R.id.okButt);

        final AlertDialog dialog = builder.create();

        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> data = new HashMap<>();
                data.put("latitude", latString);
                data.put("longitude", longString);
                data.put("traffic", trafficIMGid);
                new HttpConnectionPostAsync(data, Traffic.this).execute("http://192.168.21.214:8080/navigator.ws/server/postTrafficData");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void postData(String result) {

    }
}
