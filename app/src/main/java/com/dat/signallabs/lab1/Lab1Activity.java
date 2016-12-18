package com.dat.signallabs.lab1;

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
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.signallabs.R;
import com.github.mikephil.charting.charts.LineChart;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

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
    protected LineChart graph1;
    @Bind(R.id.graph2)
    protected LineChart graph2;
    @Bind(R.id.graph3)
    protected LineChart graph3;

    private List<Complex> signals;
    private List<Complex> DPF;
    private List<Complex> fSignal;

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
        init();
    }

    private void init() {
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
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (signals != null) {
                    updateGraphics(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter3 =
            ArrayAdapter.createFromResource(this, R.array.function_types,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);


        graph1.setDescription(null);
        graph2.setDescription(null);
        graph3.setDescription(null);
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

    @OnClick(R.id.drawGraph)
    protected void onDrawGraphClicked() {
        graph1.clear();
        graph2.clear();
        graph3.clear();

        String diogramma = (String) spinner1.getSelectedItem();
        spinner2.setSelection(0);

        function.setText((String) spinner3.getSelectedItem());
        signals = Helper.doubleToComplex(
            Helper.stringToDoubles(getPreferences(MODE_PRIVATE).getString(diogramma, "")));
        DPF = FourierTransform.DPF(signals);

        switch (spinner3.getSelectedItemPosition()) {
            case 0:
                fSignal = FourierTransform.ODPF(
                    FourierTransform.filter(DPF, spinner1.getSelectedItemPosition()));
                break;

            case 1:
                fSignal = FourierTransform.BPF(signals, true);
                break;

            case 2:
                fSignal = FourierTransform.BPFn(signals);
                break;

            default:
                break;
        }

        Helper.drawSignal(graph1,
            Helper.getSeries(Helper.complexToDouble(signals, 'r'), FourierTransform.FREQUENCY));
        Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(DPF, 'a'),
            DPF.size() / FourierTransform.FREQUENCY));
        Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(fSignal, 'a'),
            fSignal.size() / FourierTransform.FREQUENCY));
    }

    private void updateGraphics(int position) {
        graph2.clear();
        graph3.clear();
        switch (position) {
            case 0:
                Helper.drawSignal(graph2,
                    Helper.getSeries(Helper.complexToDouble(DPF, 'a'), FourierTransform.FREQUENCY));
                Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(fSignal, 'a'),
                    FourierTransform.FREQUENCY));
                break;

            case 1:
                Helper.drawSignal(graph2,
                    Helper.getSeries(Helper.complexToDouble(DPF, 'r'), FourierTransform.FREQUENCY));
                Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(fSignal, 'r'),
                    FourierTransform.FREQUENCY));
                break;

            case 2:
                Helper.drawSignal(graph2,
                    Helper.getSeries(Helper.complexToDouble(DPF, 'i'), FourierTransform.FREQUENCY));
                Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(fSignal, 'i'),
                    FourierTransform.FREQUENCY));
                break;
        }
    }
}
