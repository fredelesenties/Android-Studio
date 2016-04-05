package com.example.noel2.activity7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ShowInformationActivity extends Activity {
    private URL url;
    private Button closeButton;
    private String name, latlng;
    private TextView nameView, locationView, reviewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information);

        nameView = (TextView) findViewById(R.id.nameView);
        locationView = (TextView) findViewById(R.id.locationView);
        reviewView = (TextView) findViewById(R.id.reviewView);
        closeButton = (Button) findViewById(R.id.closeButton);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        latlng = intent.getStringExtra("location");

        nameView.setText(name);
        locationView.setText("Location: " + latlng);
        try {
            JSONArray json = new JSONArray(intent.getStringExtra("json"));
            JSONObject current = null;
            for(int i = 0; i < json.length(); i++){
                current = json.getJSONObject(i);
                if(current.getString("name").equals(name)){
                    reviewView.setText(current.getString("description"));
                    url = new URL(current.getString("url"));
                    break;
                }
            }
            new ContentDownload(url, this).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCloseButtonPressed(View v){
        finish();
    }

    private class ContentDownload extends AsyncTask<String, String, Drawable> {
        private URL url;
        private Activity activity;
        private ImageView imageView;
        private ProgressBar pBar;

        private ContentDownload(URL url, Activity activity) {
            this.url = url;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar = (ProgressBar) activity.findViewById(R.id.progressBar);
            pBar.setIndeterminate(true);
        }

        @Override
        protected Drawable doInBackground(String... params) {
            InputStream content = null;
            try {
                content = (InputStream) url.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(content, "src");
            return d;
        }

        @Override
        protected void onPostExecute(Drawable result){
            pBar.setVisibility(View.GONE);
            imageView = (ImageView) activity.findViewById(R.id.pictureView);
            imageView.setImageDrawable(result);
        }
    }
}

