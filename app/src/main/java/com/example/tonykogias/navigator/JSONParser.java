package com.example.tonykogias.navigator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {

    private String rtrip;
    private String rdistance;
    private String distancemetric;
    private String scheduled;
    private String estimated;
    private String timemetric;
    private String cspeed;
    private String pspeed;
    private String speedmetric;
    private String deviation;
    private JSONArray points;


    private String opcode;




    public JSONParser(String jsonstr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonstr);

            rtrip = jsonObj.getString("rtrip");
            rdistance = jsonObj.getString("rdistance");
            distancemetric = jsonObj.getString("distancemetric");
            scheduled = jsonObj.getString("scheduled");
            estimated = jsonObj.getString("estimated");
            timemetric = jsonObj.getString("timemetric");
            cspeed = jsonObj.getString("cspeed");
            pspeed = jsonObj.getString("pspeed");
            speedmetric = jsonObj.getString("speedmetric");
            deviation = jsonObj.getString("deviation");
            points = jsonObj.getJSONArray("points");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getRtrip() {
        return rtrip;
    }

    public String getRdistance() {
        return rdistance + "" + distancemetric;
    }

    public String getDistancemetric() {
        return distancemetric;
    }

    public String getScheduled() {
        return scheduled.split(" ")[1];
    }

    public String getEstimated() {
        return estimated.split(" ")[1];
    }

    public String getTimemetric() {
        return timemetric;
    }

    public String getCspeed() {
        return cspeed;
    }

    public String getPspeed() {
        return pspeed;
    }

    public String getSpeedmetric() {
        return speedmetric;
    }

    public String getDeviation() {
        return deviation;
    }

    public JSONArray getPoints() {
        return points;
    }




}
