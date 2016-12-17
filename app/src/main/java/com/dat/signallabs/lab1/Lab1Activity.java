package com.dat.signallabs.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.signallabs.R;

public class Lab1Activity extends AppCompatActivity {

    public static final String KEY_SAW = "SAW";
    public static final String KEY_ANGLE = "ANGLE";
    public static final String KEY_LEVELS = "LEVELS";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private MaterialDialog dialog;

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
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        generate();
    }

    private void generate() {

        String saw = Helper.signalSaver(PrimitivesGenerator.getSaw(10., 5., 2., 128));
        String angle = Helper.signalSaver(PrimitivesGenerator.getAngle(10., 5., 2., 128));
        String levels = Helper.signalSaver(PrimitivesGenerator.getLevels(10., 5., 2., 128));

        dialog = new MaterialDialog.Builder(this).title("Generated").build();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SAW, saw);
        editor.putString(KEY_ANGLE, angle);
        editor.putString(KEY_LEVELS, levels);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lab_1, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter =
            ArrayAdapter.createFromResource(this, R.array.lab1_toolbar_item,
                R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        dialog.setTitle("SAW");
                        dialog.setContent(getPreferences(MODE_PRIVATE).getString(KEY_SAW, ""));
                        dialog.show();
                        break;
                    case 1:
                        dialog.setTitle("ANGLE");
                        dialog.setContent(getPreferences(MODE_PRIVATE).getString(KEY_ANGLE, ""));
                        dialog.show();
                        break;
                    case 2:
                        dialog.setTitle("LEVELS");
                        dialog.setContent(getPreferences(MODE_PRIVATE).getString(KEY_LEVELS, ""));
                        dialog.show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }
}
