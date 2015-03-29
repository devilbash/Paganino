package it.bestapp.paganino.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.gc.materialdesign.views.ProgressBarIndeterminate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import it.bestapp.paganino.Main;
import it.bestapp.paganino.R;
import it.bestapp.paganino.fragment.adapter.BustaPaga;
import it.bestapp.paganino.fragment.adapter.BustaPagaAdapter;
import it.bestapp.paganino.fragment.adapter.Info;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.connessione.thread.ThreadHome;
import it.bestapp.paganino.utility.db.DataBaseAdapter;


public class Lista extends Fragment implements PageDownloadedInterface {
    private Activity act;
    private ListView grpView;

    private List<Info> list;

    private static final int REQUEST_CODE_SETTINGS = 0;

    private SwipeRefreshLayout swpRefresh;
    private ProgressBarIndeterminate pBar;

    private BustaPagaAdapter adapter;
    private List<BustaPaga> data;
    private SwipeListView swpLstView;
    private LinkedHashMap lista = null;
    private List<Info> infos;


    private DataBaseAdapter dataBA = null;

    private HRConnect conn = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_lista, container, false);
        setHasOptionsMenu(true);
        adapter = new BustaPagaAdapter(act);



        conn = new HRConnect();
        dataBA = new DataBaseAdapter( act);
        dataPrepare();



        swpLstView = (SwipeListView) rootView.findViewById(R.id.swipList);
        swpRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_swipe);
        pBar       = (ProgressBarIndeterminate) rootView.findViewById(R.id.progressBar);


        swpLstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            swpLstView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    mode.setTitle("Selected ( prova )");
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_delete:
                            /*swpLstView.skipSelected();
                            for (Integer i: swpLstView.skipSelected()) {
                                adapter.getItem(i).getHolder().ivImage.setImageResource(R.drawable.rosso);
                                ((EserAdapter) holder.swpLstView.getAdapter()).addInvalidItem((int)i);
                            }*/
                            adapter.notifyDataSetChanged();
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.swipe, menu);
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    swpLstView.unselectedChoiceStates();
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
        }
        //swpLstView.setSwipeListViewListener(new BaseSwipeListViewListener(this, holder, holder.swpLstView));
        swpLstView.setAdapter(adapter);

        swpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        setup(swpLstView);
        return rootView;
    }

    private void refreshContent(){
    /*    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getNewTweets());
                mListView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            });
        }*/
    }



    private void dataPrepare() {
        dataBA.open();
        list = dataBA.getAllBuste();
        dataBA.close();
        if (list.size() == 0 ){
            ThreadHome tDoc = null;
            try {
                (new ThreadHome(this, conn, "10687556", "!omabA09")).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setup(SwipeListView swpLst) {
        swpLst.setSwipeMode(SwipeListView.SWIPE_MODE_RIGHT);
        swpLst.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        swpLst.setSwipeOpenOnLongPress(false);
    }

    @Override
    public void onHomeDownloaded(boolean x) {

    }

    @Override
    public void onListaDownloaded(LinkedHashMap<String, String> l) {
        adapter.getList().clear();

        for (Map.Entry entry : l.entrySet()) {
            // creo busta Paga
            BustaPaga bP = new BustaPaga((String) entry.getKey(),
                    (String) entry.getValue());

            adapter.add(bP);
        }
        adapter.notifyDataSetChanged();
    }
}