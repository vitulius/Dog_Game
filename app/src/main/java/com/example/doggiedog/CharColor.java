package com.example.doggiedog;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CharColor extends AppCompatActivity {

    int Color;
    Button pick;
    Button like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_color);

        setAppLocale("en");
        Color = ContextCompat.getColor(CharColor.this,R.color.colorPrimary);
        pick = (Button) findViewById(R.id.colorpickbtn);
        like = (Button) findViewById(R.id.ilikethatbtn);

    }

    public void opencolorpicker() {
        AmbilWarnaDialog colorpicker = new AmbilWarnaDialog(this, Color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Color = color;
            }
        });
        colorpicker.show();
    }

    //this method makes the system language set to english instead of hebrew for example to debug the color picker.
    private void setAppLocale(String localecode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localecode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }

    // color pick button.
    public void PickAColor(View view) {
        opencolorpicker();
    }


    // user accepts the final color.
    public void ILikeThat(View view) {
    }
}
