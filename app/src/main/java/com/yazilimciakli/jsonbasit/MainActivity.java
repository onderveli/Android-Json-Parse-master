package com.yazilimciakli.jsonbasit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnGetir;
    ListView listKelimeler;
    String url = "http://yazilimciakli.com/_mehmet/json/data.php";
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetir = (Button) findViewById(R.id.btnGetir);
        listKelimeler = (ListView) findViewById(R.id.listKelimeler);
        btnGetir.setOnClickListener(this);
        editText=(EditText) findViewById(R.id.editText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetir:


                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        final List<Kelime> kelimeler = Arrays.asList(gson.fromJson(response, Kelime[].class));
                        final ArrayAdapter<Kelime> kelimelerAdapter = new ArrayAdapter<Kelime>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, kelimeler);
                        listKelimeler.setAdapter(kelimelerAdapter);
                        listKelimeler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AlertDialog.Builder dialogOlusturucu=new AlertDialog.Builder(MainActivity.this);
                                dialogOlusturucu.setMessage("Türkçesi:"+kelimelerAdapter.getItem(position).getTr()
                                +"\n"+"İngilizcesi:"+kelimelerAdapter.getItem(position).getEn())
                                        .setCancelable(false)
                                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                dialogOlusturucu.create().show();

                            }
                        });
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                // When user changed the Text
                                kelimelerAdapter.getFilter().filter(cs);
                                listKelimeler.setAdapter(kelimelerAdapter);
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                          int arg3) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Bağlantı sırasında hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue.add(request);


                break;
        }

    }
}