package com.dat.signallabs.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.dat.signallabs.DialogBuilder;
import com.dat.signallabs.MainActivity;
import com.dat.signallabs.R;
import com.github.mikephil.charting.charts.LineChart;
import java.io.IOException;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

public class Lab1Activity extends AppCompatActivity {

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
            ArrayAdapter.createFromResource(this, R.array.lab4_diagramma_types,
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
        dialog = new MaterialDialog.Builder(this).title("Generated").build();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String saw = Helper.signalSaver(PrimitivesGenerator.getSaw(10.f, 5.f, 2.f, 128f));
        String angle = Helper.signalSaver(PrimitivesGenerator.getAngle(10.f, 5.f, 2.f, 128));
        String levels = Helper.signalSaver(PrimitivesGenerator.getLevels(10.f, 5.f, 2.f, 128f));
        editor.putString(MainActivity.KEY_SAW, saw);
        editor.putString(MainActivity.KEY_TRIANGULAR, angle);
        editor.putString(MainActivity.KEY_RECTANGULAR, levels);
        try {
            String cardio = Helper.signalSaver(
                PrimitivesGenerator.getFromAssets(getAssets().open("Cardio.txt")));
            String reo =
                Helper.signalSaver(PrimitivesGenerator.getFromAssets(getAssets().open("Reo.txt")));
            String velo =
                Helper.signalSaver(PrimitivesGenerator.getFromAssets(getAssets().open("Velo.txt")));
            editor.putString(MainActivity.KEY_CARDIO, cardio);
            editor.putString(MainActivity.KEY_REO, reo);
            editor.putString(MainActivity.KEY_VELO, velo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab_4, menu);
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
                dialog.setTitle("TRIANGULAR");
                dialog.setContent(
                    getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_TRIANGULAR, ""));
                dialog.show();
                break;
            case R.id.levels:
                dialog.setTitle("RECTANGULAR");
                dialog.setContent(
                    getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_RECTANGULAR, ""));
                dialog.show();
            case R.id.cardio:
                dialog.setTitle("CARDIO");
                dialog.setContent(
                    getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_CARDIO, ""));
                dialog.show();
                break;
            case R.id.reo:
                dialog.setTitle("REO");
                dialog.setContent(getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_REO, ""));
                dialog.show();
                break;
            case R.id.velo:
                dialog.setTitle("VELO");
                dialog.setContent(
                    getPreferences(MODE_PRIVATE).getString(MainActivity.KEY_VELO, ""));
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private MaterialDialog loadingDialog;

    @OnClick(R.id.drawGraph)
    protected void onDrawGraphClicked() {
        graph1.clear();
        graph2.clear();
        graph3.clear();

        final String diogramma = (String) spinner1.getSelectedItem();
        spinner2.setSelection(0);

        function.setText((String) spinner3.getSelectedItem());
        /*signals = Helper.doubleToComplex(
            Helper.stringToDoubles(getPreferences(MODE_PRIVATE).getString(diogramma, "")));*/

        final int selectedPos = spinner1.getSelectedItemPosition();

        loadingDialog = DialogBuilder.createSimpleProgressDialog(Lab1Activity.this);
        switch (spinner3.getSelectedItemPosition()) {
            case 0:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        loadingDialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            signals = Helper.doubleToComplex(PrimitivesGenerator.getFromAssets(
                                getAssets().open(diogramma + ".txt")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DPF = FourierTransform.DPF(signals);
                        fSignal = FourierTransform.ODPF(FourierTransform.filter(DPF, selectedPos));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        loadingDialog.dismiss();
                        graph1.clear();
                        graph2.clear();
                        graph3.clear();
                        Helper.drawSignal(graph1,
                            Helper.getSeries(Helper.complexToDouble(signals, 'r'),
                                FourierTransform.FREQUENCY));
                        Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(DPF, 'a'),
                            DPF.size() / FourierTransform.FREQUENCY));
                        Helper.drawSignal(graph3,
                            Helper.getSeries(Helper.complexToDouble(fSignal, 'a'),
                                fSignal.size() / FourierTransform.FREQUENCY));
                    }
                }.execute();
                break;

            case 1:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        loadingDialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            signals = Helper.doubleToComplex(PrimitivesGenerator.getFromAssets(
                                getAssets().open(diogramma + ".txt")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DPF = FourierTransform.DPF(signals);
                        fSignal = FourierTransform.BPF(signals, true);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        loadingDialog.dismiss();
                        graph1.clear();
                        graph2.clear();
                        graph3.clear();
                        Helper.drawSignal(graph1,
                            Helper.getSeries(Helper.complexToDouble(signals, 'r'),
                                FourierTransform.FREQUENCY));
                        Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(DPF, 'a'),
                            DPF.size() / FourierTransform.FREQUENCY));
                        Helper.drawSignal(graph3,
                            Helper.getSeries(Helper.complexToDouble(fSignal, 'a'),
                                fSignal.size() / FourierTransform.FREQUENCY));
                    }
                }.execute();
                break;

            case 2:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        loadingDialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            signals = Helper.doubleToComplex(PrimitivesGenerator.getFromAssets(
                                getAssets().open(diogramma + ".txt")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DPF = FourierTransform.DPF(signals);
                        fSignal = FourierTransform.BPFn(signals);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        loadingDialog.dismiss();
                        graph1.clear();
                        graph2.clear();
                        graph3.clear();
                        Helper.drawSignal(graph1,
                            Helper.getSeries(Helper.complexToDouble(signals, 'r'),
                                FourierTransform.FREQUENCY));
                        Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(DPF, 'a'),
                            DPF.size() / FourierTransform.FREQUENCY));
                        Helper.drawSignal(graph3,
                            Helper.getSeries(Helper.complexToDouble(fSignal, 'a'),
                                fSignal.size() / FourierTransform.FREQUENCY));
                    }
                }.execute();
                break;

            default:
                break;
        }
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
