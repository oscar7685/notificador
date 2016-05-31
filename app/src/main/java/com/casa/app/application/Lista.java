package com.casa.app.application;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Oscar on 23/05/2016.
 */
public class Lista extends AppCompatActivity {
    ListView lista;
    String[] nombres = {"Oscar", "Diana", "Lucero"};

    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        lista = (ListView)findViewById(R.id.listView);
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nombres);
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition     = position;
                // ListView Clicked item value
                String  itemValue    = (String) lista.getItemAtPosition(position);
                // Show Alert
                Toast.makeText(getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();

            }


        });
    }
}
