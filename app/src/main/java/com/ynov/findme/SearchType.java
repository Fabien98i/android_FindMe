
package com.ynov.findme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.ynov.findme.models.ApiObject;
import com.ynov.findme.models.Records;
import com.ynov.findme.utils.Constant;
import com.ynov.findme.utils.FastDialog;
import com.ynov.findme.utils.Network;

import java.util.ArrayList;
import java.util.List;
import android.util.TypedValue;
import android.widget.Toolbar;

public class  SearchType extends AppCompatActivity {

    private EditText editTextCity;
    private ListView listViewType;
    //Création de la ArrayList qui nous permettra de remplir la listView
    //private List<String> stringList = new ArrayList<>();

    //Declaration de l'adaptateur
    private ArrayAdapter <String> adapter ;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type);

        editTextCity = (EditText) findViewById(R.id.editTextCity);
        listViewType = (ListView) findViewById(R.id.listViewType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_map) {
            Intent map_gare = new Intent(SearchType.this, MapsActivity.class);
            startActivity(map_gare);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public String modifyTextCity () {
        String text = editTextCity.getText().toString();
        text = text.replace(" ", "+");
        return text;
    }

    //SUBMIT : boutton OK
    public void submit (View view) {
        if (editTextCity.getText().toString().isEmpty()) {
            FastDialog.showDialog(SearchType.this,
                    FastDialog.SIMPLE_DIALOG, "Vous devez renseigner un nom de gare");
            return;
        }

        if (!Network.isNetworkAvailable(SearchType.this)) {
            FastDialog.showDialog(SearchType.this,
                    FastDialog.SIMPLE_DIALOG,
                    "Vous devez etre connecté");
            return;
        }

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

    private void parseJson(String json) {
        //Regeneration de la list  après chaque submit
        List<String> stringList = new ArrayList<>();
        //Liaison de GSON vers le modele: ApiObjects.
        ApiObject api = new Gson().fromJson(json, ApiObject.class);
        //remplissage de la liste en fonction du nombre de lignes
        for (int i = 0; i < api.getParameters().getRows(); i++) {
            stringList.add("n°"+ i + ":   " + api.getRecords().get(i).getFields().getGc_obo_nature_c() );
        }

         adapter = new  ArrayAdapter<String>( SearchType.this,android.R.layout.simple_list_item_1, stringList) {
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
        listViewType.setAdapter(adapter);

        listViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(SearchType.this);
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


//Log.e("API", "i" + i + " nature : " + api.getRecords().get(i).getFields().getGc_obo_nature_c());
//Log.e("API", "id" + api.getRecords().get(1).getFields().getGc_obo_nature_c());
//Log.e("Api", "nbhits : " + api.getNhits());
//Log.e ("parametes", " rows and format"  + api.getParameters().getRows() + api.getParameters().getFormat() );
