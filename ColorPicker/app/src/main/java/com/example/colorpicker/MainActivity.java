package com.example.colorpicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

//    AlertDialog builder;
    private View picked_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picked_color = findViewById(R.id.picked_color);

        ColorPickerDialog dialog = new ColorPickerDialog(this);
        dialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color) {
                picked_color.setBackgroundColor(color);
            }
        });


        findViewById(R.id.button_pick).setOnClickListener((View v) -> dialog.show());


    }
}
