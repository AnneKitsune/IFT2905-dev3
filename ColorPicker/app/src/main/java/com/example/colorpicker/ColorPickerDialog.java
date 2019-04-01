package com.example.colorpicker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.colorpicker.Views.AreaPicker;

class ColorPickerDialog extends AlertDialog {
    private final static int MAX_RGB_VALUE = 255;
    private final static int MAX_SV_VALUE = 100;
    private final static int MAX_H_VALUE = 360;

    private AreaPicker seekSV;
    private SaturationValueGradient saturationValueGradient;


    // Coded by Hojun
    SeekBar seekBarH;
    SeekBar seekBarR;
    SeekBar seekBarG;
    SeekBar seekBarB;

    // Représentation/stockage interne de la couleur présentement sélectionnée par le Dialog.
    private int r, g, b = 0;

    // ajouté par Raouf
    private OnColorPickedListener onColorPickedListener;


    ColorPickerDialog(Context context) {
        super(context);
        init(context);
    }

    ColorPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    ColorPickerDialog(Context context, int themeResId) {
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
        saturationValueGradient = new SaturationValueGradient();
        seekSV.setInsetDrawable(saturationValueGradient);
        seekSV.setOnPickedListener(new AreaPicker.OnPickedListener() {
            @Override
            public void onPicked(AreaPicker areaPicker, int x, int y, boolean fromUser) {
                int[] rgb = HSVtoRGB(seekBarH.getProgress(), x, y);
                r = rgb[0];
                g = rgb[1];
                b = rgb[2];
                updateGradients();
            }
        });

        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        saturationValueGradient.setColor(Color.BLACK);


        // Default color
        setColor(getContext().getColor(R.color.defaultColor));

        // Coded by Hojun
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

        // je regarde cette link :
        // https://stackoverflow.com/questions/4342757/how-to-make-a-color-gradient-in-a-seekbar?fbclid=IwAR0lYWflv2H9bGI5WR0ibqwmx-X5m9iCRliuyB_JoSK81Zur8ESh5bV5iEM
        seekBarH = v.findViewById(R.id.seekH);
        int[] rainbowColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED};
        setGradientColors(rainbowColors);

        seekBarH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int[] rgb = HSVtoRGB(seekBar.getProgress(), seekSV.getPickedX(), seekSV.getPickedY());
                r = rgb[0];
                g = rgb[1];
                b = rgb[2];
                System.out.println("ryys "+seekBar.getProgress()+","+seekSV.getPickedX()+" rgb "+r+","+g+","+b);
                updateGradients();
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
                updateGradients();
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
                updateGradients();
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
                updateGradients();
            }
        });



    }

    public void updateGradients() {
        int[] hsv = RGBtoHSV(r, g, b);
        System.out.println("Updating gradient. rgb = "+r+","+g+","+b+","+hsv[0]+","+hsv[1]+","+hsv[2]);
        saturationValueGradient.setColor(Color.rgb(r,g,b));
        seekBarH.setProgress(hsv[0]);
        seekSV.setPickedX(hsv[1]);
        seekSV.setPickedX(hsv[2]);
        seekBarR.setProgress(r);
        seekBarG.setProgress(g);
        seekBarB.setProgress(b);
    }

    public void setGradientColors(int[] rainbowColors) {
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

    public void setColor(@ColorInt int newColor){
        r = Color.red(newColor);
        g = Color.green(newColor);
        b = Color.blue(newColor);
    }

    static private int[] HSVtoRGB(int hue, int saturation, int val){
        float h = hue / 60f;
        float s = saturation / 100f;
        float v = val / 100f;

        float c = s * v;
        float delta = v - c;

        float x = 1 - Math.abs((h % 2) - 1);

        float r_ = 0;
        float g_ = 0;
        float b_ = 0;

        if(0 <= h && h <= 1) {
            r_ =  1; g_ = x; b_ = 0;
        } else if(1 < h && h <= 2) {
            r_ =  x; g_ = 1; b_ = 0;
        } else if(2 < h && h <= 3) {
            r_ =  0; g_ = 1; b_ = x;
        } else if(3 < h && h <= 4) {
            r_ =  0; g_ = x; b_ = 1;
        } else if(4 < h && h <= 5) {
            r_ =  x; g_ = 0; b_ = 1;
        } else if(5 < h && h <= 6) {
            r_ =  1; g_ = 0; b_ = x;
        }

        int r = (int) (255f * (c * r_+ delta));
        int g = (int) (255f * (c * g_ + delta));
        int b = (int) (255f * (c * b_ + delta));
        int[] rgb = {r,g,b};
        return rgb;
    }

    static private int[] RGBtoHSV(int r, int g, int b){
        int c_max = Math.max(Math.max(r, g), b);
        int c_min = Math.min(Math.min(r, g), b);
        int delta = c_max - c_min;
        float h_ = 0;

        if(delta == 0){
            delta = 1;
        }
        if(c_max == 0) {
            c_max = 1;
        }


        if(c_max == r) {
            h_ = (g - b) / (float)delta;
        } else if(c_max == g) {
            h_ = 2 + (b - r) / (float)delta;
        } else if(c_max == b) {
            h_ = 4 + (r - g) / (float)delta;
        }

        int h = 0;
        int s = 0;
        int v = 0;

        if(h_ >= 0) {
            h = (int) (60 * h_);
        } else if(h_ < 0) {
            h = (int) (60 * (h_ + 6));
        }

        s = (int)(100 * ((float) delta / c_max));
        v = (int)(100 * (c_max / 255.0f));

        int[] hsv = {h, s, v};

        return hsv;
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        //ajouté par Raouf
        this.onColorPickedListener = onColorPickedListener;
    }

    public interface OnColorPickedListener{
        void onColorPicked(@ColorInt int color);
    }
}
