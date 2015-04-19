package it.bestapp.paganino.adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import it.bestapp.paganino.R;



public class TabsPagerAdapter extends PagerAdapter {
    private final Activity act;
    final Handler myHandler = new Handler();
    private WebView wv;


    public TabsPagerAdapter(Activity a) {
        act = a;
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
        String title ="";

        switch (position) {
            case 0:
                title ="Stipendio";
                break;
            case 1:
                title ="Ferie";
                break;
            case 2:
                title ="ROL";
                break;
            case 3:
                title ="Banca ORE";
                break;
        }
        return title;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view = act.getLayoutInflater().inflate(R.layout.pgr_chart, container, false);
        container.addView(view);


        wv = (WebView) view.findViewById(R.id.wview);


        final JSJavaBridge apiJS
                = new JSJavaBridge( act.getApplicationContext());
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(apiJS, "API");
        wv.loadUrl("file:///android_asset/www/paga.html");


        // Return the View
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
        public void showPlot(){
            myHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
        @JavascriptInterface
        public void showDialog(String str, String perc){
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }



}


