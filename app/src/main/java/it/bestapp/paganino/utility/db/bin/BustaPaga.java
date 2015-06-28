package it.bestapp.paganino.utility.db.bin;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public class BustaPaga implements Parcelable {

    private String id;

    private float  pagaBase;
    private float  superMin;
    private float  totRit;
    private float  totComp;
    private float  netto;

    private float  pereq;
    private float  straOrd;
    private float  trasf;


    private Map<String,Ore> ore;
    private List<Voci> voci;

    public int  getNumMese() {
        return Integer.parseInt(id.substring(2, 4));
    }

    public int getNumAnno() {
        return 2000 + Integer.parseInt(id.substring(0, 2));
    }



    public BustaPaga(String id) {
        this.id = id;
        ore = new HashMap();
    }

    public Map<String,Ore> getOre() {
        return ore;
    }
    public void setOre(Map<String,Ore> ore) {
        this.ore = ore;
    }

    public List<Voci> getVoci() {
        return voci;
    }

    public void setVoci(List<Voci> voci) {
        this.voci = voci;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPagaBase() {
        return pagaBase;
    }

    public void setPagaBase(float pagaBase) {
        this.pagaBase = pagaBase;
    }

    public float getSuperMin() {
        return superMin;
    }

    public void setSuperMin(float superMin) {
        this.superMin = superMin;
    }

    public float getTotRit() {
        return totRit;
    }

    public void setTotRit(float totRit) {
        this.totRit = totRit;
    }

    public float getTotComp() {
        return totComp;
    }

    public void setTotComp(float totComp) {
        this.totComp = totComp;
    }

    public float getNetto() {
        return netto;
    }

    public void setNetto(float netto) {
        this.netto = netto;
    }

    public float getPereq() {
        return pereq;
    }

    public void setPereq(float pereq) {
        this.pereq = pereq;
    }

    public float getStraOrd() {
        return straOrd;
    }

    public void setStraOrd(float stra) {
        this.straOrd += stra;
    }

    public float getTrasf() {
        return trasf;
    }

    public void setTrasf(float trasf) {
        this.trasf = trasf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeFloat(pagaBase);
        dest.writeFloat(superMin);
        dest.writeFloat(totRit);
        dest.writeFloat(totComp);
        dest.writeFloat(netto);

        dest.writeFloat(pereq);
        dest.writeFloat(straOrd);
        dest.writeFloat(trasf);

        writeOre(dest);
    }

    public static final Parcelable.Creator<BustaPaga> CREATOR
            = new Parcelable.Creator<BustaPaga>() {
        public BustaPaga createFromParcel(Parcel in) {
            return new BustaPaga(in);
        }

        public BustaPaga[] newArray(int size) {
            return new BustaPaga[size];
        }
    };

    private BustaPaga(Parcel in) {
        ore = new HashMap();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        id = in.readString();

        pagaBase = in.readFloat();
        superMin = in.readFloat();
        totRit   = in.readFloat();
        totComp  = in.readFloat();
        netto    = in.readFloat();

        pereq    = in.readFloat();
        straOrd  = in.readFloat();
        trasf    = in.readFloat();

        readOre(in);
    }


    public void writeOre(Parcel out) {
        out.writeInt(ore.size());
        for (Map.Entry<String, Ore> entry : ore.entrySet()) {
            out.writeString(entry.getKey());
            out.writeParcelable(entry.getValue(),0);
        }
    }

    private void readOre(Parcel in) {
        int size = in.readInt();
        String key = null;
        Ore o = null;
        for (int i = 0; i < size; i++) {
            key = in.readString();
            o = in.readParcelable(Ore.class.getClassLoader());
            ore.put(key, o);
        }
    }


}



