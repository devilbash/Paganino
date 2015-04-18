package it.bestapp.paganino.adapter.bustapaga;

import android.app.Activity;

import it.bestapp.paganino.R;

public class BustaPaga  {
	private Info info;
    private String  id;
    private Activity act;
	private boolean nascosto;
	
    public BustaPaga(Activity a, String anno_mese) {
        this.act = a;
    	this.info = null;
    	this.id = anno_mese;
    }

    public String  getID() {
        return this.id ;
    }


    public String  getMese() {
    	String idsplit = id.substring(2, 4);
        String[] mesi = act.getResources().getStringArray(R.array.MESI);

        return mesi[Integer.parseInt(idsplit)-1];
    }

    public String getAnno() {
        return ("20" + id.substring(0,2));
    }



	public boolean getNascosto(boolean  x){
		return this.nascosto;		
	}
	public void setNascosto(boolean  x){
		this.nascosto = x;		
	}
	
	
	public void setInfo(Info i){
		this.info = i;		
	}
	
	public Info getInfo(){
		return this.info ;		
	}


		
	
}
