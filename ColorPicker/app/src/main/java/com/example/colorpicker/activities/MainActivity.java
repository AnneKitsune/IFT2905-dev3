package com.example.colorpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.colorpicker.R;
import com.example.colorpicker.components.ColorPickerDialog;

public class MainActivity extends AppCompatActivity {
    private View picked_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
