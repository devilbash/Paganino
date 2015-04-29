package it.bestapp.paganino.utility.db.bin;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public class Busta implements Parcelable {

    private String id;

    private float  pagaBase;
    private float  superMin;
    private float  totRit;
    private float  totComp;
    private float  netto;

    private List<Ore>  ore;
    private List<Voci> voci;

    public Busta(String id) {
        this.id = id;
    }

    public List<Ore> getOre() {
        return ore;
    }

    public void setOre(List<Ore> ore) {
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

        dest.writeTypedList(ore);
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

        pagaBase = in.readFloat();
        superMin = in.readFloat();
        totRit   = in.readFloat();
        totComp  = in.readFloat();
        netto    = in.readFloat();


        if (ore == null) {
            ore = new ArrayList();
        }
        in.readTypedList(ore, Ore.CREATOR);
    }

}
