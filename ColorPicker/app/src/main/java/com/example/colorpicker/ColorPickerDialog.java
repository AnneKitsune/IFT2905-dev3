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
    private int r, g, b;

    // ajouté par Raouf
    private OnColorPickedListener onColorPickedListener;

    private ColorPickerCallback callback;

    private View picked_color;


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


    // ajouté par Raouf
    public interface ColorPickerCallback{

        void onColorSelected(@ColorInt int color);
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

        // Exemple pour afficher un gradient SV centré sur du rouge pur.
        saturationValueGradient.setColor(Color.RED);


        // Default color
        setColor(getContext().getColor(R.color.defaultColor));



        // Coded by Hojun
        setTitle("Chosir une couleur");
        setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                setOnColorPickedListener(onColorPickedlistener);
            }
        });
        setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

// je regarde cette link :
// https://stackoverflow.com/questions/4342757/how-to-make-a-color-gradient-in-a-seekbar?fbclid=IwAR0lYWflv2H9bGI5WR0ibqwmx-X5m9iCRliuyB_JoSK81Zur8ESh5bV5iEM
        int[] rainbowColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED};
        LinearGradient linearGradient =
                new LinearGradient(0,0,900,0, rainbowColors, null, Shader.TileMode.CLAMP);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(linearGradient);

        seekBarH = v.findViewById(R.id.seekH);
        seekBarH.setMax(MAX_H_VALUE);
        seekBarH.setProgressDrawable(shapeDrawable);

        seekBarH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int[] hsv = RGBtoHSV(r, g, b);
                hsv[0] = progress;
                System.out.println("r,g,b = " + r + "," + g +"," + b);
                System.out.println("h,s,v = " + hsv[0] + "," + hsv[1] +"," + hsv[2]);


                saturationValueGradient.setColor(Color.rgb(r,g,b));



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // ajouté par Raouf
        picked_color = v.findViewById(R.id.picked_color);

        seekBarR = v.findViewById(R.id.seekR);
        seekBarG = v.findViewById(R.id.seekG);
        seekBarB = v.findViewById(R.id.seekB);

        seekBarR.setMax(MAX_RGB_VALUE);
        seekBarG.setMax(MAX_RGB_VALUE);
        seekBarB.setMax(MAX_RGB_VALUE);

        seekBarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                saturationValueGradient.setColor(Color.rgb(r, g, b));
                System.out.println("r,g,b = " + r + "," + g +"," + b);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        seekBarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                saturationValueGradient.setColor(Color.rgb(r, g, b));
                System.out.println("r,g,b = " + r + "," + g +"," + b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                saturationValueGradient.setColor(Color.rgb(r, g, b));
                System.out.println("r,g,b = " + r + "," + g +"," + b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });



    }

    // ajouté par Raouf
    public void setCallback(ColorPickerCallback listener){callback = listener; }



    @ColorInt int getColor(){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit retourner la couleur présentement sélectionnée par le dialog.
         * */
        return Color.rgb(r,g,b);
    }

    public void setColor(@ColorInt int newColor){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit mettre à jour l'état du dialog pour que la couleur sélectionnée
         * corresponde à "newColor".
         * */
        r = Color.red(newColor);
        g = Color.green(newColor);
        b = Color.blue(newColor);
    }

    static private int[] HSVtoRGB(int hue, int saturation, int val){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit convertir un trio de valeurs HSV à un trio de valeurs RGB
         * */

        float h = hue / 60;
        float s = saturation / 100;
        float v = val / 100;

        float c = s * v;
        float delta = v - c;

        float h_mod_2_minus_1 = (h % 2) - 1;
        if (h_mod_2_minus_1 < 0) {
            h_mod_2_minus_1 = -(h_mod_2_minus_1);
        }

        float x = 1 - h_mod_2_minus_1;

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

        int r = (int) (255 * (c * r_+ delta));
        int g = (int) (255 * (c * g_ + delta));
        int b = (int) (255 * (c * b_ + delta));
        int[] rgb = {r,g,b};
        return rgb;
    }

    static private int[] RGBtoHSV(int r, int g, int b){
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit convertir un trio de valeurs RGB à un trio de valeurs HSV
         * */
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
            h_ = (g - b) / delta;
        } else if(c_max == g) {
            h_ = 2 + (b - r) /delta;
        } else if(c_max == b) {
            h_ = 4 + (r - g) /delta;
        }

        int h = 0;
        int s = 0;
        int v = 0;

        if(h_ >= 0) {
            h = (int) (60 * h_);
        } else if(h_ < 0) {
            h = (int) (60 * (h_ + 6));
        }

        s = 100 * (delta / c_max);
        v = 100 * (c_max / 255);

        int[] hsv = {h, s, v};

        return hsv;
    }

    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        /* IMPLÉMENTER CETTE MÉTHODE
         * Elle doit enregistrer un "OnColorPickedListener", qui sera appelé, éventuellement,
         * lorsque le bouton "ok" du dialog sera cliqué.
         * */

        //ajouté par Raouf
        this.onColorPickedListener = onColorPickedListener;



    }

    public interface OnColorPickedListener{
        void onColorPicked(ColorPickerDialog colorPickerDialog, @ColorInt int color);
    }
}
