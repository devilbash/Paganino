package it.bestapp.paganino.utility.connessione;


//import it.bestapp.paganino.BustaPaghe.BustaPaga;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.utility.db.bin.BustaPaga;


public interface PageDownloadedInterface {

	public static final int ARCHIVIA 	= 1;
	public static final int ANALIZZA 	= 2;
	public static final int VISUALIZZA 	= 3;
	public static final int SCARICA 	= 4;
	
	public void onPDFDownloaded(Busta b, File f, char mode);
    public void onPDFDownloaded(BustaPaga b, char mode);
    public void onStoreCompleted(List<Busta> buste);
	public void onListaDownloaded(ArrayList<Busta> lista);

    public void procesDialog(boolean b);
    public void login();
}
