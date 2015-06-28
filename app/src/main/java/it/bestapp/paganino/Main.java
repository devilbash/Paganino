package it.bestapp.paganino;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;


import java.net.CookieHandler;
import java.net.CookieManager;

import it.bestapp.paganino.dialog.SelectPeriod;
import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.fragment.NavigationDrawerFragment;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.baseactivity.DriveActivity;
import it.bestapp.paganino.utility.setting.SettingsManager;
import it.bestapp.paganino.utility.thread.ThreadCud;


public class Main extends DriveActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int REQUEST_CODE_SETTINGS = 0;
    private SingletonParametersBridge singleton;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private SettingsManager settings;
    public  NavigationDrawerFragment mNavigationDrawerFragment;
    private HRConnect conn = null;
    private Lista  listaBP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Paganino");
            setSupportActionBar(toolbar);
        }

        singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings", this);

        CookieHandler.setDefault(new CookieManager());
        conn = new HRConnect();
        listaBP = new Lista();
        listaBP.setConn(conn);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, listaBP)
                .commit();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position+1){
            case 1:
                //cambio pswd
                break;
            case 2:
                (new ThreadCud(this, conn)).execute();
                break;
            case 3:
                (new SelectPeriod(this, listaBP, conn)).show();
                break;
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            Intent intent = new Intent(this, Impostazioni.class);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        driveActive(settings.isDrive());
        super.onResume();
    }

    /*@Override
    protected  void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("Lista", (ArrayList<Busta>) listaBP.getAdapter().getList());
    }
    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List<Busta>  list = savedInstanceState.getParcelableArrayList("Lista");
        listaBP.getAdapter().setList(list);
    }*/



}
