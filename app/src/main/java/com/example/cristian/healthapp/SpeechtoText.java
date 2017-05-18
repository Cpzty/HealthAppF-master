package com.example.cristian.healthapp;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

//import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;

public class SpeechtoText extends AppCompatActivity  {


    public  TextView resultTEXT;
    ArrayList<String> result;
    String respuestaEnfermedad;
    String traslado;
    private TextView watsonResp;
    private String text;
    private Button btnResponse;
    JsonParser par = new JsonParser();

    // private Conversation service;
    NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
            NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
            //"e47f7720-a590-4c09-a8bd-b4d8f1edc7e7",
            //"Xqo4KfXFnutA"
            "e431d06f-091e-45df-b7b0-eb80ade5de83",
           "XZ4lshXtyhuR"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechto_text);

        resultTEXT =  (TextView)findViewById(R.id.TVresult);
        btnResponse = (Button)findViewById(R.id.btnAnalizar);
    }


    private class CallWatson extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            //Autenticacion de Watson:
            service.setEndPoint("https://gateway.watsonplatform.net/natural-language-understanding/api");
            EntitiesOptions entities = new EntitiesOptions.Builder().sentiment(true).limit(1).model("20:b02adc9f-da6d-4c05-80eb-d17ff0fabbd9").build();
            Features features = new Features.Builder().entities(entities).build();
            AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(result.get(0)).features(features).build();
            AnalysisResults results = service.analyze(parameters).execute();
            String x = results.toString();
            JSONObject c;
            JSONArray consulta=null;
            try {
                c = new JSONObject(x);
                consulta = c.getJSONArray("entities");
                String resultadosFinales="";
                for (int i =0;i<consulta.length();i++){
                    if (i ==0){
                        resultadosFinales = consulta.getJSONObject(i).getString("text");
                    }else if (i==(consulta.length()-1)){
                        resultadosFinales = resultadosFinales+" y " + consulta.getJSONObject(i).getString("text");
                    }else{
                        resultadosFinales = resultadosFinales+", " + consulta.getJSONObject(i).getString("text");
                    }
                }
                return x;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            respuestaEnfermedad = s;
            resultTEXT.setText("El diagnostico es: "+s);
            Log.d("respuesta", respuestaEnfermedad);
        }
    }

    public void promptSpeechInput(){

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo");

        try {
            startActivityForResult(i, 100);
        }catch (ActivityNotFoundException a){
            Toast.makeText(SpeechtoText.this,"No es posible en este dispositivo!",Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code,result_code,i);

        switch (request_code){
            case 100: if(result_code== RESULT_OK && i != null){
                result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTEXT.setText(result.get(0));
                traslado = result.get(0);

//                new CallWatson().execute();

            }
                break;
        }
    }

    public void onButtonClick(View v){

        if(v.getId() == R.id.imageButton){


            promptSpeechInput();

        }

        if(v.getId()==R.id.btnAnalyze){
            new CallWatson().execute();

        }
        if(v.getId()==R.id.btnguardar){
            String normalized = Normalizer.normalize(traslado, Normalizer.Form.NFD);
            normalized.replaceAll("[^\\p{ASCII}]", "");
            if(normalized.contains("´")==true){
                normalized.replaceAll("´","");


            }

            



            Intent int1 = new Intent(SpeechtoText.this,AnalizarGrab.class);
            int1.putExtra("enfermedad",normalized);
            Log.d("traslado", traslado);
            int1.putExtra("muestra","cancer");
            startActivity(int1);
        }
    }




}
