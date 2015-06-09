package it.bestapp.paganino.utility.thread;

import android.content.Intent;
import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.bestapp.paganino.ChartConfronto;
import it.bestapp.paganino.ChartRange;
import it.bestapp.paganino.Main;
import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.db.DataBaseAdapter;
import it.bestapp.paganino.utility.db.bin.BustaPaga;
import it.bestapp.paganino.utility.parser.BustaPagaParser;
import it.bestapp.paganino.utility.setting.SettingsManager;

/**
 * Created by marco.compostella on 11/04/2015.
 */
public class ThreadStoreController extends AsyncTask<Void, Void, Void> {

    // private Fragment frag   = null;
    private HRConnect conn  = null;
    private PageDownloadedInterface pCall = null;
    private SingletonParametersBridge singleton;
    private String path;
    private List<Busta> buste;
    private DataBaseAdapter dataBA;
    private ArrayList<BustaPaga> range;
    private Main act;
    private char grafico ;

    public ThreadStoreController(Main a, List<Busta> b, HRConnect c,char g) {
        //this.frag  = f;
        this.conn  = c;
        //this.pCall = (PageDownloadedInterface) f;
        this.buste = b;
        this.grafico = g;
        act = a;
        singleton = SingletonParametersBridge.getInstance();
        SettingsManager settings = (SettingsManager) singleton.getParameter("settings",act);

        range = new ArrayList<BustaPaga>();
        dataBA = new DataBaseAdapter(act);

        path = settings.getPath();
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //    pCall.procesDialog(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        int count;
        File  f              = null;
        InputStream in       = null;
        FileOutputStream out = null;
        BustaPaga bPaga = null;

        dataBA.open();
        for (Busta b : buste){
            f = new File(path , b.getID() + ".pdf");
            //se file non esiste: 1) scarico 2) scrivo 3) analizzo 4)salvo db
            if (!f.exists()) {
    //step 1-2
                try {
                    in = conn.getPDF( b.getID() );
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
            }

            bPaga = dataBA.getSingleBusta(b.getID());
            if (bPaga == null) {
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
                    bPaga = BustaPagaParser.getBusta(foglio, b.getID());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dataBA.insertBusta(bPaga);
            }
            range.add(bPaga);
        }

        dataBA.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void param) {
        //    pCall.procesDialog(false);
        //    pCall.onStoreCompleted(buste);
        Intent intent = null;
        if ( grafico == 'R' )
            intent = new Intent( act, ChartRange.class);
        else if(grafico == 'C')
            intent = new Intent( act, ChartConfronto.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putParcelableArrayListExtra("BUSTE", range);
        act.startActivity(intent);
    }
}
