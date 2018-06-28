package com.example.marek.earthquakeapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by marek on 24.06.2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {


    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.quakeview, parent, false);

        Earthquake earthquake = getItem(position);

        double mag = earthquake.getMag();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String output = decimalFormat.format(mag);
        TextView magnitude = view.findViewById(R.id.magnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        magnitude.setText(output);
        magnitudeCircle.setColor(magnitudeColor(mag));

        TextView location = view.findViewById(R.id.preciselocation);
        TextView location1 = view.findViewById(R.id.country);
        String preciseLocation = "";
        String country = "";
        if (earthquake.getLocation().contains(", ")) {
            String temp[] = earthquake.getLocation().split(", ");
            preciseLocation = temp[0];
            country = temp[1];
            location.setText(preciseLocation);
            location1.setText(country);
        } else {
            preciseLocation = earthquake.getLocation();
            location1.setVisibility(View.GONE);
            location.setText(preciseLocation);
            location.setGravity(Gravity.CENTER_VERTICAL);
        }


        Date dateObject = new Date(earthquake.getDate());
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(dateObject);
        TextView date = view.findViewById(R.id.date);

        date.setText(dateText);
        return view;
    }

    private int magnitudeColor(double mag) {

        int floorMag = ((int) Math.floor(mag));
        int color;
        switch (floorMag) {
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude1;

        }

        return ContextCompat.getColor(getContext(), color);


    }
}
