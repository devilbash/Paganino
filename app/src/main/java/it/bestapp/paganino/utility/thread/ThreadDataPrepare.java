package it.bestapp.paganino.utility.thread;


import android.app.Activity;
import android.os.AsyncTask;
import java.io.File;
import java.util.ArrayList;

import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.setting.SettingsManager;



public class ThreadDataPrepare extends AsyncTask<Void, Void, ArrayList<String>> {

    private SingletonParametersBridge singleton;
    private String dirPath;
    private PageDownloadedInterface callBack = null;
    private HRConnect conn;
    private Activity act;
    private Lista fLista;

    public ThreadDataPrepare(Lista f, HRConnect c) {
        this.act = f.getActivity();
        this.fLista = f;
        this.callBack= (PageDownloadedInterface) f;
        this.conn = c;
        singleton = SingletonParametersBridge.getInstance();
        SettingsManager settings = (SettingsManager) singleton.getParameter("settings");
        dirPath = settings.getPath();
    }

    @Override
    public ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> lista = new ArrayList<String>();
        String[] filen = new String[2];
        File f = new File(dirPath);
        if (f.exists() && f.isDirectory()){
            File[] files = f.listFiles();
            for(int i=0; i < files.length; i++) {
                File file = files[i];
                filen = file.getName().split("\\.");
                if( filen[1].equalsIgnoreCase("pdf")   ){
                    lista.add(filen[0]);
                }
            }
        }
        return  lista;
    }

    @Override
    protected void onPostExecute(ArrayList<String> l) {
        if (l.isEmpty())
            (new ThreadHome(fLista, conn)).execute();
        else
            callBack.onListaDownloaded(l);
    }
}