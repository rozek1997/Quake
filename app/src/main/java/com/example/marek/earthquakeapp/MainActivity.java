package com.example.marek.earthquakeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=1&limit=500";

    private EarthquakeAdapter adapter;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        DownloadJson downloadJson = new DownloadJson();
        downloadJson.execute(USGS_REQUEST_URL);
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Earthquake current = (Earthquake) adapterView.getItemAtPosition(i);
                intent.setData(Uri.parse(current.getUrlAdress()));
                startActivity(intent);
            }
        });

    }

    public void updateUI(ArrayList<Earthquake> earthquakes) {
        progressBar.setVisibility(View.GONE);
        if (earthquakes == null) {
            earthquakesNotFounnd();
            return;
        }
        if (earthquakes.size() == 0) {
            earthquakesNotFounnd();
            return;
        }

        ListView listView = findViewById(R.id.list);
        listView.setVisibility(View.VISIBLE);
        adapter.clear();
        adapter.addAll(earthquakes);

    }

    public void earthquakesNotFounnd() {
        ListView listView = findViewById(R.id.list);
        listView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.ifnotfound);
        textView.setVisibility(View.VISIBLE);
    }


    private class DownloadJson extends AsyncTask<String, Void, ArrayList<Earthquake>> {


        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {
            ArrayList<Earthquake> earthquakes = null;
            try {
                earthquakes = extractEarthquakes(makeHttpRequest(urls[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return earthquakes;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {

            updateUI(earthquakes);
        }


        public ArrayList<Earthquake> extractEarthquakes(String json) {

            if (json != "") {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("features");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = jsonArray.getJSONObject(i);
                        JSONObject temp1 = temp.getJSONObject("properties");
                        earthquakes.add(new Earthquake(temp1.getDouble("mag"), temp1.getString("place"), temp1.getLong("time"), temp1.getString("url")));
                    }

                } catch (JSONException e) {

                    Log.e("QueryUtils", "Problem parsing the  JSON results", e);
                }

                return earthquakes;
            }
            // Return the list of earthquakes
            return null;
        }

        public String makeHttpRequest(String adress) throws IOException {

            HttpURLConnection urlConnection = null;
            String response = "";
            URL url = createURL(adress);
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    throw new IOException();
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("makeHHTPRequest", response);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (inputStream != null)
                    inputStream.close();
            }


            return response;


        }

        public String readFromStream(InputStream inputStream) {
            StringBuilder stringBuilder = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                try {
                    line = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (line != null) {
                    stringBuilder.append(line);
                    try {
                        line = bufferedReader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return stringBuilder.toString();
        }


        public URL createURL(String adress) {
            URL url;
            try {
                url = new URL(adress);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            return url;

        }
    }
}
