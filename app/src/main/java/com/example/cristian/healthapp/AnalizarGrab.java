package com.example.cristian.healthapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AnalizarGrab extends AppCompatActivity {

    TextView keyE, nameE, resultE, getEnfermedad;
    String key, name, varEnfermedad;
    int contador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analizar_grab);

        keyE = (TextView) findViewById(R.id.txtName);
        nameE = (TextView) findViewById(R.id.txtEnfermedad);
        resultE = (TextView) findViewById(R.id.txtResultado);
        getEnfermedad = (TextView) findViewById(R.id.TVresult);
        resultE.setVisibility(View.INVISIBLE);
        nameE.setVisibility(View.INVISIBLE);
        contador=0;
        Bundle bundle;
        SpeechtoText trasladar = new SpeechtoText();
        bundle = getIntent().getExtras();


//        String varEnfermedad = "cancer";
        if(bundle.getString("enfermedad")!=null) {
            varEnfermedad = bundle.getString("enfermedad");
        }
        else{
//            varEnfermedad = "cancer";
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("diagnostico", varEnfermedad);
        client.get("http://uvgproyectos.esaludgt.org/web/Api/Codigos?", params, new  JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                keyE.setText("No hay contenido");
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONObject jsonobject = null;
                    try {
                        resultE.setVisibility(View.VISIBLE);
                        jsonobject = response;
                        key = jsonobject.getString("Key");
                        Log.d("key", key);
                        name = jsonobject.getString("Value");
                        Log.d("name", name);
                        nameE.setVisibility(View.VISIBLE);
                        nameE.setText(name);
                        keyE.setText(key);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

        });

    }
}
