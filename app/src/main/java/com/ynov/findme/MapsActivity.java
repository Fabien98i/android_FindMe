package com.ynov.findme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //Google map
    GoogleMap mMap;
    //Toolbar
    Toolbar toolbar;
    List<String> stringList = new ArrayList<>();

    // Dialog de rechargement
    private final String LOG = "MapsActivity";
    private static ProgressDialog progressDialog;

    // initialisation du marker
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //TOOLBAR
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_list) {
            Intent list_gare = new Intent(MapsActivity.this, SearchType.class);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        List <Address> gareList = null;

        List<Gares> listeGares = new ArrayList<>();
        listeGares.add(new Gares("Calais Ville"));
        listeGares.add(new Gares("Paris Gare du Nord"));
        listeGares.add(new Gares("Paris Saint-Lazare"));
        listeGares.add(new Gares("Strasbourg"));
        listeGares.add(new Gares("Bordeaux Saint-Jean"));
        listeGares.add(new Gares("Paris Est"));
        listeGares.add(new Gares("Lille Europe"));
        listeGares.add(new Gares("Lyon Part Dieu"));
        listeGares.add(new Gares("Lyon Perrache"));
        listeGares.add(new Gares("Marseille Saint-Charles"));
        listeGares.add(new Gares("Nice"));
        listeGares.add(new Gares("Montpellier Saint-Roch"));
        listeGares.add(new Gares("Rennes"));
        listeGares.add(new Gares("Paris Montparnasse"));
        listeGares.add(new Gares("Nantes"));
        listeGares.add(new Gares(" Paris Austerlitz"));
        listeGares.add(new Gares("Toulouse Matabiau"));
        listeGares.add(new Gares("Metz Ville"));
        listeGares.add(new Gares("Dijon"));
        listeGares.add(new Gares("Orléans"));
        listeGares.add(new Gares("Tours" ));
        listeGares.add(new Gares("Nancy"));
        listeGares.add(new Gares("Caen"));
        listeGares.add(new Gares("Mulhouse" ));
        listeGares.add(new Gares("Avignon TGV" ));
        listeGares.add(new Gares("Poitiers" ));
        listeGares.add(new Gares("La Rochelle" ));
        listeGares.add(new Gares( "Amiens" ));
        listeGares.add(new Gares( "Grenoble" ));
        listeGares.add(new Gares( "Quimper" ));
        listeGares.add(new Gares( "Cannes" ));
        listeGares.add(new Gares( "Colmar"));
        listeGares.add(new Gares("Toulon" ));
        listeGares.add(new Gares("Hendaye"));
        listeGares.add(new Gares("Bellegarde"));
        listeGares.add(new Gares("Saint-Pierre-des-Corps"));
        listeGares.add(new Gares("Brest"));
        listeGares.add(new Gares("Annecy"));
        listeGares.add(new Gares("Perpignan"));
        listeGares.add(new Gares("Aix-en-Provence TGV"));
        listeGares.add(new Gares("Besançon Viotte"));
        listeGares.add(new Gares("Aéroport Charles de Gaulle 2 TGV"));
        listeGares.add(new Gares("Reims"));
        listeGares.add(new Gares("Les Aubrais"));
        listeGares.add(new Gares("Valence"));
        listeGares.add(new Gares("Paris Bercy"));
        listeGares.add(new Gares("Creil"));
        listeGares.add(new Gares("Nevers"));
        listeGares.add(new Gares("Mantes-la-Jolie"));
        listeGares.add(new Gares("Massy TGV"));
        listeGares.add(new Gares("Le Havre"));
        listeGares.add(new Gares("Marne-la-Vallée Chessy"));
        listeGares.add(new Gares("Dunkerque"));
        listeGares.add(new Gares("Saint-Nazaire"));
        listeGares.add(new Gares("Compiègne"));
        listeGares.add(new Gares("Châteauroux"));
        listeGares.add(new Gares("Le Croisic"));
        listeGares.add(new Gares("Valenciennes"));
        listeGares.add(new Gares("Chartres"));
        listeGares.add(new Gares("Vannes"));
        listeGares.add(new Gares("Lisieux"));
        listeGares.add(new Gares("Roanne"));
        listeGares.add(new Gares("Saverne"));
        listeGares.add(new Gares("Granville"));
        listeGares.add(new Gares("Saintes"));
        listeGares.add(new Gares("Carcassonne"));
        listeGares.add(new Gares("Paris Gare de Lyon"));

        for (int i=0 ; i <listeGares.size(); i++){
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            try {
                gareList = geocoder.getFromLocationName(listeGares.get(i).getName(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

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