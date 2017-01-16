package com.dat.signallabs.lab2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.signallabs.DialogBuilder;
import com.dat.signallabs.MainActivity;
import com.dat.signallabs.R;
import com.dat.signallabs.lab1.FourierTransform;
import com.dat.signallabs.lab1.PrimitivesGenerator;
import com.github.mikephil.charting.charts.LineChart;
import java.io.IOException;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

public class Lab2Activity extends AppCompatActivity {

    private static final String KEY_FILE_PATH = "FILE_PATH";
    private static final int PICK_AUDIO_REQUEST_CODE = 123;
    private static final String TAG = Lab2Activity.class.getName();
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

    @Bind(R.id.checkBoxHP)
    protected CheckBox checkBoxHP;
    @Bind(R.id.editTextNValue)
    protected EditText editTextNValue;

    private List<Complex> signals;
    private List<Complex> fSignal;
    private List<Complex> ampl;
    private List<Complex> lAmpl;
    private Filter filter;
    private SoundStream ss;
    private Uri fileURI;
    String amplitud = "Амплитудно-частотная характеристика";
    String lAmplitud = "Log10 Амплитудно-частотная характеристика";

    private MaterialDialog loadingDialog;

    public static void startActivity(Context context) {
        if (context instanceof Lab2Activity) {
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
                if (signals != null) {
                    updateGraphics(position);
                }
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

    private void updateGraphics(int position) {
        graph2.clear();
        graph3.clear();
        graph4.clear();
        final String diogramma = (String) spinner1.getSelectedItem();
        switch (position) {
            case 0:
                Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(fSignal, 'a'), 1),
                    diogramma);
                Helper.drawSignal(graph4, Helper.getSeries(Helper.complexToDouble(ampl, 'a'), 1),
                    lAmplitud);
                Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(lAmpl, 'a'), 1),
                    amplitud);
                break;

            case 1:
                Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(fSignal, 'r'), 1),
                    diogramma);
                Helper.drawSignal(graph4, Helper.getSeries(Helper.complexToDouble(ampl, 'r'), 1),
                    lAmplitud);
                Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(lAmpl, 'r'), 1),
                    amplitud);
                break;

            case 2:
                Helper.drawSignal(graph2, Helper.getSeries(Helper.complexToDouble(fSignal, 'i'), 1),
                    diogramma);
                Helper.drawSignal(graph4, Helper.getSeries(Helper.complexToDouble(ampl, 'i'), 1),
                    lAmplitud);
                Helper.drawSignal(graph3, Helper.getSeries(Helper.complexToDouble(lAmpl, 'i'), 1),
                    amplitud);
                break;
        }
    }

    @OnClick(R.id.drawGraph)
    protected void onDrawGraphClicked() {
        graph1.clear();
        graph2.clear();
        graph3.clear();
        graph4.clear();

        final String diogramma = (String) spinner1.getSelectedItem();
        spinner2.setSelection(0);
        ss = new SoundStream();

        final int hpVal = Integer.valueOf(editTextNValue.getText().toString());
        final boolean hpIsChecked = checkBoxHP.isChecked();
        final String funcName = (String) spinner1.getSelectedItem();

        loadingDialog = DialogBuilder.createSimpleProgressDialog(Lab2Activity.this);

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
                        signals = Helper.getIdealSignal(5000);
                        filter = new Filter(ss.getGc(), hpVal, signals);
                        fSignal = filter.getY(funcName, hpIsChecked);
                        ampl = filter.getAmplitude();
                        lAmpl = filter.getLogAmplitude();
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
                            Helper.getSeries(Helper.complexToDouble(signals, 'r'),
                                FourierTransform.FREQUENCY), "Оригинал");
                        Helper.drawSignal(graph2,
                            Helper.getSeries(Helper.complexToDouble(fSignal, 'r'), 1), diogramma);
                        Helper.drawSignal(graph4,
                            Helper.getSeries(Helper.complexToDouble(ampl, 'r'), 1), lAmplitud);
                        Helper.drawSignal(graph3,
                            Helper.getSeries(Helper.complexToDouble(lAmpl, 'r'), 1), amplitud);
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
                        signals = Helper.genSawSignal();
                        filter = new Filter(ss.getGc(), hpVal, signals);
                        fSignal = filter.getY(funcName, hpIsChecked);
                        ampl = filter.getAmplitude();
                        lAmpl = filter.getLogAmplitude();
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
                            Helper.getSeries(Helper.complexToDouble(signals, 'r'),
                                FourierTransform.FREQUENCY), "Оригинал");
                        Helper.drawSignal(graph2,
                            Helper.getSeries(Helper.complexToDouble(fSignal, 'r'), 1), diogramma);
                        Helper.drawSignal(graph4,
                            Helper.getSeries(Helper.complexToDouble(ampl, 'r'), 1), lAmplitud);
                        Helper.drawSignal(graph3,
                            Helper.getSeries(Helper.complexToDouble(lAmpl, 'r'), 1), amplitud);
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
                            signals =
                                Helper.doubleToComplex(ss.loadSignal(fileURI, Lab2Activity.this));
                            filter = new Filter(ss.getGc(), hpVal, signals);
                            fSignal = filter.getY(funcName, hpIsChecked);
                            ampl = filter.getAmplitude();
                            lAmpl = filter.getLogAmplitude();
                        } catch (IOException e2) {
                            Toast.makeText(Lab2Activity.this, e2.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
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
                            Helper.getSeries(Helper.complexToDouble(signals, 'r'),
                                FourierTransform.FREQUENCY), "Оригинал");
                        Helper.drawSignal(graph2,
                            Helper.getSeries(Helper.complexToDouble(fSignal, 'r'), 1), diogramma);
                        Helper.drawSignal(graph4,
                            Helper.getSeries(Helper.complexToDouble(ampl, 'r'), 1), lAmplitud);
                        Helper.drawSignal(graph3,
                            Helper.getSeries(Helper.complexToDouble(lAmpl, 'r'), 1), amplitud);

                        try {
                            ss.saveSignal(Helper.complexToDouble(fSignal, 'r'), "result.wav");
                            Toast.makeText(Lab2Activity.this, "File result.wav saved",
                                Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
                break;

            default:
                break;
        }
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

                openFileBrowser();
                dialog.setTitle("LEVELS");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFileBrowser() {
        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("audio/*");
        try {
            startActivityForResult(fileintent, PICK_AUDIO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "No activity can handle picking a file. Showing alternatives.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_AUDIO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            fileURI = data.getData();
            Uri uri = data.getData();
            String path = uri.getPath();
            Log.e(TAG, "File's path: " + path);
            SharedPreferences sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_FILE_PATH, uri.toString());
            editor.apply();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
