package it.bestapp.paganino.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Locale;

import it.bestapp.paganino.Charts;
import it.bestapp.paganino.R;
import it.bestapp.paganino.fragment.ChartFragment;

/**
 * Created by marco.compostella on 26/04/2015.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private Charts charts;
    private ChartFragment[] frags;
    private ViewPager viewPager;

    public TabsPagerAdapter(Charts c, FragmentManager fm) {
        super(fm);
        this.charts = c;
        frags = new ChartFragment[4];
    }

    @Override
    public Fragment getItem(int index) {
        ChartFragment frag;
        if (frags[index] != null) {
            frag = frags[index];
        } else {
            frag = frags[index] = ChartFragment.newInstance(index);
        }
        return frag;
    }

    @Override
    public int getCount() {
        return frags.length;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        String [] titoli = charts.getResources().getStringArray(R.array.TITOLIPAGER);

        switch (position) {
            case 0:
                return titoli[0].toUpperCase(l);
            case 1:
                return titoli[1].toUpperCase(l);
            case 2:
                return titoli[2].toUpperCase(l);
            case 3:
                return titoli[3].toUpperCase(l);
        }
        return null;
    }
}
