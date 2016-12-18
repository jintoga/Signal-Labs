package com.dat.signallabs.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.signallabs.R;
import com.jjoe64.graphview.GraphView;

public class Lab1Activity extends AppCompatActivity {

    public static final String KEY_SAW = "SAW";
    public static final String KEY_ANGLE = "ANGLE";
    public static final String KEY_LEVELS = "LEVELS";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.spinner1)
    protected Spinner spinner1;
    @Bind(R.id.spinner2)
    protected Spinner spinner2;
    @Bind(R.id.spinner3)
    protected Spinner spinner3;
    private MaterialDialog dialog;


    @Bind(R.id.function)
    protected TextView function;
    @Bind(R.id.graph1)
    protected GraphView graph1;
    @Bind(R.id.graph2)
    protected GraphView graph2;
    @Bind(R.id.graph3)
    protected GraphView graph3;

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
        initSpinners();
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> adapter1 =
            ArrayAdapter.createFromResource(this, R.array.diogramma_types,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 =
            ArrayAdapter.createFromResource(this, R.array.param_types,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 =
            ArrayAdapter.createFromResource(this, R.array.function_types,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saw:
                dialog.setTitle("SAW");
                dialog.setContent(getPreferences(MODE_PRIVATE).getString(KEY_SAW, ""));
                dialog.show();
                break;
            case R.id.angle:
                dialog.setTitle("ANGLE");
                dialog.setContent(getPreferences(MODE_PRIVATE).getString(KEY_ANGLE, ""));
                dialog.show();
                break;
            case R.id.levels:
                dialog.setTitle("LEVELS");
                dialog.setContent(getPreferences(MODE_PRIVATE).getString(KEY_LEVELS, ""));
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
