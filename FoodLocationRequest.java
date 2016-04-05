package com.example.noel2.activity7;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by noel2 on 4/4/2016.
 */


public class FoodLocationsRequest extends AsyncTask<String, Void, JSONObject> {
    private JSONArray locations;
    private Activity activity;
    private LatLng myPosition;
    private GoogleMap map;

    public FoodLocationsRequest(Activity activity, GoogleMap map, LatLng position) {
        this.locations = new JSONArray();
        this.myPosition = position;
        this.activity = activity;
        this.map = map;
    }

    public double distance(LatLng target, LatLng currentPosition){
        double d =  Math.sqrt(Math.pow((target.latitude - currentPosition.latitude), 2) +
                Math.pow((target.longitude - currentPosition.longitude), 2));
        return d;
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(activity, "Retrieving food locations...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpGet get = new HttpGet(params[0]);
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        JSONObject jo = null;

        try{
            HttpResponse response = client.execute(get);
            StatusLine sl = response.getStatusLine();
            int code = sl.getStatusCode();

            Log.i("HTTP", "CODE:" + code);
            //HTTP successful request
            if(code == 200){
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String currentLine;
                while((currentLine = reader.readLine()) != null){
                    sb.append(currentLine);
                    Log.i("JSON REQUEST", "Getting JSON");
                }
                jo = new JSONObject(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jo;
    }

    @Override
    protected void onPostExecute(JSONObject result){
        JSONObject current;
        MarkerOptions marker;
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        int n = 0;
        try {
            locations = result.getJSONArray("FoodLocations");
            LatLng near = new LatLng(locations.getJSONObject(0).getDouble("lat"), locations.getJSONObject(0).getDouble("lng"));
            for(int i = 0; i < locations.length(); i++){
                current = locations.getJSONObject(i);
                LatLng pos = new LatLng(current.getDouble("lat"), current.getDouble("lng"));

                marker = new MarkerOptions();
                marker.position(pos);
                marker.title(current.getString("name"));
                marker.snippet("Click here for more information.");

                if(distance(pos, myPosition) < distance(near, myPosition)){
                    near = pos;
                    n = i;
                }
                markers.add(marker);
            }

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent i = new Intent(activity, ShowInformationActivity.class);
                    i.putExtra("name", marker.getTitle());
                    i.putExtra("location", marker.getPosition().latitude + ", " + marker.getPosition().longitude);
                    i.putExtra("json", locations.toString());
                    activity.startActivity(i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < markers.size(); i++){
            marker = markers.get(i);
            if(n == i){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            map.addMarker(marker);
        }
        Toast.makeText(activity, "Done!", Toast.LENGTH_SHORT).show();
    }
}
