package it.bestapp.paganino.utility.db.bin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public class Ore implements Parcelable {

    private String id;
    private String tipo;

    private float spe;
    private float god;
    private float res;

    public Ore(String id, String t) {
        this.id = id;
        this.tipo = t;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getGod() {
        return god;
    }

    public void setGod(String god) {
        this.god = Float.parseFloat(god.replace(",", "."));
    }

    public float getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = Float.parseFloat(res.replace(",", "."));
    }

    public float getSpe() {
        return spe;
    }

    public void setSpe(String spe) {
        this.spe = Float.parseFloat(spe.replace(",", "."));
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setSpe(float spe) {
        this.spe = spe;
    }

    public void setGod(float god) {
        this.god = god;
    }

    public void setRes(float res) {
        this.res = res;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tipo);
        dest.writeFloat(spe);
        dest.writeFloat(god);
        dest.writeFloat(res);
    }

    public void readFromParcel(Parcel in) {
        id   = in.readString();
        tipo = in.readString();

        spe = in.readFloat();
        god = in.readFloat();
        res   = in.readFloat();
    }

    private Ore(Parcel in) {
        readFromParcel(in);
    }


    public static final Parcelable.Creator<Ore> CREATOR = new Parcelable.Creator<Ore>() {
        public Ore createFromParcel(Parcel in) {
            return new Ore(in);
        }

        public Ore[] newArray (int size)  {
            return new Ore[size];
        }
    };




}
