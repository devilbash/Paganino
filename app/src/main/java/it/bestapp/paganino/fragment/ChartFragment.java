package it.bestapp.paganino.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import it.bestapp.paganino.R;
import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.utility.db.bin.BustaPaga;
import it.bestapp.paganino.utility.db.bin.Ore;
import it.bestapp.paganino.view.MyWebView;

/**
 * Created by marco.compostella on 26/04/2015.
 */
public class ChartFragment extends Fragment {
    final Handler myHandler = new Handler();
    private MyWebView wv;
    private int position;
    private String page;
    private JSJavaBridge apiJS;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static ChartFragment newInstance(int position) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 1;
        BustaPaga busta = null;
        Activity act = getActivity();
        Bundle extras = act.getIntent().getExtras();
        if (extras != null) {
            busta = (BustaPaga) extras.getParcelable("BUSTA");
        }
        String [] titoli = act.getResources().getStringArray(R.array.TITOLIPAGER);
        page = titoli[position];
        page = page.replace(' ', '_');
        if ( position > 0 ) {
            page = "Ore";
        }
        String tipo="";
        switch (position){
            case 1:
                tipo="F";
                break;
            case 2:
                tipo="R";
                break;
            case 3:
                tipo="B";
                break;
        }
        apiJS = new JSJavaBridge(busta, busta.getOre().get(tipo), position);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate a new layout from our resources
        View view = inflater.inflate(R.layout.fra_chart, container, false);
        wv = (MyWebView) view.findViewById(R.id.wview);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(false);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(apiJS, "API");
        wv.loadUrl("file:///android_asset/www/" + page + ".html");
        return view;
    }

    public class JSJavaBridge {
        int position;
        BustaPaga busta;
        Ore ore;
        JSJavaBridge( BustaPaga b,Ore o, int p) {
            busta = b;
            ore = o;
            position = p;
        }
        @JavascriptInterface
        public void plotStipendio(){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    String jsonStr = getStipendio(busta);
                 /*           "[ "+ busta.getPagaBase() + " , " +
                                    + busta.getSuperMin() + " , " +
                                    + busta.getTotComp()  + " , " +
                                    + busta.getTotRit()   + " , " +
                                    + busta.getNetto()    + " ]" ; */
                    wv.loadUrl("javascript:showPlot( \'" + jsonStr + "\' )");
                }
            });
        }

        @JavascriptInterface
        public void plotOre(){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    String jsonStr = getOre(ore);
                    /*            + ora.getRes()  + " , " +
                                + ora.getSpe()  + " , " +
                                + ora.getGod()   ;*/
                    wv.loadUrl("javascript:showPlot( " + jsonStr + " )");
                }
            });
        }
    }

    public String getStipendio(BustaPaga busta){
        String jsonStr =
                "[ "+ busta.getPagaBase() + " , " +
                        + busta.getSuperMin() + " , " +
                        + busta.getTotComp()  + " , " +
                        + busta.getTotRit()   + " , " +
                        + busta.getNetto()    + " ]" ;
        return jsonStr;
    }
    public String getOre(Ore ora){
        String jsonStr =
                + ora.getRes()  + " , " +
                        + ora.getSpe()  + " , " +
                        + ora.getGod()   ;
        return jsonStr;
    }
}
