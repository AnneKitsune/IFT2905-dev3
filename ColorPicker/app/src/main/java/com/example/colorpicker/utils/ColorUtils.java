package com.example.colorpicker.utils;

public final class ColorUtils {

    public static int[] HSVtoRGB(int hue, int saturation, int val){
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

    public static int[] RGBtoHSV(int r, int g, int b){
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
}
