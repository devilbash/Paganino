package it.bestapp.paganino.utility.thread;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.apache.http.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.bestapp.paganino.Main;
import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.dialog.Login;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.setting.SettingsManager;

public class ThreadCud extends AsyncTask<Void, Void, File> {

    //private Activity act;
    private HRConnect con = null;
    private String user;
    private String pswd;
    //private PageDownloadedInterface callBack = null;
    private ArrayList<Busta> lista = null;
    private boolean error;
    private Login lDialog;
    private SettingsManager settings;


    private String path;

    public ThreadCud(Main a, HRConnect c) {
        Activity act = a;
        this.con  = c;

        //this.callBack = (PageDownloadedInterface) a;

        SingletonParametersBridge singleton = SingletonParametersBridge.getInstance();


        settings = (SettingsManager) singleton.getParameter("settings",act);
        path = settings.getPath() + "/cud";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();

        this.user = settings.getUser();
        this.pswd = settings.getPaswd();
    }


    @Override
    protected File doInBackground(Void... params) {
        String page = "";
        List<NameValuePair>  postParams	= null;
        File  f = null;

        error = true;
        if (user.equalsIgnoreCase("") || pswd.equalsIgnoreCase(""))
            return f;

        try {
            page = con.GetPageContent(con.LOGIN);
            postParams = con.getFormParams(page, settings.getUser(), settings.getPaswd());
            page = con.sendPost(postParams);
            con.loginCheck(page);
            if (!con.getLoginCheck())
                return f;
            page = con.GetPageContent(con.LIS_CUD);;
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        }
        error = false;
        settings.setUser(user);
        settings.setPaswd(pswd);


        String link = null;
        String keys[];
        String key ;
        Element body    = Jsoup.parseBodyFragment(page).body();
        Element bodyTab = body.getElementById("ctl00_CPLH_TContenitore");
        Document doc    = Jsoup.parseBodyFragment( bodyTab.toString() );
        Elements righe  = doc.getElementsByTag("tr");
        righe.remove(0);
        for (Element riga : righe) {
            riga= riga.child(0).nextElementSibling().child(0).child(0);
            link = riga.attr("href");
            break;
        }


        int count;
        InputStream in;
        FileOutputStream out = null;

        f = new File(path + "CUD.pdf");
     /*   if (!f.exists()) {
            try {
                in = con.getCUD(link);
                out = new FileOutputStream(f);
                byte data[] = new byte[1024];
                while ((count = in.read(data)) != -1)
                    out.write(data, 0, count);
                out.flush();
                out.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return f;




    }


    protected void onPostExecute(File f) {
/*        if (error) {
            callBack.login();
            return;
        };*/

       // callBack.onListaDownloaded(lista);
    }
}




