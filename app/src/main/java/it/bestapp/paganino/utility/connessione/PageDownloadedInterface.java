package it.bestapp.paganino.utility.connessione;


//import it.bestapp.paganino.BustaPaghe.BustaPaga;

import java.io.File;
import java.util.ArrayList;
import it.bestapp.paganino.adapter.bustapaga.BustaPaga;


public interface PageDownloadedInterface {

	public static final int ARCHIVIA 	= 1;
	public static final int ANALIZZA 	= 2;
	public static final int VISUALIZZA 	= 3;
	public static final int SCARICA 	= 4;
	
	public void onPDFDownloaded(BustaPaga b, File f, char mode);
    public void onStoreCompleted();
	public void onListaDownloaded(ArrayList<String> lista);

    public void procesDialog(boolean b);
    public void login();
}
