package com.example.marek.earthquakeapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

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
import java.util.List;

/**
 * Created by marek on 18.11.2018.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private final String URL;
    public EarthquakeLoader(Context context, String URL) {
        super(context);
        this.URL=URL;
    }

    @Override
    public List<Earthquake> loadInBackground() {

        ArrayList<Earthquake> earthquakes=new ArrayList<>();
        try {
            earthquakes.=extractEarthquakes(makeHttpRequest(URL));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return earthquakes;
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

                Log.e("Download JSON", "Problem parsing the  JSON results", e);
            }

            return earthquakes;
        }
        // Return the list of earthquakes
        return null;
    }

    /**
     * connect to server for json information
     * @param adress
     * @return String plain text information received from server
     * @throws IOException
     */
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

    /**
     * Bufferd reding from server response
     * @param inputStream
     * @return plain text
     */
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


    /**
     * creating new url
     * @param adress
     * @return url object
     */
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
