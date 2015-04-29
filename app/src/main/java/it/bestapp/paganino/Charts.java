package it.bestapp.paganino;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import it.bestapp.paganino.adapter.TabsPagerAdapter;
import it.bestapp.paganino.utility.db.bin.Busta;
import it.bestapp.paganino.view.tabsliding.SlidingTabLayout;


public class Charts extends ActionBarActivity
                implements ActionBar.TabListener
{

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
   // private Busta busta;
    private SlidingTabLayout tabs;


    final Handler myHandler = new Handler();
    private WebView wv;

    TabsPagerAdapter mSectionsPagerAdapter;


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

        tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        tabs.setDistributeEvenly(true);
        // Setting Custom Color for the Scroll bar indicator of the Tab View

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);




        tabs.setViewPager(mViewPager);


    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
