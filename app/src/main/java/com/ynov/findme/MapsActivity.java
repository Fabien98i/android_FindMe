package com.ynov.findme;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private static final int PERMS_CALL_ID = 1234;
    private GoogleMap mMap;
    private Toolbar toolbar;
    private AutoCompleteTextView selectGare;
    private LocationManager lm;
    private SupportMapFragment mapFragment;

    // Dialog de rechargement de la page
    private static ProgressDialog progressDialog;
    String current_adresse = null;

    // initialisation du marker
    private Marker myMarker;
    //liste de modèles Gares, ajout des noms de gares dans le modèle de données
    private List <Gares> listeGares = new ArrayList<>();


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

        //je recupère les noms de gares declarés dans les ressources
        String [] gares = getResources().getStringArray(R.array.gares);
        for (int j=0; j < gares.length; j++){ listeGares.add(new Gares(gares[j])); }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gares);
        selectGare.setAdapter(adapter);
        selectGare.setThreshold(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home) {
            Intent intentList = new Intent(MapsActivity.this, HomeActivity.class);
            startActivity(intentList);
            return true;
        }
        if(id == R.id.action_objet) {
            Intent intentObjet = new Intent(MapsActivity.this, SelectObjectActivity.class);
            startActivity(intentObjet);
            return true;
        }
        if(id == R.id.action_info) {
            Intent intentOther = new Intent(MapsActivity.this, OtherActivity.class);
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

    //verification des permissions et l'accès aux fournisseurs tels que le GPS, le provider, ...
    private void checkPermissions () {
        //verification des permissions
        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION }, PERMS_CALL_ID) ; //appel asynchrone
            return;
        }
        //je recois mes différents informations grace à mes différents fournisseurs
        lm = (LocationManager) getSystemService (LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 0,this);
        }
        if(lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000, 0,this);
        }
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000, 0,this);
        }
        //après verif, je load ma map.
        loadMap();
    }

    //Acquerir les différents fournisseurs et s'abonner a ces derniers
    @Override
    //@SuppressWarnings("MissingPermission")
    protected void onResume(){
        super.onResume();
        //verification des permissions
        checkPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(lm != null) {
            //si on passé le checking sur les permissions alors :
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL_ID) { checkPermissions(); }
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder((MapsActivity.this));
        //a chaque changement, j'update mes coordonnées et je les récupères pour les renvoyer au service d'itinéraire
        List <Address> listAdresses = null;
        try {
            listAdresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(listAdresses == null || listAdresses.size() == 0){
            Log.e("GPS : ", "Erreur, aucune adresse trouvee");
        }
        else{
            Address address = listAdresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
            {
                addressFragments.add(address.getAddressLine(i));
            }
            current_adresse = addressFragments.get(0);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @SuppressWarnings("MissingPermission")
    private void loadMap () {
        mapFragment.getMapAsync (new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                googleMap.setMyLocationEnabled(true);
                List<Address> gareList = null;

                //pour toutes les Gares faire:
                for (int i = 0; i < listeGares.size(); i++) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        //renvoyer la recherche sur la map
                        gareList = geocoder.getFromLocationName(listeGares.get(i).getName(), 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //recuperer cette recherche et le mettre dans une variable
                    Address address = gareList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    myMarker = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.search_loupe_sm))
                            .position(latLng).title(listeGares.get(i).getName()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                progressDialog.dismiss();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String markertitle = marker.getTitle();
                        String params_current_add = current_adresse;
                        Intent i = new Intent(MapsActivity.this, DetailsGare.class);
                        i.putExtra("gare", markertitle);
                        i.putExtra("currentAdress", params_current_add);
                        startActivity(i);
                        return true;
                    }
                });
            }
        } );
    }

    public void submit (View view) {
        List <Address> gareList = null;
        Geocoder geocoder = new Geocoder(MapsActivity.this);

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
        try {
            gareList = geocoder.getFromLocationName(mySearch, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //recuperer cette recherche et le mettre dans une variable
        Address address = gareList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        //je zoom sur la gare en question
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}










