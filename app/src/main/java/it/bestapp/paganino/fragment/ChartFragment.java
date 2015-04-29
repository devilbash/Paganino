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
import it.bestapp.paganino.utility.db.bin.Busta;

/**
 * Created by marco.compostella on 26/04/2015.
 */
public class ChartFragment extends Fragment {
    final Handler myHandler = new Handler();
    private WebView wv;
    private int position;
    private Busta busta;
    private String page;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static ChartFragment newInstance(int position) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ChartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 1;

        Activity act = getActivity();
        Bundle extras = act.getIntent().getExtras();
        if (extras != null) {
            busta = (Busta) extras.getParcelable("BUSTA");
        }
        String [] titoli = act.getResources().getStringArray(R.array.TITOLIPAGER);
        page = titoli[position];
        page = page.replace(' ', '_');
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate a new layout from our resources
        View view = inflater.inflate(R.layout.fra_chart, container, false);

        wv = (WebView) view.findViewById(R.id.wview);
        final JSJavaBridge apiJS = new JSJavaBridge( getActivity().getApplicationContext());
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(false);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.addJavascriptInterface(apiJS, "API");
        wv.loadUrl("file:///android_asset/www/" + page +".html");

        return view;
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
    }



}
