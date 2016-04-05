package com.example.frede.homework6;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class FriendRequest extends AsyncTask<String, Void, JSONObject> {
    private Activity activity;
    public ArrayList<String> names;
    public ListView friendZone;

    public FriendRequest(Activity activity){
        this.activity = activity;
        this.names = new ArrayList<>();
    }


    @Override
    protected void onPreExecute() {
        Toast.makeText(activity, "Loading data.", Toast.LENGTH_LONG).show();
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

            if(code == 200){
                //All is perfect!
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String currentLine;
                while((currentLine = br.readLine()) != null){
                    sb.append(currentLine);
                    Log.i("JSON REQUEST", "Everything fine here!");
                }
            }
            jo = new JSONObject(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jo;
    }

    @Override
    protected void onPostExecute(JSONObject result){
        friendZone = (ListView) activity.findViewById(R.id.FriendZone);
        try {
            JSONArray friends =  result.getJSONArray("friends");
            for(int i = 0; i < friends.length(); i++){
                this.names.add(friends.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, names);
        friendZone.setAdapter(adapter);
    }

}
