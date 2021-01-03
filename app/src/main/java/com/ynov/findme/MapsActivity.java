package com.ynov.findme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ynov.findme.models.Gares;
import com.ynov.findme.utils.FastDialog;
import com.ynov.findme.utils.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    Toolbar toolbar;
    AutoCompleteTextView selectGare;

    // Dialog de rechargement
    private final String LOG = "MapsActivity";
    private static ProgressDialog progressDialog;

    // initialisation du marker
    private Marker myMarker;

    List <Gares> listeGares = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        selectGare = (AutoCompleteTextView) findViewById(R.id.selectGare);
        setActionBar(toolbar);

        // Page de rechargement  : Progress Dialog
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String [] gares = getResources().getStringArray(R.array.gares);

        for (int j=0; j < gares.length; j++){
            listeGares.add(new Gares(gares[j]));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, gares);
        selectGare.setAdapter(adapter);
        Log.e("Adapteur","Gares 0 :  +++ " + gares[0]+ "<----");
        Log.e("Adapteur","Gares 1 :  +++ " + gares[1] + "<----");
        Log.e("Adapteur","Gares 2 :  +++ " + gares[2]+ "<----");
        Log.e("Adapteur","Gares 3 :  +++ " + gares[3] + "<----");
        selectGare.setThreshold(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_list) {
            Intent list_gare = new Intent(MapsActivity.this, SearchType.class);
            startActivity(list_gare);
            return true;
        }

        if(id == R.id.action_info) {
            Intent list_gare = new Intent(MapsActivity.this, OtherActivity.class);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        List <Address> gareList = null;


        //pour tout les Gares faire:
        for (int i=0 ; i <listeGares.size(); i++){
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            try {
                //renvoyer la recherche sur la map
                gareList = geocoder.getFromLocationName(listeGares.get(i).getName(), 1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            //recuperer cette recherche et le mettre dans une variable
            Address address = gareList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            myMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.search_loupe_m))
                    .position(latLng).title(listeGares.get(i).getName()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
        progressDialog.dismiss();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markertitle = marker.getTitle();
                    Log.e("TITLE marker ", "Name gare : "+ markertitle);
                    Intent i = new Intent(MapsActivity.this, DetailsGare.class);
                    i.putExtra("gare", markertitle);
                    startActivity(i);
                    //finish();
                return true;
            }
        });
    }

    public void submit (View view) {
        GoogleMap googleMap;
        List <Address> gareList = null;
        if (selectGare.getText().toString().isEmpty()) {
            FastDialog.showDialog(MapsActivity.this,
                    FastDialog.SIMPLE_DIALOG, "Vous devez renseigner un nom de gare");
            return;
        }

        if (!Network.isNetworkAvailable(MapsActivity.this)) {
            FastDialog.showDialog(MapsActivity.this,
                    FastDialog.SIMPLE_DIALOG,
                    "Vous devez etre connecté");
            return;
        }
        String mySearch = selectGare.getText().toString();
        Log.e("My Search",":  it is : " + mySearch + "<----");
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        try {
            //renvoyer la recherche sur la map
            gareList = geocoder.getFromLocationName(mySearch, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //recuperer cette recherche et le mettre dans une variable
        Address address = gareList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}


























/*

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}*/


/*

        //REQUETTE HTTP
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format(Constant.URL);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.e("volley", "onResponse: " + json);
                        parseJson(json);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "onErrorResponse" + error);

                String json = new String(error.networkResponse.data);
                parseJson(json);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



//Recuperation des noms de Gares et lier ces donner avec le modèle.
    private void parseJson(String json) {
        //Regeneration de la list  après chaque submit
        List<String> stringList = new ArrayList<>();
        //Liaison de GSON vers le modele: ApiObjects.
        ApiObject api = new Gson().fromJson(json, ApiObject.class);
        //remplissage de la liste en fonction du nombre de lignes
        for (int i = 0; i < 110; i++) {
            stringList.add(api.getFacetsGroups().get(i).getFacets().getPath());
            Log.e("Cities", "string list " + stringList.get(i));
        }
    }*/


       /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); */

        /*
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                if(arg0 != null){
                    Log.e("Marker", "Title: " + arg0.getTitle());
                }
            }
        }); */