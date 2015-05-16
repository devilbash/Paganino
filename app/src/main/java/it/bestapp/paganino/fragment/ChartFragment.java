package it.bestapp.paganino.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import it.bestapp.paganino.R;
import it.bestapp.paganino.utility.db.bin.BustaPaga;
import it.bestapp.paganino.utility.db.bin.Ore;

/**
 * Created by marco.compostella on 26/04/2015.
 */
public class ChartFragment extends Fragment {
    final Handler myHandler = new Handler();
    private WebView wv;
    private int position;
    private BustaPaga busta;
    private Ore ora;
    private String page;

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

        ora = busta.getOre().get(tipo);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate a new layout from our resources
        View view = inflater.inflate(R.layout.fra_chart, container, false);

        wv = (WebView) view.findViewById(R.id.wview);
        final JSJavaBridge apiJS = new JSJavaBridge(busta, position);
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

        JSJavaBridge( BustaPaga b, int p) {
            busta = b;
            position = p;

        }
        @JavascriptInterface
        public void plotStipendio(){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    String jsonStr =
                            "[ "+ busta.getPagaBase() + " , " +
                                    + busta.getSuperMin() + " , " +
                                    + busta.getTotComp()  + " , " +
                                    + busta.getTotRit()   + " , " +
                                    + busta.getNetto()    + " ]" ;
                    wv.loadUrl("javascript:showPlot( \'" + jsonStr + "\' )");
                }
            });
        }

        @JavascriptInterface
        public void plotOre(){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    String jsonStr =
                                + ora.getRes()  + " , " +
                                + ora.getSpe()  + " , " +
                                + ora.getGod()   ;
                    wv.loadUrl("javascript:showPlot( \'" + jsonStr + "\' )");
                }
            });
        }

    }
}
