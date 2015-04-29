package it.bestapp.paganino.view.swipelist;

import android.view.View;

import it.bestapp.paganino.R;
import it.bestapp.paganino.adapter.bustapaga.BustaPaga;
import it.bestapp.paganino.adapter.bustapaga.BustaPagaAdapter;
import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.thread.ThreadPDF;

/**
 * Created by marco.compostella on 11/04/2015.
 */
public class BaseSwipeListViewListener implements SwipeListViewListener {

    private Lista frag;
    private HRConnect conn;
    private BustaPagaAdapter bpAdapter;
    private SwipeListView swpLstView;

    public BaseSwipeListViewListener(Lista f, SwipeListView list, HRConnect conn, BustaPagaAdapter adapter) {
        this.frag       = f;
        this.conn       = conn;
        this.bpAdapter  = adapter;
        this.swpLstView = list;
    }

    @Override
    public void onOpened(int position, boolean toRight) {
        BustaPaga bp= bpAdapter.getItem(position);
        if (toRight)
            (new ThreadPDF(frag, conn, bp, 'V')).execute();
    }

    @Override
    public void onClosed(int position, boolean fromRight) {
    }

    @Override
    public void onListChanged() {
    }

    @Override
    public void onMove(int position, float x) {
    }

    @Override
    public void onStartOpen(int position, int action, boolean right) {
        View main = swpLstView.getChildAt(position);
        View element;

        if (main ==null) return;
        if (right){    // apre file
            element =  main.findViewById(R.id.backsx);
            element.setVisibility(View.GONE);

            element = main.findViewById(R.id.backdx);
            element.setVisibility(View.VISIBLE);
        }else {        //vede pulsanti
            element = main.findViewById(R.id.backdx);
            element.setVisibility(View.GONE);

            element =  main.findViewById(R.id.backsx);
            element.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStartClose(int position, boolean right) {
    }

    @Override
    public void onClickFrontView(int position) {
    }

    @Override
    public void onClickBackView(int position) {
    }

    @Override
    public void onDismiss(int[] reverseSortedPositions) {
    }

    @Override
    public int onChangeSwipeMode(int position) {
        return SwipeListView.SWIPE_MODE_DEFAULT;
    }

    @Override
    public void onChoiceChanged(int position, boolean selected) {
    }

    @Override
    public void onChoiceStarted() {
    }

    @Override
    public void onChoiceEnded() {
    }

    @Override
    public void onFirstListItem() {
    }

    @Override
    public void onLastListItem() {
    }
}