package com.ynov.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectObjectActivity extends AppCompatActivity {

    private ImageView imageViewValise;
    private ImageView imageViewElectronic;
    private ImageView imageViewClothes;
    private ImageView imageViewWallet;
    private ImageView imageViewPassport;
    private ImageView imageViewOptic;
    private ImageView imageViewLivre;
    private ImageView imageViewCle;
    private ImageView imageViewMontre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_object);
          imageViewValise = (ImageView) findViewById(R.id.imageViewValise);
          imageViewElectronic = (ImageView) findViewById(R.id.imageViewElectronic);
          imageViewClothes = (ImageView) findViewById(R.id.imageViewClothes);
          imageViewWallet = (ImageView) findViewById(R.id.imageViewWallet);
          imageViewPassport = (ImageView) findViewById(R.id.imageViewPassport);
          imageViewOptic = (ImageView) findViewById(R.id.imageViewOptic);
          imageViewLivre = (ImageView) findViewById(R.id.imageViewLivre);
          imageViewCle = (ImageView) findViewById(R.id.imageViewCle);
          imageViewMontre = (ImageView) findViewById(R.id.imageViewMontre);



        imageViewValise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Bagagerie:+sacs,+valises,+cartables";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur bagage", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Appareils+électroniques,+informatiques,+appareils+photo";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur appareils", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params="Vêtements,+chaussures";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur vetements", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);

            }
        });

        imageViewWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Porte-monnaie+%2F+portefeuille,+argent,+titres";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur porte feuille", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Pièces+d%27identités+et+papiers+personnels";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur passeport", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewOptic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Optique";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur optique", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewLivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Livres,+articles+de+papéterie";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur livre", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewCle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Clés,+porte-clés,+badge+magnétique";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur clés", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });

        imageViewMontre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String params = "Bijoux,+montres";
                Toast.makeText(SelectObjectActivity.this, "Tu as cliqué sur montres", Toast.LENGTH_LONG).show();
                Intent searchType = new Intent(SelectObjectActivity.this, SearchTypeActivity.class);
                searchType.putExtra("type_object", params);
                startActivity(searchType);
            }
        });
    }

    // choix des items dans la liste Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_map) {
            Intent intentMap = new Intent(SelectObjectActivity.this, MapsActivity.class);
            startActivity(intentMap);
            return true;
        }

        if(id == R.id.action_info) {
            Intent list_gare = new Intent(SelectObjectActivity.this, OtherActivity.class);
            startActivity(list_gare);
            return true;
        }
        if(id == R.id.action_home) {
            Intent intentObject = new Intent(SelectObjectActivity.this, HomeActivity.class);
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


    public void submitSearch(View view) {
        Intent intentSearchObject = new Intent(SelectObjectActivity.this, SearchObjectActivity.class);
        startActivity(intentSearchObject);
    }
}