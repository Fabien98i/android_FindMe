package com.ynov.findme.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ynov.findme.R;
import com.ynov.findme.ui.listing.SelectObjectActivity;
import com.ynov.findme.ui.maps.MapsActivity;
import com.ynov.findme.utils.Constant;

public class OtherActivity extends AppCompatActivity {
    TextView textObjtPerdu;
    TextView questionObjTrouve;
    TextView textObjTrouve;
    ImageView imageViewObjetTrouve;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textObjtPerdu = (TextView) findViewById(R.id.textObjtPerdu);
        questionObjTrouve = (TextView) findViewById(R.id.questionObjTrouve);
        textObjTrouve = (TextView) findViewById(R.id.textObjTrouve);
        imageViewObjetTrouve = (ImageView) findViewById(R.id.imageViewObjetTrouve);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home) {
            Intent intentList = new Intent(OtherActivity.this, HomeActivity.class);
            startActivity(intentList);
            return true;
        }
        if(id == R.id.action_map) {
            Intent intentMap = new Intent(OtherActivity.this, MapsActivity.class);
            startActivity(intentMap);
            return true;
        }
        if(id == R.id.action_objet) {
            Intent intentObjet = new Intent(OtherActivity.this, SelectObjectActivity.class);
            startActivity(intentObjet);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void submit (View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setData(Uri.parse(buttonWeb.getText().toString()));
        intent.setData(Uri.parse(Constant.URL_WEBSITE_SNCF));
        startActivity(intent);
    }
}