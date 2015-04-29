package it.bestapp.paganino.adapter.bustapaga;

import android.app.Activity;

import it.bestapp.paganino.R;
import it.bestapp.paganino.utility.db.Info;
import it.bestapp.paganino.utility.db.bin.Busta;

public class BustaPaga implements Comparable<BustaPaga> {
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

    public int  getNumMese() {
        return Integer.parseInt(id.substring(2, 4));
    }


    public String getAnno() {
        return ("20" + id.substring(0,2));
    }

    public int getNumAnno() {
        return 2000 + Integer.parseInt(id.substring(0,2));
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

    public boolean isEqual(BustaPaga b){
        return b.getID().equalsIgnoreCase(this.getID());
    }


    @Override
    public int compareTo(BustaPaga another) {
        int i= another.getNumAnno() - this.getNumAnno();
        if (i == 0)
            i= another.getNumMese() - this.getNumMese();
        return i;
    }
}
