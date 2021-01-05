package com.ynov.findme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.ynov.findme.models.ApiObject;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;



public class DetailsGare extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView textTitle;
    private ListView listNature;
    private TextView textDate;
    private Button buttonDate;
    private ArrayAdapter <String> adapter ;
    //Declaration des formats des dates
    public static final String DATE_DASH_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_gare);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
        textTitle = (TextView) findViewById(R.id.textTitle);
        listNature =(ListView) findViewById(R.id.listNature);
        textDate = (TextView) findViewById(R.id.textDate);
        buttonDate = (Button) findViewById(R.id.buttonDate);

        String title = getIntent().getStringExtra("gare");
        textTitle.setText(title);

        //Generation du calendrier.
        buttonDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                DialogFragment datePicker = new com.ynov.findme.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        String currentDateFullString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        textDate.setText(currentDateFullString);

        //convert the date (dd/mm/yyyy) to (yyyy-mm-dd)
        try {
            Date date = new SimpleDateFormat( DATE_FORMAT , Locale.ENGLISH ).parse(currentDateString);
            DateFormat formatter = new SimpleDateFormat( DATE_DASH_FORMAT , Locale.getDefault() );
            String trueDate = formatter.format( date.getTime() );
            textTitle.getText();

            // HTTP REQUEST
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = String.format(Constant.URL_SEARCH_OBJECT, modifyTextCity(),trueDate);

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

    /* TO REPLACE ESPACE BY "+"  TO HTTP REQUEST in THE GARE NAME */
    public String modifyTextCity () {
        String text = textTitle.getText().toString();
        text = text.replace(" ", "+");
        return text;
    }

    private void parseJsonDate(String json, String trueDate) {
        String dateModified = trueDate;
        int tailleList = 0;
        HashMap<String, String> hashmap;
        ArrayList < HashMap<String, String> > listItem = new ArrayList < HashMap <String, String> >();
        ApiObject api = new Gson().fromJson(json, ApiObject.class);

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
            hashmap.put("textViewObjDate", dateModified);

            listItem.add(hashmap);
        }
        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter adapter = new SimpleAdapter (
                DetailsGare.this, listItem, R.layout.item_map_object,
                new String[] {"textViewObjType", "textViewObjNature", "textViewObjDate"},
                new int[]    {R.id.textViewObjType, R.id.textViewObjNature, R.id.textViewObjDate}
        );
        listNature.setAdapter(adapter);

        listNature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nom_de_gare = api.getRecords().get(position).getFields().getGc_obo_gare_origine_r_name();
                Log.e("Position & Gare", "Postion : "+ position + "Gare : " +nom_de_gare);
               /* if((nom_de_gare != null) || !(nom_de_gare.isEmpty()) ){
                    Intent intent = new Intent (SearchObjectActivity.this, MapTrackActivity.class);
                    intent.putExtra("name_location", nom_de_gare);
                    startActivity(intent);
                } */
                try {
                    String current_address = getIntent().getStringExtra("currentAdress");
                    Intent intent = new Intent (DetailsGare.this, MapTrackActivity.class);
                    intent.putExtra("name_location", nom_de_gare);
                    intent.putExtra("currentAdresstoItineraire", current_address);
                    startActivity(intent);

                }
                catch (NullPointerException e){
                    AlertDialog.Builder adb = new AlertDialog.Builder(DetailsGare.this);
                    adb.setTitle("Localisation de l'objet indéterminé ");
                    adb.setMessage("La perte de l'objet n'as pas été signalé dans une gare ");
                    adb.setPositiveButton("Ok", null);
                    adb.show();
                }
            }
        });
    }
}
