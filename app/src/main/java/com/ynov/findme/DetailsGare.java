package com.ynov.findme;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsGare extends AppCompatActivity {
    TextView title_txt;
    ListView list_nature;
    private ArrayAdapter <String> adapter ;
    private Typeface mTypeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_nature);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
        title_txt = (TextView) findViewById(R.id.title_txt);
        list_nature =(ListView) findViewById(R.id.list_nature);

        String title = getIntent().getStringExtra("gare");
        Log.e("TITLE", "gare : "+title);
        title_txt.setText(title);

        //REQUETTE HTTP
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format(Constant.URL, modifyTextCity());
        String url2 = Constant.URL.replace("%s", modifyTextCity());


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
    }

    public String modifyTextCity () {
        String text = title_txt.getText().toString();
        text = text.replace(" ", "+");
        return text;
    }

    private void parseJson(String json) {
        //Regeneration de la list  après chaque submit
        List<String> stringList = new ArrayList<>();
        //Liaison de GSON vers le modele: ApiObjects.
        ApiObject api = new Gson().fromJson(json, ApiObject.class);
        //remplissage de la liste en fonction du nombre de lignes
        for (int i = 0; i < api.getParameters().getRows(); i++) {
            stringList.add("n°"+ i + ":   " + api.getRecords().get(i).getFields().getGc_obo_nature_c() );
        }

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
