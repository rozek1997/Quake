package com.example.marek.earthquakeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Earthquake> places=new ArrayList<Earthquake>();
        places=QueryUtils.extractEarthquakes();
        EarthquakeAdapter adapter=new EarthquakeAdapter(this, places);
        ListView listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                Earthquake current=(Earthquake)adapterView.getItemAtPosition(i);
                intent.setData(Uri.parse(current.getUrlAdress()));
                startActivity(intent);
            }
        });

    }
}
