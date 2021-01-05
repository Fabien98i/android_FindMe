package com.ynov.findme.ui.maps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ynov.findme.ui.home.OtherActivity;
import com.ynov.findme.R;
import com.ynov.findme.ui.listing.SelectObjectActivity;
import com.ynov.findme.ui.home.HomeActivity;

public class MapTrackActivity extends AppCompatActivity {
    private EditText source;
    private TextView destination;
    private Button track;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_track);
        destination = (TextView)findViewById(R.id.destination);
        source = (EditText)findViewById(R.id.source);
        track = (Button)findViewById(R.id.track);
        String location_name = getIntent().getStringExtra("name_location");
        String current_address = getIntent().getStringExtra("currentAdresstoItineraire");

        if(location_name != null) {
            destination.setText(location_name);
        }
        if(current_address != null ) {
            source.setText(current_address);
        }
        if ( (current_address == null || current_address.equals(" ")) && (location_name == null || location_name.equals(" ")) ){
            AlertDialog.Builder adb = new AlertDialog.Builder(MapTrackActivity.this);
            adb.setTitle("Localisation de l'objet indéterminé ");
            adb.setMessage("La perte de l'objet n'as pas été signalé dans une gare ");
            adb.setPositiveButton("Ok", null);
            adb.show();
            Intent intent = new Intent(MapTrackActivity.this, SelectObjectActivity.class);
            startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home) {
            Intent intentList = new Intent(MapTrackActivity.this, HomeActivity.class);
            startActivity(intentList);
            return true;
        }
        if(id == R.id.action_objet) {
            Intent intentObjet = new Intent(MapTrackActivity.this, SelectObjectActivity.class);
            startActivity(intentObjet);
            return true;
        }
        if(id == R.id.action_info) {
            Intent intentOther = new Intent(MapTrackActivity.this, OtherActivity.class);
            startActivity(intentOther);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

}