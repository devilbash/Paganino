package it.bestapp.paganino;


import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonFlat;
//import com.gc.materialdesign.views.Switch;
import it.bestapp.paganino.view.Switch;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import it.bestapp.paganino.dialog.OpenDir;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.setting.SettingsManager;


public class Impostazioni extends ActionBarActivity
        implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private SettingsManager settings;
    private Impostazioni _this;
    public static final String TIMEPICKER_TAG = "timepicker";

    private int day;

    private String ora;
    private TextView oraV;
    private TextView giornoV;
    private ButtonFlat noti;
    private ButtonFlat time;
    private ButtonFlat date;
    private LinearLayout riga;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);

        final Calendar calendar = Calendar.getInstance();
        _this = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Paganino");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SingletonParametersBridge singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings");


/*card notifica*/
        final Switch swNoti = (Switch ) findViewById(R.id.swNoti);
        riga = (LinearLayout) findViewById(R.id.subriga);


        noti = (ButtonFlat) findViewById(R.id.notifica);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settings.isNotifica()){
                    swNoti.setChecked(false);
                    settings.setNotifica(false);
                    riga.setVisibility(View.GONE);
                }else{
                    swNoti.setChecked(true);
                    settings.setNotifica(true);
                    riga.setVisibility(View.VISIBLE);
                }
            }
        });
        swNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        swNoti.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                settings.setNotifica(b);
                if (b) {
                    riga.setVisibility(View.VISIBLE);
                } else {
                    riga.setVisibility(View.GONE);
                }
            }
        });
        swNoti.setChecked(settings.isNotifica());
        if (settings.isNotifica()){
            riga.setVisibility(View.VISIBLE);
        }else{
            riga.setVisibility(View.GONE);
        }





        giornoV = (TextView) findViewById(R.id.giorno);
        giornoV.setText( ((Integer)settings.getGiorno()).toString());

        date = (ButtonFlat) findViewById(R.id.notificaDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogWithoutDateField().show();
            }
        });
        day = settings.getGiorno();


        ora = settings.getOra();
        time = (ButtonFlat) findViewById(R.id.notificaTime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oraMinuti[] = ora.split(":");
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        _this,
                        Integer.parseInt(oraMinuti[0]),
                        Integer.parseInt(oraMinuti[1]),
                        false,
                        false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });
        oraV = (TextView) findViewById(R.id.ora);
        oraV.setText(ora);



/*sync*/
        Switch swSync = (Switch) findViewById(R.id.setSync);
        swSync.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                settings.setSync(b);
            }
        });
        swSync.setChecked(settings.isSync());

/*drive*/
        Switch swDrive = (Switch) findViewById(R.id.setDrive);
        swDrive.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                settings.setDrive(b);
            }
        });
        swDrive.setChecked(settings.isDrive());



/*salvataggio file*/
        ButtonFlat setpath = (ButtonFlat) findViewById(R.id.setpath);
        setpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OpenDir(_this).show();
            }
        });
        TextView pathV = (TextView) findViewById(R.id.path);
        pathV.setText(settings.getPath());

        ButtonFlat esci = (ButtonFlat) findViewById(R.id.esci);
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setUser("");
                settings.setPaswd("");
                settings.setRicordami(false);
            }
        });
    }


    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i2) {
        DateFormat format = new SimpleDateFormat("kkmm");
        Date time = null;

        String oreminuti = "";
        if (i<10)
            oreminuti = "0";
        oreminuti = oreminuti.concat(((Integer) (i * 100 + i2)).toString());

        try {
            time = format.parse(oreminuti);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("kk:mm");
        ora = format.format(time);
        settings.setOra(ora);
        oraV.setText(ora);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        settings.setGiorno(day);
        giornoV.setText(((Integer)day).toString());
    }

    private DatePickerDialog createDialogWithoutDateField() {
        Calendar cal = Calendar.getInstance();
        day = settings.getGiorno();
        DatePickerDialog dpd = new DatePickerDialog(_this, _this, cal.get(cal.YEAR), cal.get(cal.MONTH), day);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mYearSpinner".equals(datePickerField.getName()) ||
                                "mMonthSpinner".equals(datePickerField.getName())  ) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex) {
        }
        return dpd;
    }
}

