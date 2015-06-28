package it.bestapp.paganino.utility.thread;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.utility.ExportXls;
import it.bestapp.paganino.utility.SingletonParametersBridge;
import it.bestapp.paganino.utility.connessione.HRConnect;
import it.bestapp.paganino.utility.connessione.PageDownloadedInterface;
import it.bestapp.paganino.utility.db.DataBaseAdapter;
import it.bestapp.paganino.utility.db.bin.BustaPaga;
import it.bestapp.paganino.utility.parser.BustaPagaParser;
import it.bestapp.paganino.utility.setting.SettingsManager;
import jxl.write.WriteException;


public class ThreadAnaPDF extends AsyncTask<Void, Void, BustaPaga> {

    private Fragment frag = null;
    private HRConnect con = null;
    private Busta busta = null;
    private PageDownloadedInterface pCall = null;
    private SingletonParametersBridge singleton;
    private String pathPDF;
    private String pathXLS;
    private char mode;
    private DataBaseAdapter dataBA = null;

    private File fXLS;
    private BustaPaga bustaP;

    public ThreadAnaPDF(Fragment f, HRConnect c, Busta bP, char m) {
        this.frag = f;
        this.mode = m;
        this.con = c;
        this.busta = bP;
        this.pCall = (PageDownloadedInterface) f;


        dataBA = new DataBaseAdapter(frag.getActivity().getBaseContext());
        singleton = SingletonParametersBridge.getInstance();
        SettingsManager settings = (SettingsManager) singleton.getParameter("settings",f.getActivity());
        pathPDF = settings.getPath() + "/pdf";
        if (mode == 'E') {
            pathXLS = settings.getPath() + "/excel";
            File file = new File(pathXLS);
            if (!file.exists())
                file.mkdirs();
        }
        File file = new File(pathPDF);
        if (!file.exists())
            file.mkdirs();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pCall.procesDialog(true);
    }

    @Override
    public BustaPaga doInBackground(Void... params) {
        int count;
        InputStream in;
        FileOutputStream out = null;
        bustaP = null;

        dataBA.open();
        bustaP = dataBA.getSingleBusta(busta.getID());
        dataBA.close();

        if (bustaP == null) {
            File fPDF = new File(pathPDF, busta.getID() + ".pdf");
            if (!fPDF.exists()) {
                try {
                    in = con.getPDF(busta.getID());
                    out = new FileOutputStream(fPDF);
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
            String txt = "";
            try {
                PdfReader reader = new PdfReader(fPDF.getAbsolutePath());
                PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                TextExtractionStrategy strategy;
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                    txt += (strategy.getResultantText());
                }
                String tst = txt.replaceAll(" {2,}", " ");
                String foglio[] = tst.split("\n");
                bustaP = BustaPagaParser.getBusta(foglio, busta);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataBA.open();
            dataBA.insertBusta(bustaP);
            dataBA.close();
        }

//scrivi excel
        if (mode == 'E') {
            fXLS = new File(pathXLS, busta.getID() + ".xls");
            if (!fXLS.exists()) {
                ExportXls test = new ExportXls(fXLS, frag);
                try {
                    test.create(bustaP);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
        return bustaP;
    }

    @Override
    protected void onPostExecute(BustaPaga bP) {
        pCall.procesDialog(false);
        if (mode == 'G')
            pCall.onPDFDownloaded(bP, mode);
        else if (mode == 'E')
            pCall.onPDFDownloaded(busta, fXLS ,mode);
    }
}