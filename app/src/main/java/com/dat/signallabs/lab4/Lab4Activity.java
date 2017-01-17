package com.dat.signallabs.lab4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.signallabs.R;

public class Lab4Activity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private MaterialDialog dialog;
    @Bind(R.id.spinner1)
    protected Spinner spinner1;
    @Bind(R.id.spinner2)
    protected Spinner spinner2;

    @Bind(R.id.editTextFrom)
    protected EditText editTextFrom;
    @Bind(R.id.editTextTo)
    protected EditText editTextTo;

    public static void startActivity(Context context) {
        if (context instanceof Lab4Activity) {
            return;
        }
        Intent intent = new Intent(context, Lab4Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.drawGraph)
    protected void onDrawGraphClicked() {

    }
}
