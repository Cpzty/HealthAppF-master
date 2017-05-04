package com.example.cristian.healthapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;

import java.util.ArrayList;
import java.util.Locale;

//import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;

public class SpeechtoText extends AppCompatActivity {

    ArrayList<String> result;


    private TextView resultTEXT;
    private TextView watsonResp;
    private String text;
    private Button btnResponse;
    // private Conversation service;
    NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
            NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
            "e431d06f-091e-45df-b7b0-eb80ade5de83",
            "XZ4lshXtyhuR"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechto_text);
        // Set up the Watson Q&A Service
//                service = new Conversation();
//        service.setClientId("username", "password");
//        service.set(QuestionAndAnswerDataset.TRAVEL);
        resultTEXT =  (TextView)findViewById(R.id.TVresult);
        btnResponse = (Button)findViewById(R.id.btnAnalizar);
    }

    private class Llamada extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                    NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                    "e431d06f-091e-45df-b7b0-eb80ade5de83",
                    "XZ4lshXtyhuR"
            );
            service.setEndPoint("https://gateway.watsonplatform.net/natural-language-undesrtanding/api");
            EntitiesOptions entities = new EntitiesOptions.Builder().sentiment(true).limit(1).model("20:f080ec47-b6e0-4cbe-96e4-fd1d98e11e54").build();
            Features features = new Features.Builder().entities(entities).build();
            AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                    .text(result.get(0))
                    .features(features)
                    .build();
            AnalysisResults results = service.analyze(parameters).execute();
            return results.toString();
        }

        @Override
        protected void onPostExecute(String st){
            resultTEXT.setText(st);
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

    public void onButtonClick(View v){

        if(v.getId() == R.id.imageButton){


            promptSpeechInput();

        }

        if(v.getId()==R.id.btnAnalyze){
            class Llamada extends AsyncTask<Void, Void, String>{
                @Override
                protected String doInBackground(Void... params) {
                    NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                            NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                            "e431d06f-091e-45df-b7b0-eb80ade5de83",
                            "XZ4lshXtyhuR"
                    );
                    service.setEndPoint("https://gateway.watsonplatform.net/natural-language-undesrtanding/api");
                    EntitiesOptions entities = new EntitiesOptions.Builder().sentiment(true).limit(1).build();
                    Features features = new Features.Builder().entities(entities).build();
                    AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                            .text(result.get(0))
                            .features(features)
                            .build();
                    AnalysisResults results = service.analyze(parameters).execute();
                    return results.toString();
                }

                @Override
                protected void onPostExecute(String st){
                    resultTEXT.setText(st);
                }
        }

        }

    }

    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code,result_code,i);

        switch (request_code){
            case 100: if(result_code== RESULT_OK && i != null){
                ArrayList<String>result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTEXT.setText(result.get(0));
                new Llamada().execute();

            }
                break;
        }
    }

}
