package it.bestapp.paganino.adapter.bustapaga;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import it.bestapp.paganino.R;
import it.bestapp.paganino.utility.db.bin.Ore;

public class Busta implements
                        Comparable<Busta>,
                        Parcelable {
    private String  id;
    private boolean nascosto;

    public Busta(String anno_mese) {
        this.id = anno_mese;
    }

    public String  getID() {
        return this.id ;
    }


    public String  getMese(Activity a) {
        String idsplit = id.substring(2, 4);
        String[] mesi = a.getResources().getStringArray(R.array.MESI);

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


    @Override
    public int compareTo(Busta another) {
        int i= another.getNumAnno() - this.getNumAnno();
        if (i == 0)
            i= another.getNumMese() - this.getNumMese();
        return i;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeByte((byte) (nascosto ? 1 : 0));
    }

    public static final Parcelable.Creator<Busta> CREATOR
            = new Parcelable.Creator<Busta>() {
        public Busta createFromParcel(Parcel in) {
            return new Busta(in);
        }

        public Busta[] newArray(int size) {
            return new Busta[size];
        }
    };
    private Busta(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        id = in.readString();
        nascosto = in.readByte() != 0;
    }


    static public String getData(Activity a, String id) {
        String idsplit = id.substring(2, 4);
        String[] mesi = a.getResources().getStringArray(R.array.MESI);
        return mesi[Integer.parseInt(idsplit)-1] +"-"+("20" + id.substring(0,2));
    }

    static public String getData( String id) {
         return id.substring(2, 4) +"-"+ ("20" + id.substring(0,2));
    }
}
