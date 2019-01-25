package com.example.tonykogias.navigator;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private  GPSUpdate gpsUpdate = null;

    Location location;
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context=context;
    }

    public Location getLocation() {

        try{

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if(isGPSEnabled) {
                    if(location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 10, this);
                        if(locationManager!=null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            System.out.println("GOT LOC");
                        }
                    }
                }

                //if location is not found from GPS then it will found from network
                if(location == null) {
                    if(isNetworkEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 10, this);
                            if(locationManager!=null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                System.out.println("GOT LOC!");
                            }

                    }
                }
            }
        } catch (Exception e){

        }

        return location;
    }

    public void onLocationChanged(Location location) {
        if(this.gpsUpdate != null) {
            this.gpsUpdate.updateGPS(location);
        }
    }

    public void onStatusChanged(String Provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String Provider) {

    }

    public void onProviderDisabled(String Provider) {

    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void setGpsUpdate(GPSUpdate gpsUpdate) {
        this.gpsUpdate = gpsUpdate;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS setting");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

