package com.ynov.findme;

import android.app.DatePickerDialog;
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
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;



public class DetailsGare extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView title_txt;
    ListView list_nature;
    TextView textDate;
    Button buttonDate;
    private ArrayAdapter <String> adapter ;
    private Typeface mTypeface;

    public static final String DATE_DASH_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_nature);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
        title_txt = (TextView) findViewById(R.id.title_txt);
        list_nature =(ListView) findViewById(R.id.list_nature);
        textDate = (TextView) findViewById(R.id.textDate);
        buttonDate = (Button) findViewById(R.id.buttonDate);

        String title = getIntent().getStringExtra("gare");
        title_txt.setText(title);

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

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        String currentDateFullString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        textDate.setText(currentDateFullString);

        //convert the date (dd/mm/yyyy) to (yyyy-mm-dd)
        Log.e(" 0 Date String ", "DATA FIELD: " + currentDateString);
        try {
            Date date = new SimpleDateFormat( DATE_FORMAT , Locale.ENGLISH ).parse(currentDateString);
            DateFormat formatter = new SimpleDateFormat( DATE_DASH_FORMAT , Locale.getDefault() );
            String trueDate = formatter.format( date.getTime() );
            Log.e(" 2 Date String ", " - TRUE DATE: " + trueDate);

            title_txt.getText();
            Log.e("Title", "IT'S : " +  title_txt.getText());

            // HTTP REQUEST
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = String.format(Constant.URL3, modifyTextCity(),trueDate);
            //String url2 = Constant.URL.replace("%s", modifyTextCity());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String json) {
                            Log.e("volley", "onResponse: " + json);
                            parseJsonDate(json);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volley", "onErrorResponse" + error);

                    String json = new String(error.networkResponse.data);
                    parseJsonDate(json);
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
        String text = title_txt.getText().toString();
        text = text.replace(" ", "+");
        return text;
    }

    private void parseJsonDate(String json) {
        //Regeneration de la list  après chaque submit
        List<String> stringList = new ArrayList<>();
        //Liaison de GSON vers le modele: ApiObjects.
        ApiObject api = new Gson().fromJson(json, ApiObject.class);
        //remplissage de la liste en fonction du nombre de lignes
        for (int i = 0; i < api.getNhits(); i++) {
            stringList.add(" "+ (i+1) + ":   " + api.getRecords().get(i).getFields().getGc_obo_nature_c() );
        }

        // String myDate = (String)textDate.getText();
        // Log.e(" - Date String ", " - DATA FIELD: " + myDate);

        adapter = new  ArrayAdapter<String>( DetailsGare.this,android.R.layout.simple_list_item_1, stringList) {
            @Override
            public View getView (int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView (position,convertView,parent);
                // Set the typeface/font for the current item
                item.setTypeface(mTypeface);
                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#160A67"));
                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);
                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                // return the view
                return item;
            }

        };
        list_nature.setAdapter(adapter);

        list_nature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(DetailsGare.this);
                adb.setTitle("Nature de la perte : " + api.getRecords().get(position).getFields().getGc_obo_nature_c());
                adb.setMessage( " - Type d'objet : " + api.getRecords().get(position).getFields().getGc_obo_type_c() +
                        "\n - A la date du "+ api.getRecords().get(position).getFields().getDate() +
                        "\n - Localisation "+ api.getRecords().get(position).getFields().getGc_obo_gare_origine_r_name());
                //on indique que l'on veut le bouton ok à notre boite de dialogue
                adb.setPositiveButton("Ok", null);
                //on affiche la boite de dialogue
                adb.show();
            }
        });
    }
}
