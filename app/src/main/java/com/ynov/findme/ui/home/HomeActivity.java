package com.ynov.findme.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ynov.findme.ui.maps.MapsActivity;
import com.ynov.findme.R;
import com.ynov.findme.ui.listing.SelectObjectActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    // choix des items dans la liste Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_map) {
            Intent intentMap = new Intent(HomeActivity.this, MapsActivity.class);
            startActivity(intentMap);
            return true;
        }

        if(id == R.id.action_info) {
            Intent list_gare = new Intent(HomeActivity.this, OtherActivity.class);
            startActivity(list_gare);
            return true;
        }
        if(id == R.id.action_objet) {
            Intent intentObject = new Intent(HomeActivity.this, SelectObjectActivity.class);
            startActivity(intentObject);
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