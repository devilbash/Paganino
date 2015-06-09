package it.bestapp.paganino.dialog;


import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.bestapp.paganino.Main;
import it.bestapp.paganino.R;
import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.thread.ThreadStoreController;
import it.bestapp.paganino.view.seekbar.RangeSeekBar;


public class SelectPeriod {

    private MaterialDialog dialog;
    private Main act;
    private HRConnect conn;
    private RangeSeekBar<Integer> rangeSeekBar;
    private TextView anno1;
    private TextView anno2;
    private TextView mese1;
    private TextView mese2;
    private final List<Busta> lista;
    private List<String> mesi;
    private Integer first;
    private Integer last;

    public SelectPeriod(Main m,Lista lF,HRConnect c){
        this.act  =  m;
        this.conn = c;
        mesi = Arrays.asList(act.getResources().getStringArray(R.array.MESI));

        lista = lF.getAdapter().getList();
        first = 1;
        last  = lista.size();

        MaterialDialog.Builder dBuilder =
                new MaterialDialog.Builder(act)
                        .title("Confronto")
                        .customView(R.layout.dia_period, true)
                        .positiveText("OK")
                        .negativeText("Annulla")
                        .cancelable(false)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                ArrayList<Busta> range = componiLista();
                                (new ThreadStoreController(act, range, conn, 'R')).execute();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        });
        dialog = dBuilder.build();
        View view = dialog.getView();

        anno1 = (TextView) view.findViewById(R.id.anno1);
        anno2 = (TextView) view.findViewById(R.id.anno2);
        mese1 = (TextView) view.findViewById(R.id.mese1);
        mese2 = (TextView) view.findViewById(R.id.mese2);


        rangeSeekBar = (RangeSeekBar<Integer>) view.findViewById(R.id.seekbar);
        rangeSeekBar.setRangeValues(1, lista.size());

        anno2.setText(((Integer) (lista.get(0)).getNumAnno()).toString());
        mese2.setText((lista.get(0)).getMese(act));
        anno1.setText(((Integer) (lista.get(lista.size() - 1).getNumAnno())).toString());
        mese1.setText((lista.get(lista.size()-1)).getMese(act));


        rangeSeekBar = (RangeSeekBar<Integer>) view.findViewById(R.id.seekbar);
        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
/*                first = minValue;
                last  = maxValue;

                minValue = Math.abs(lista.size() - minValue +1);
                maxValue = Math.abs(lista.size() - maxValue +1);

                anno1.setText(((Integer) (lista.get(minValue-1 ).getNumAnno())).toString());
                mese1.setText((lista.get(minValue-1 )).getMese(act));
                anno2.setText(((Integer) (lista.get(maxValue-1)).getNumAnno()).toString());
                mese2.setText((lista.get(maxValue-1)).getMese(act));*/

                first = minValue;
                last  = maxValue;

                minValue = Math.abs(lista.size() - minValue);
                maxValue = Math.abs(lista.size() - maxValue);

                anno1.setText(((Integer) (lista.get(minValue ).getNumAnno())).toString());
                mese1.setText((lista.get(minValue )).getMese(act));
                anno2.setText(((Integer) (lista.get(maxValue)).getNumAnno()).toString());
                mese2.setText((lista.get(maxValue)).getMese(act));

            }
        });
    }

    private ArrayList<Busta> componiLista( ) {
        ArrayList<Busta> range = new ArrayList<Busta>();

        int r1 = lista.size() - last ;
        int r2  = lista.size() - first;

       for(Busta b; r1<=r2; r1++){
           b= lista.get(r1);
           range.add(b);
       }
        Collections.reverse(range);
        return range;
    }


    public void show(){
        dialog.show();
    }


}



