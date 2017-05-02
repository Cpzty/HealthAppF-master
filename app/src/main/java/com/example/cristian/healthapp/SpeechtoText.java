package com.example.cristian.healthapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

//import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;

public class SpeechtoText extends AppCompatActivity  {



    private  TextView resultTEXT;
    private TextView watsonResp;
    private String text;
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
    }

    public void onButtonClick(View v){

        if(v.getId() == R.id.imageButton){


            promptSpeechInput();

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
                ArrayList<String>result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTEXT.setText(result.get(0));

            }
                break;
        }
    }




}

