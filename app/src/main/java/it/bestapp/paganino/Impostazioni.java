package it.bestapp.paganino;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.gc.materialdesign.views.Switch;

import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.setting.SettingsManager;




public class Impostazioni extends ActionBarActivity {


    private Activity act;
    private SettingsManager settings;
    private Impostazioni _this;
    private Toolbar toolbar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);

        _this = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Paganino");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        SingletonParametersBridge singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings");

        Switch swNoti = (Switch) findViewById(R.id.switch1);
        swNoti.setOncheckListener( new Switch.OnCheckListener(){
            @Override
            public void onCheck(boolean b) {
                settings.setNotifiche(b);
            }
        });
        swNoti.setChecked(settings.isNotifiche());

        Switch swTimer = (Switch) findViewById(R.id.switch2);
        swTimer.setOncheckListener( new Switch.OnCheckListener(){
            @Override
            public void onCheck(boolean b) {
                settings.setTimer(b);
            }
        });
        swTimer.setChecked(settings.isTimer());

        Switch swAttivo = (Switch) findViewById(R.id.switch3);
        swAttivo.setOncheckListener( new Switch.OnCheckListener(){
            @Override
            public void onCheck(boolean b) {
                settings.setAttivo(b);
            }
        });
        swAttivo.setChecked(settings.isAttivo());
/*
        final FragmentManager fm = getSupportFragmentManager();
        TextView login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog alertdLogin = new LoginDialog();
                alertdLogin.show(fm,"");
            }
        });*/

        /*
        TextView disconnetti = (TextView) rootView.findViewById(R.id.disconnetti);
        disconnetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setSync(false);
                Intent intent =new Intent( _this, Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });*/

    }

}
