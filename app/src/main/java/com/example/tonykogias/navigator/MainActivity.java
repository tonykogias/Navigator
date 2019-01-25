package com.example.tonykogias.navigator;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GPSUpdate, HttpAsyncResponse{

    ConstraintLayout layoutbg;
    ImageButton mapimg;
    ImageButton trafficbut;
    TextView speedText;

    TextView timeRemain;
    TextView distRemain;
    TextView timeSched;
    TextView timeEstimated;

    private GPSTracker gpsTracker;
    private Location mLocation;
    private LocationManager locationManager;
    Boolean isGPSEnab = false;

    double latitude, longitude;
    double speed = 0;
    int currentSpeed = 0;

    ImageView arrow;

    String points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapimg = findViewById(R.id.mapImg);
        trafficbut = findViewById(R.id.imageButtonTRAFIC);

        setBackground();

        speedText = findViewById(R.id.speed);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnab = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGPSEnab) {
            gpsTracker = new GPSTracker(getApplicationContext());
            mLocation = gpsTracker.getLocation();
            updateGPS(mLocation);
        } else {
            gpsTracker.showSettingsAlert();
            Toast.makeText(this, "Enable GPS in settings.", Toast.LENGTH_SHORT).show();
        }

        timeRemain = findViewById(R.id.remainingTime);
        distRemain = findViewById(R.id.remainingDist);
        timeSched = findViewById(R.id.schedTime);
        timeEstimated = findViewById(R.id.estimatedTime);
        arrow = findViewById(R.id.arrow);

        mapimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });

        trafficbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Traffic.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });


    }

    public void setBackground() {


        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); //Current hour
        System.out.println(currentHour);

        layoutbg = findViewById(R.id.conLayout);
        mapimg = findViewById(R.id.mapImg);
        trafficbut = findViewById(R.id.imageButtonTRAFIC);

        if(currentHour>6 && currentHour<18) {
            layoutbg.setBackgroundResource(R.drawable.wbg);
            trafficbut.setBackgroundResource(R.color.white);
        } else {
            layoutbg.setBackgroundResource(R.drawable.bbg);
            mapimg.setImageResource(R.drawable.rsz_bmap);
            trafficbut.setBackgroundResource(R.color.black);
        }


    }

    @Override
    public void updateGPS(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        speed = location.getSpeed();

        currentSpeed = (int) speed;
        speedText.setText(currentSpeed+"");

        String speedString = Integer.toString(currentSpeed);

        Map<String, String> data = new HashMap<>();
        data.put("speed", speedString+"");
        data.put("latitude", latitude+"");
        data.put("longitude", longitude+"");

        new HttpConnectionPostAsync(data, this).execute("http://192.168.21.214:8080/navigator.ws/server/getData");

    }
/*

ERROR NULL EXCEPTION

    @Override
    public void postData(String result) {

        JSONParser parse = new JSONParser(result);

        timeRemain.setText(parse.getRtrip());
        distRemain.setText(parse.getRdistance());
        timeSched.setText(parse.getScheduled());
        timeEstimated.setText(parse.getEstimated());
        points = parse.getPoints().toString();

        try {
            setArrow(Integer.parseInt(parse.getCspeed()), Integer.parseInt(parse.getPspeed()), parse.getScheduled(), parse.getEstimated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
*/

    @Override
    public void postData(String result) {

    }


    public void setArrow(int currSpeed, int propSpeed, String schedTime, String estTime) throws ParseException {


        if((propSpeed - currSpeed) > 5) {
            arrowIncr();
        } else if((currSpeed - propSpeed) > 5){
            arrowDecr();
            timeEstimated.setTextColor(Color.RED);
        } else {
            noArrow();
        }


    }

    public void arrowIncr() {
        arrow.setImageResource(R.drawable.rsz_up);
    }

    public void arrowDecr() {
        arrow.setImageResource(R.drawable.rsz_down);
    }

    public void noArrow() {
        arrow.setImageResource(android.R.color.transparent);
    }

}
