package com.example.colorpicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

//    AlertDialog builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorPickerDialog dialog = new ColorPickerDialog(this);


        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());


    }
}