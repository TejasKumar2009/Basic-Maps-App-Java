package com.tejas.tmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DetailsActivity extends AppCompatActivity {
    ArrayList<String> detailsInfo1 = new ArrayList<String>();
//    {"Address", "Place Name", "Locality", "Country", "Postal Code", "Latitude", "Longitude", "Phone Number", "Website"};
    String[] detailsInfot = {"Tejas", "Mannu", "Kannu", "Ahana", "Sayansh", "MannTej", "VibhKan", "Sanshu", "Shinchan"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Adding Items in detailsInfo1
        detailsInfo1.add("Address");
        detailsInfo1.add("Place Name");
        detailsInfo1.add("Locality");
        detailsInfo1.add("Country");
        detailsInfo1.add("Postal Code");
        detailsInfo1.add("Latitude");
        detailsInfo1.add("Longitude");
        detailsInfo1.add("Phone Number");
        detailsInfo1.add("Website");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
         String[] detailsInfo2 = bundle.getStringArray("detailsInfo2");

        ListView detailsListView = findViewById(R.id.detailsListView);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, detailsInfo1){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(detailsInfo1.get(position));
                text2.setText(detailsInfo2[position]);

                return view;
            }
        };



        detailsListView.setAdapter(adapter);

    }
};