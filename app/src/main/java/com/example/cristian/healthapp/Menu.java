package com.example.cristian.healthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    //Instanciar los objetos a utilizar
    Button botonGrabacion, botonAnalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Constructores de la actividad principal
        //Se les agrega el this para que se sepa a quien se esta refiriendo de la Activity
        botonGrabacion = (Button) findViewById(R.id.btnGrabacion);
        botonGrabacion.setOnClickListener(this);

        botonAnalizar = (Button) findViewById(R.id.btnAnalizar);
        botonAnalizar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
        switch (v.getId()) {
            case R.id.btnGrabacion: {
                //Crear intent para iniciar la siguiente Activity
                Intent int1 = new Intent(this, SpeechtoText.class);
                //Empezar la siguiente activity
                startActivity(int1);
                break;
            }
            case R.id.btnAnalizar: {
                //Crear intent para iniciar la siguiente Activity
                Intent int2 = new Intent(this, AnalizarGrab.class);
                //Empezar la siguiente activity
                startActivity(int2);
                break;
            }
            }
        }
        //Lanza la excepcion
        catch(Exception e1){
            //Aqui se coloca diferente el mensaje de error
            //JOptionPane.showMessageDialog(null,"Error");
            Toast.makeText(Menu.this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
}

