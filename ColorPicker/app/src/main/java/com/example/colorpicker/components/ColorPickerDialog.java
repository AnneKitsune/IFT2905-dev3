package com.example.colorpicker.components;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.example.colorpicker.R;
import com.example.colorpicker.components.AreaPicker;
import com.example.colorpicker.components.SaturationValueGradient;
import com.example.colorpicker.utils.ColorUtils;

public class ColorPickerDialog extends AlertDialog {
    private final static int MAX_RGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100;
    private final static int MAX_H_VALUE = 360;

    public interface OnColorPickedListener{
        void onColorPicked(@ColorInt int color);
    }

    private AreaPicker seekSV;
    private SaturationValueGradient saturationValueGradient;

    private SeekBar seekBarH;
    private SeekBar seekBarR;
    private SeekBar seekBarG;
    private SeekBar seekBarB;

    // Représentation/stockage interne de la couleur présentement sélectionnée par le Dialog.
    private int r, g, b = 0;

    private OnColorPickedListener onColorPickedListener;


    public ColorPickerDialog(Context context) {
        super(context);
        init(context);
    }

    public ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }


    private void init(Context context){
        /* CETTE MÉTHODE DEVRA ÊTRE MODIFIÉE */

        // Initialize dialog
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_picker, null);
        setView(v);

        // Initialize SV gradient
        seekSV = v.findViewById(R.id.seekSV);
        seekSV.setMaxX(MAX_SV_VALUE);
        seekSV.setMaxY(MAX_SV_VALUE);
        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);
        seekSV.setOnPickedListener(new AreaPicker.OnPickedListener() {
            @Override
            public void onPicked(AreaPicker areaPicker, int x, int y, boolean fromUser) {
                if(fromUser){
                    int[] rgb = ColorUtils.HSVtoRGB(seekBarH.getProgress(), x, MAX_SV_VALUE - y);
                    r = rgb[0];
                    g = rgb[1];
                    b = rgb[2];
                    updateRGB();
                }
            }
        });

        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        saturationValueGradient.setColor(Color.RED);

        // Default color
        setColor(getContext().getColor(R.color.defaultColor));

        setTitle(getContext().getString(R.string.pick_color));
        setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(onColorPickedListener != null)
                    onColorPickedListener.onColorPicked(getColor());
            }
        });
        setButton(AlertDialog.BUTTON_NEGATIVE, getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Source: https://stackoverflow.com/questions/4342757/how-to-make-a-color-gradient-in-a-seekbar?fbclid=IwAR0lYWflv2H9bGI5WR0ibqwmx-X5m9iCRliuyB_JoSK81Zur8ESh5bV5iEM
        seekBarH = v.findViewById(R.id.seekH);
        int[] rainbowColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED};
        setGradientColors(rainbowColors);

        seekBarH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int[] rgb = ColorUtils.HSVtoRGB(seekBar.getProgress(), MAX_SV_VALUE, MAX_SV_VALUE);
                    r = rgb[0];
                    g = rgb[1];
                    b = rgb[2];
                    updateRGB();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarR = v.findViewById(R.id.seekR);
        seekBarG = v.findViewById(R.id.seekG);
        seekBarB = v.findViewById(R.id.seekB);

        seekBarR.setMax(MAX_RGB_VALUE);
        seekBarG.setMax(MAX_RGB_VALUE);
        seekBarB.setMax(MAX_RGB_VALUE);

        seekBarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                r = seekBar.getProgress();
                updateHSV();
            }
        });

        seekBarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                g = seekBar.getProgress();
                updateHSV();
            }
        });

        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                b = seekBar.getProgress();
                updateHSV();
            }
        });



    }

    private void updateHSV() {
        int[] hsv = ColorUtils.RGBtoHSV(r, g, b);
        saturationValueGradient.setColor(Color.rgb(r,g,b));
        seekBarH.setProgress(hsv[0]);
        seekSV.setPickedX(hsv[1]);
        seekSV.setPickedX(hsv[2]);
    }

    private void updateRGB() {
        seekBarR.setProgress(r);
        seekBarG.setProgress(g);
        seekBarB.setProgress(b);

        // Ignore SV values for the gradient. Otherwise it will become darker as we move the cursor.
        int[] hsv = ColorUtils.RGBtoHSV(r,g,b);
        int[] rgb = ColorUtils.HSVtoRGB(hsv[0], MAX_SV_VALUE, MAX_SV_VALUE);
        saturationValueGradient.setColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
    }

    private void setGradientColors(int[] rainbowColors) {
        LinearGradient linearGradient =
                new LinearGradient(0,0,900,0, rainbowColors, null, Shader.TileMode.CLAMP);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(linearGradient);

        seekBarH.setMax(MAX_H_VALUE);
        seekBarH.setProgressDrawable(shapeDrawable);
    }

    @ColorInt int getColor(){
        return Color.rgb(r,g,b);
    }

    private void setColor(@ColorInt int newColor){
        r = Color.red(newColor);
        g = Color.green(newColor);
        b = Color.blue(newColor);
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        this.onColorPickedListener = onColorPickedListener;
    }
}
