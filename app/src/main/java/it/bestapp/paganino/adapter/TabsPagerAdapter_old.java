package it.bestapp.paganino.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


import it.bestapp.paganino.Charts;
import it.bestapp.paganino.R;
import it.bestapp.paganino.utility.db.bin.Busta;

public class TabsPagerAdapter_old extends PagerAdapter {
    private final Activity act;
    final Handler myHandler = new Handler();
    private WebView wv;
    private String[] titoli;
    private Busta busta;

    public TabsPagerAdapter_old(Activity a) {
        act = a;
        titoli = a.getResources().getStringArray(R.array.TITOLIPAGER);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titoli[position];
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view = act.getLayoutInflater().inflate(R.layout.fra_chart, container, false);
        container.addView(view);
        wv = (WebView) view.findViewById(R.id.wview);

        String page = titoli[position];
        page = page.replace(' ', '_');

       // busta = ((Charts) act).getBusta();

        final JSJavaBridge apiJS = new JSJavaBridge( act.getApplicationContext());
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(false);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.addJavascriptInterface(apiJS, "API");
        wv.loadUrl("file:///android_asset/www/" + page +".html");

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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


