package it.bestapp.paganino;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.ArrayList;

import it.bestapp.paganino.utility.db.bin.BustaPaga;


public class Chart extends ActionBarActivity {

    private Toolbar toolbar;
    private ArrayList<BustaPaga> list;
    private WebView wv;
    final Handler myHandler = new Handler();
    private String label = null;
    private String netto = null;
    private String tasse = null;
    private String extra = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_chart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        list =  getIntent().getParcelableArrayListExtra("BUSTE");


        wv = (WebView) findViewById(R.id.rangewv);
        final JSJavaBridge apiJS = new JSJavaBridge( getApplicationContext());
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(false);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(apiJS, "API_S");
        wv.loadUrl("file:///android_asset/www/Range.html");
    }

    public void elaoraBustaPaga(){
        label = "\"";
        netto = "";
        tasse = "";
        extra = "";
        boolean flag =false;

        for (BustaPaga bP : list){
            netto += String.valueOf(bP.getTotRit()) + ",";
            tasse += String.valueOf(bP.getNetto())  + ",";
            label += bP.getId()  + "\", \"";
            flag = true;
        }

        if (flag) {
            netto = netto.substring(0, netto.length() - 1);
            tasse = tasse.substring(0, tasse.length() - 1);
            label = label.substring(0, label.length() - 3);
        }
        netto = "[" + netto +"]";
        tasse = "[" + tasse +"]";
        label = "[" + label +"]";
    }



    public class JSJavaBridge {
        Context mContext;
        JSJavaBridge( Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void plot(){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    elaoraBustaPaga();
                    wv.loadUrl("javascript:showPlot( " + label + ", "+
                            "\'" + netto + "\', "+
                            "\'" + tasse + /*"\', "+
                                                    "\'" + extra + */
                            "\' )");
                }
            });
        }
    }



}
