package it.bestapp.paganino.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import it.bestapp.paganino.R;

/**
 * Created by marco.compostella on 27/03/2015.
 */
public class FragLista extends Fragment implements Frag{
    private Activity act;
    private ListView grpView;


    public void setActivity(Activity a){
        act = a;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_lista, container, false);




        return rootView;
    }




}
