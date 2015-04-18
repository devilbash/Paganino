package it.bestapp.paganino.utility.thread;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.ListAdapter;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.apache.http.impl.client.AbstractHttpClient;
import org.jsoup.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import it.bestapp.paganino.adapter.bustapaga.BustaPaga;
import it.bestapp.paganino.adapter.bustapaga.BustaPagaAdapter;
import it.bestapp.paganino.fragment.Lista;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.db.DataBaseAdapter;
import it.bestapp.paganino.utility.parser.BustaPagaParser;
import it.bestapp.paganino.utility.setting.SettingsManager;

/**
 * Created by marco.compostella on 11/04/2015.
 */
public class ThreadStoreController extends AsyncTask<Void, Void, Void> {

    private Fragment frag   = null;
    private HRConnect conn  = null;
    private PageDownloadedInterface pCall = null;
    private SingletonParametersBridge singleton;
    private String path;
    private List<BustaPaga> buste;


    public ThreadStoreController(Lista f, List<BustaPaga> b, HRConnect c) {
        this.frag  = f;
        this.conn  = c;
        this.pCall = (PageDownloadedInterface) f;
        this.buste = b;

        singleton = SingletonParametersBridge.getInstance();
        SettingsManager settings = (SettingsManager) singleton.getParameter("settings");
        path = settings.getPath();
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pCall.procesDialog(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        int count;
        File  f              = null;
        InputStream in       = null;
        FileOutputStream out = null;

        for (BustaPaga bPaga : buste){
            f = new File(path , bPaga.getID() + ".pdf");
            if (!f.exists()) {   //se file non esiste: 1) scarico 2) scrivo 3) analizzo 4)salvo db
                //step 1-2
                try {
                    in = conn.getPDF( bPaga.getID() );
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

                //step 3
                BustaPagaParser bPP = null;
                String txt = "";
                try {
                    PdfReader reader = new PdfReader(f.getAbsolutePath());
                    PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                    TextExtractionStrategy strategy;
                    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                        strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                        txt += (strategy.getResultantText());
                    }

                    String tst = txt.replaceAll(" {2,}", " ");
                    String foglio[] = tst.split("\n");
                    bPP = new BustaPagaParser(foglio, bPaga.getID());

                } catch (Exception e) {
                    e.printStackTrace();
                }

		        //step 4
                DataBaseAdapter dataBA = null;
                dataBA = new DataBaseAdapter(frag.getActivity().getBaseContext());
                dataBA.open();
                dataBA.insertBusta(bPP.returnInfo());
                dataBA.close();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void param) {
        pCall.procesDialog(false);
        pCall.onStoreCompleted();
    }
}
