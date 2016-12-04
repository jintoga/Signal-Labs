package com.dat.signallabs.lab1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.dat.signallabs.R;

public class Lab1Activity extends AppCompatActivity {

    public static final String KEY_SAW = "SAW";
    public static final String KEY_SAW = "SAW";
    public static final String KEY_SAW = "SAW";

    public static void startActivity(Context context) {
        if (context instanceof Lab1Activity) {
            return;
        }
        Intent intent = new Intent(context, Lab1Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);
        String saw = Helper.signalSaver(PrimitivesGenerator.getSaw(10., 5., 2., 128), KEY_SAW);
        Log.d("TAG", saw);
    }
}
