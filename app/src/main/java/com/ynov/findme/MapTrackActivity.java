package com.ynov.findme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MapTrackActivity extends AppCompatActivity {
    private EditText source;
    private TextView destination;
    private Button track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_track);

        destination = (TextView)findViewById(R.id.destination);
        source = (EditText)findViewById(R.id.source);
        track = (Button)findViewById(R.id.track);
        String location_name = getIntent().getStringExtra("name_location");
        String current_address = getIntent().getStringExtra("currentAdresstoItineraire");
        if(location_name!= null) {
            destination.setText(location_name);
        }
        if(current_address != null ) {
            source.setText(current_address);
        }
        else {
            AlertDialog.Builder adb = new AlertDialog.Builder(MapTrackActivity.this);
            adb.setTitle("Localisation de l'objet indéterminé ");
            adb.setMessage("La perte de l'objet n'as pas été signalé dans une gare ");
            adb.setPositiveButton("Ok", null);
            adb.show();
        }

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String sSource = source.getText().toString().trim();
                String sDestination = destination.getText().toString().trim();

                //condition
                if(sSource.equals("") && sDestination.equals("")){
                    //erreurs
                    Toast.makeText(getApplicationContext(), "Entrer une localisation valide", Toast.LENGTH_SHORT).show();
                }
                else {
                    //valider
                    DisplayTrack(sSource,sDestination);
                }
            }
        });
    }

    private void DisplayTrack(String sSource, String sDestination) {
        //If the device does not have a map installed, then redirect it to play store
        try {
            //When google map is installed
            //Initialize uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+sSource+"/"+sDestination);
            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //set package
            intent.setPackage("com.google.android.apps.maps");
            //Set Flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start Activity
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //When google map is not installed
            //initialise URI
            Uri uri = Uri.parse("https://^play.google.com/store/apps/details?id=com.google.android.apps.maps");
            //Initialise Intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}