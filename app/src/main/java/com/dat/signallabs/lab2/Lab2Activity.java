package com.dat.signallabs.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.dat.signallabs.MainActivity;
import com.dat.signallabs.R;
import com.dat.signallabs.lab1.Helper;
import com.dat.signallabs.lab1.Lab1Activity;
import com.dat.signallabs.lab1.PrimitivesGenerator;
import com.github.mikephil.charting.charts.LineChart;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

public class Lab2Activity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private MaterialDialog dialog;
    @Bind(R.id.spinner1)
    protected Spinner spinner1;
    @Bind(R.id.spinner2)
    protected Spinner spinner2;
    @Bind(R.id.spinner3)
    protected Spinner spinner3;

    @Bind(R.id.graph1)
    protected LineChart graph1;
    @Bind(R.id.graph2)
    protected LineChart graph2;
    @Bind(R.id.graph3)
    protected LineChart graph3;
    @Bind(R.id.graph4)
    protected LineChart graph4;

    private List<Complex> signals;

    public static void startActivity(Context context) {
        if (context instanceof Lab1Activity) {
            return;
        }
        Intent intent = new Intent(context, Lab2Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        generate();
        init();
    }

    private void generate() {

        String saw = Helper.signalSaver(PrimitivesGenerator.getSaw(10., 5., 2., 128));
        String angle = Helper.signalSaver(PrimitivesGenerator.getAngle(10., 5., 2., 128));
        String levels = Helper.signalSaver(PrimitivesGenerator.getLevels(10., 5., 2., 128));

        dialog = new MaterialDialog.Builder(this).title("Generated").build();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.KEY_SAW, saw);
        editor.putString(MainActivity.KEY_TRIANGULAR, angle);
        editor.putString(MainActivity.KEY_RECTANGULAR, levels);
        editor.apply();
    }

    private void init() {
        ArrayAdapter<CharSequence> adapter1 =
            ArrayAdapter.createFromResource(this, R.array.low_pass_filter_types,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 =
            ArrayAdapter.createFromResource(this, R.array.param_types,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter3 =
            ArrayAdapter.createFromResource(this, R.array.lab2_signal_types,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);


        graph1.setDescription(null);
        graph2.setDescription(null);
        graph3.setDescription(null);
        graph4.setDescription(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saw:
                dialog.setTitle("SAW");
                dialog.setContent(getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_SAW, ""));
                dialog.show();
                break;
            case R.id.angle:
                dialog.setTitle("ANGLE");
                dialog.setContent(
                    getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_TRIANGULAR, ""));
                dialog.show();
                break;
            case R.id.levels:
                dialog.setTitle("LEVELS");
                dialog.setContent(
                    getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_RECTANGULAR, ""));
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
