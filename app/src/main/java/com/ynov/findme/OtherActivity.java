package com.ynov.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.ynov.findme.utils.Constant;

public class OtherActivity extends AppCompatActivity {
    TextView textObjtPerdu;
    TextView questionObjTrouve;
    TextView textObjTrouve;
    //Button buttonWebSncf;
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
        //buttonWebSncf = (Button) findViewById(R.id.buttonWebSncf);
        imageViewObjetTrouve = (ImageView) findViewById(R.id.imageViewObjetTrouve);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_list) {
            Intent list_gare = new Intent(OtherActivity.this, SearchTypeActivity.class);
            startActivity(list_gare);
            return true;
        }

        if(id == R.id.action_map) {
            Intent list_gare = new Intent(OtherActivity.this, MapsActivity.class);
            startActivity(list_gare);
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