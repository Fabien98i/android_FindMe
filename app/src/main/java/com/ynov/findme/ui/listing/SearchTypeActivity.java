
package com.ynov.findme.ui.listing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ynov.findme.R;
import com.ynov.findme.models.ApiObject;
import com.ynov.findme.ui.home.OtherActivity;
import com.ynov.findme.ui.maps.MapsActivity;
import com.ynov.findme.utils.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class  SearchTypeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText editTextCity;
    private ListView listViewType;
    //Declaration de l'adaptateur
    private ArrayAdapter <String> adapter ;
    //Declaration des formats des dates
    public static final String DATE_DASH_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    private TextView textDate;
    private Button buttonDate;
    private String paramsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type);

        editTextCity = (EditText) findViewById(R.id.editTextCity);
        listViewType = (ListView) findViewById(R.id.listViewType);
        textDate = (TextView) findViewById(R.id.textDate);
        buttonDate = (Button) findViewById(R.id.buttonDate);

        //EdithText ecouteurs sur les changements
        editTextCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textDate.setText("Selectionner une date ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Generation du calendrier.
        buttonDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                DialogFragment datePicker = new com.ynov.findme.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    // choix des items dans la liste Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_map) {
            Intent intentMap = new Intent(SearchTypeActivity.this, MapsActivity.class);
            startActivity(intentMap);
            return true;
        }

        if(id == R.id.action_info) {
            Intent list_gare = new Intent(SearchTypeActivity.this, OtherActivity.class);
            startActivity(list_gare);
            return true;
        }
        if(id == R.id.action_objet) {
            Intent intentObject = new Intent(SearchTypeActivity.this, SelectObjectActivity.class);
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

    // Remplacer l'espace par des "+" prenant en argument le nom de gare pour le mettre dans l'URL
    public String modifyTextCity () {
        String text = editTextCity.getText().toString();
        text = text.replace(" ", "+");
        return text;
    }

    // Choix Dates
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        paramsType = getIntent().getStringExtra("type_object");
        Log.e("PARAMSTYPE:","Value: "+ paramsType);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                .format(c.getTime());
        String currentDateFullString = DateFormat.getDateInstance(DateFormat.FULL)
                .format(c.getTime());
        textDate.setText(currentDateFullString);

        //convert the date (dd/mm/yyyy) to (yyyy-mm-dd)
        //Log.e(" 0 Date String ", "DATA FIELD: " + currentDateString);

        try {
            Date date = new SimpleDateFormat( DATE_FORMAT , Locale.ENGLISH ).parse(currentDateString);
            DateFormat formatter = new SimpleDateFormat( DATE_DASH_FORMAT , Locale.getDefault() );
            String trueDate = formatter.format( date.getTime() );

            editTextCity.getText();
            // HTTP REQUEST
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = String.format
                    (Constant.URL_SEARCH_TYPE, modifyTextCity(),trueDate,paramsType);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String json) {
                            Log.e("volley", "onResponse: " + json);
                            parseJsonDate(json, trueDate);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volley", "onErrorResponse" + error);

                    String json = new String(error.networkResponse.data);
                    parseJsonDate(json, trueDate);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        catch( ParseException e ) {
            e.printStackTrace();
        }
    }

    // lecture JSON
    private void parseJsonDate(String json, String trueDate) {
        String currentDate = trueDate;
        //Regeneration de la list  après chaque submit
        List<String> stringList = new ArrayList<>();
        HashMap<String, String> hashmap;
        ArrayList < HashMap<String, String> > listItem = new ArrayList < HashMap <String, String> >();
        //Liaison de GSON vers le modele: ApiObjects.
        ApiObject api = new Gson().fromJson(json, ApiObject.class);
        int tailleList = 0;

        //getnHits = nombre de resultats de la recherche
        //getRows = nombre de resultats à afficher
        if(api.getNhits() > api.getParameters().getRows())
        { tailleList = api.getParameters().getRows(); }
        else { tailleList = api.getNhits(); }

        //remplissage de la liste en fonction du nombre de lignes
        for (int i = 0; i < tailleList; i++) {
            hashmap = new HashMap<String,String>();
            hashmap.put("textViewObjType", api.getRecords().get(i).getFields().getGc_obo_type_c());
            hashmap.put("textViewObjNature", api.getRecords().get(i).getFields().getGc_obo_nature_c());
            hashmap.put("textViewObjDate", api.getRecords().get(i).getFields().getDate().replace("T", " "));

            listItem.add(hashmap);
        }
        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter adapter = new SimpleAdapter (
                SearchTypeActivity.this, listItem, R.layout.item_object,
                new String[] {"textViewObjType", "textViewObjNature", "textViewObjDate"},
                new int[]    {R.id.textViewObjType, R.id.textViewObjNature, R.id.textViewObjDate}
                );
        listViewType.setAdapter(adapter);

        listViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder adb = new AlertDialog.Builder(SearchTypeActivity.this);
                adb.setTitle("Nature de la perte:  " + api.getRecords().get(position).getFields().getGc_obo_nature_c());
                adb.setMessage( "\n - Type d'objet:  " + api.getRecords().get(position).getFields().getGc_obo_type_c() +
                        "\n - A la date du:  "       + api.getRecords().get(position).getFields().getDate().replaceAll("T", " ") +
                        "\n - Localisation:  "       + api.getRecords().get(position).getFields().getGc_obo_gare_origine_r_name());
                //on indique que l'on veut le bouton ok à notre boite de dialogue
                adb.setPositiveButton("Ok", null);
                //on affiche la boite de dialogue
                adb.show();
            }
        });
    }
}
