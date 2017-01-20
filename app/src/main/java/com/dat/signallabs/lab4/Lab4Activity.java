package com.dat.signallabs.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Lab4Activity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private MaterialDialog dialog;
    @Bind(R.id.spinner1)
    protected Spinner spinner1;
    @Bind(R.id.spinner2)
    protected Spinner spinner2;

    @Bind(R.id.graph1)
    protected LineChart graph1;
    @Bind(R.id.graph2)
    protected LineChart graph2;
    @Bind(R.id.graph3)
    protected LineChart graph3;
    @Bind(R.id.graph4)
    protected LineChart graph4;

    @Bind(R.id.editTextFrom)
    protected EditText editTextFrom;
    @Bind(R.id.editTextTo)
    protected EditText editTextTo;

    List<Float> signals;
    List<Float> fSignal;
    List<Float> rSignal;
    List<Float> aprox;
    private MaterialDialog loadingDialog;

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
            ArrayAdapter.createFromResource(this, R.array.lab4_function_types,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        graph1.setDescription(null);
        graph2.setDescription(null);
        graph3.setDescription(null);
        graph4.setDescription(null);
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

    @OnClick(R.id.drawGraph)
    protected void onDrawGraphClicked() {

        graph1.clear();
        graph2.clear();
        graph3.clear();
        graph4.clear();

        final int from = Integer.parseInt(editTextFrom.getText().toString());
        final int to = Integer.parseInt(editTextTo.getText().toString());

        final String diogramma = (String) spinner1.getSelectedItem();
        try {
            signals = PrimitivesGenerator.getFromAssets(getAssets().open(diogramma + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadingDialog = DialogBuilder.createSimpleProgressDialog(Lab4Activity.this);

        final int spinner1SelectedPos = spinner1.getSelectedItemPosition();
        switch (spinner2.getSelectedItemPosition()) {
            case 0:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        loadingDialog.show();
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        fSignal = UolshAdamarHelper.getUolshTransform(signals, false);
                        aprox = UolshAdamarHelper.getAprox(fSignal, from, to);
                        if (spinner1SelectedPos > 2) {
                            rSignal = UolshAdamarHelper.getUolshTransform(aprox, true);
                        } else {
                            rSignal = UolshAdamarHelper.getUolshTransform(
                                FourierTransform.filter(fSignal, spinner1SelectedPos), true);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        loadingDialog.dismiss();
                        graph1.clear();
                        graph2.clear();
                        graph3.clear();
                        graph4.clear();
                        Helper.drawSignal(graph1,
                            Helper.getSeries(signals, FourierTransform.FREQUENCY), "Оригинал");
                        Helper.drawSignal(graph3,
                            Helper.getSeries(UolshAdamarHelper.getAmplitude(fSignal), 1),
                            "Амплитуда");
                        Helper.drawSignal(graph2,
                            Helper.getSeries(rSignal, FourierTransform.FREQUENCY), "Обратное");
                        Helper.drawSignal(graph4,
                            Helper.getSeries(UolshAdamarHelper.getPhase(fSignal), 1), "Фаза");
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
                        fSignal = UolshAdamarHelper.getAdamarTransform(signals, false);
                        aprox = UolshAdamarHelper.getAprox(fSignal, from, to);
                        if (spinner1SelectedPos > 2) {
                            rSignal = UolshAdamarHelper.getAdamarTransform(aprox, true);
                        } else {
                            rSignal = UolshAdamarHelper.getAdamarTransform(
                                FourierTransform.filter(fSignal, spinner1SelectedPos), true);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        loadingDialog.dismiss();
                        graph1.clear();
                        graph2.clear();
                        graph3.clear();
                        graph4.clear();
                        Helper.drawSignal(graph1,
                            Helper.getSeries(signals, FourierTransform.FREQUENCY), "Оригинал");
                        Helper.drawSignal(graph3,
                            Helper.getSeries(UolshAdamarHelper.getAmplitude(fSignal), 1),
                            "Амплитуда");
                        Helper.drawSignal(graph2,
                            Helper.getSeries(rSignal, FourierTransform.FREQUENCY), "Обратное");
                        Helper.drawSignal(graph4,
                            Helper.getSeries(UolshAdamarHelper.getPhase(fSignal), 1), "Фаза");
                    }
                }.execute();
                break;
            default:
                break;
        }
    }
}
