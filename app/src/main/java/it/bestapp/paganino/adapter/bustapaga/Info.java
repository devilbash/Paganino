package it.bestapp.paganino.adapter.bustapaga;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {

	private String id;

	protected float lordo;
	protected float lordo_base;
	protected float bonus;
	protected float ritenute;
	protected float totale;
	protected float extra;
	protected float netto;
/*	
	protected float[] ferie;
	protected float[] rol;
	protected float[] banca;
	*/
	

	public Info(String id) {
		this.id = id;
	}

	public Info(String id, float l, float lb, float r, float t) {
		this.id = id;
		this.lordo = l;
		this.lordo_base = lb;
		this.ritenute = r;
		this.totale = t;
		this.netto = this.totale - this.ritenute;
		this.bonus = this.lordo - this.lordo_base;
		this.extra = this.totale - this.lordo_base;
		

	}

	public String getID() {
		return this.id;
	}
	public float getLordo() {
		return this.lordo;
	}
	public float getLordoB() {
		return this.lordo_base;
	}
	public float getRitenute() {
		return this.ritenute;
	}
	public float getTotale() {
		return this.totale;
	}
	public float getExtra() {
		return this.extra;
	}
	public float getBonus() {
		return this.bonus;
	}
	public float getNetto() {
		return this.netto;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/*
	 * Utility per lo scambio di dati fra activity
	 */
	public Info(Parcel i) {
		this.id = i.readString();
		this.lordo = i.readFloat();
		this.lordo_base = i.readFloat();
		this.ritenute = i.readFloat();
		this.totale = i.readFloat();
		this.netto = this.totale - this.ritenute;
		this.bonus = this.lordo - this.lordo_base;
		this.extra = this.totale - this.lordo_base;
	}

	@Override
	public void writeToParcel(Parcel i, int flags) {
		i.writeString(this.id);
		i.writeFloat(this.lordo);
		i.writeFloat(this.lordo_base);
		i.writeFloat(this.ritenute);
		i.writeFloat(this.totale);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Info createFromParcel(Parcel in) {
			return new Info(in);
		}

		public Info[] newArray(int size) {
			return new Info[size];
		}
	};

}
