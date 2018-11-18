package com.example.marek.earthquakeapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
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
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>  {


    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=1&limit=2000";

    private EarthquakeAdapter adapter;
    private View progressBar;
    private ListView listView;

    /**
     * Loading activity_main layout, which contain list view, progressbar and textview
     * Excecute background task downloadJson-downoalding rest from server
     * Setting custom adapter for listview
     * Setting onItemClickListener for listview
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        listView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress);
        getLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
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

    /**
     * Exceute after downloading thread, to show earthquake information
     *
     * @param earthquakes
     */
    public void updateUI(List<Earthquake> earthquakes) {
        progressBar.setVisibility(View.GONE);
        if (earthquakes == null) {
            earthquakesNotFounnd();
            return;
        }
        if (earthquakes.size() == 0) {
            earthquakesNotFounnd();
            return;
        }

        listView.setVisibility(View.VISIBLE);
        adapter.clear();
        adapter.addAll(earthquakes);

    }

    /**
     * If lack of inforamtion due to downnloading error
     * Hide list view and show error message
     */
    public void earthquakesNotFounnd() {
        listView.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.ifnotfound);
        textView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(MainActivity.this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        updateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        updateUI(new ArrayList<Earthquake>());
    }


}
