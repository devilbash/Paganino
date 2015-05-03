package it.bestapp.paganino.utility.thread;

import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.dialog.Login;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.setting.SettingsManager;

import org.apache.http.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

public class ThreadHome extends AsyncTask<Void, Void, String> {

    //private Activity act;
    private HRConnect con = null;
    private String user;
    private String pswd;
    private PageDownloadedInterface callBack = null;
    private ArrayList<Busta> lista = null;
    private boolean error;
    private Login lDialog;
    private SettingsManager settings;



    public ThreadHome( PageDownloadedInterface fL, HRConnect c) {
        this.con  = c;
        this.callBack = fL;

        SingletonParametersBridge singleton = SingletonParametersBridge.getInstance();
        settings = (SettingsManager) singleton.getParameter("settings");
        this.user = settings.getUser();
        this.pswd = settings.getPaswd();

    }


    @Override
    protected String doInBackground(Void... params) {
        String page = "";
        List<NameValuePair>  postParams	= null;
        error = true;
        if (user.equalsIgnoreCase("") || pswd.equalsIgnoreCase(""))
            return page;


        try {
            page = con.GetPageContent(con.LOGIN);
            postParams = con.getFormParams(page, settings.getUser(), settings.getPaswd());
            page = con.sendPost(postParams);
            con.loginCheck(page);
            if (!con.getLoginCheck())
                return page;
            page = con.GetPageContent(con.LISTA);;
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        }
        error = false;
        settings.setUser(user);
        settings.setPaswd(pswd);
        return page;
    }


    protected void onPostExecute(String page) {
        if (error) {
            callBack.login();
            return;
        };
        lista = new ArrayList<Busta>();
        String link;
        String keys[];
        String key ;
        Element body    = Jsoup.parseBodyFragment(page).body();
        Element bodyTab = body.getElementById("ctl00_CPLH_TPrincipale").child(0);
        Document doc    = Jsoup.parseBodyFragment( bodyTab.toString() );
        Elements righe  = doc.getElementsByTag("tr");

        for (Element riga : righe) {
            riga= riga.child(0).nextElementSibling().child(0).child(0);
            link = riga.attr("href");
            keys = link.split("Versione=A");
            key  = keys[keys.length-1];
            key  = key.substring(2, 6);
            lista.add(new Busta(key));
        }
        callBack.onListaDownloaded(lista);
    }
}




