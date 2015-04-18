package it.bestapp.paganino.utility.parser;

import it.bestapp.paganino.adapter.bustapaga.Info;

public class BustaPagaParser extends Info {


	public BustaPagaParser (String a[], String id){
		super(id);
		String[] tmp= new String[3];
		String[] tmp_ore= new String[10];
		String importo;
		String ore;
		
		tmp = a[12].split(" ");
		importo = tmp[2];
		importo = importo.replace(".", "");
		importo = importo.replace(",", ".");
		lordo_base = Float.parseFloat(importo);
		
		tmp = a[21].split(" ");
		importo = tmp[1];
		importo = importo.replace(".", "");
		importo = importo.replace(",", ".");
		lordo = Float.parseFloat(importo);;
		
		bonus = lordo - lordo_base;
		
		int i=22;  //prima di questa riga non trovo info utili
		for(; i < a.length ; i++  ){
			if (a[i].equalsIgnoreCase("Totale Ritenute Totale Competenze"))
				break;			
		}
		i= i+1;
		tmp = a[i].split(" ");
		importo = tmp[0];
		importo = importo.replace(".", "");
		importo = importo.replace(",", ".");
		ritenute = Float.parseFloat(importo);
		
		
		importo = tmp[1];
		importo = importo.replace(".", "");
		importo = importo.replace(",", ".");		
		totale  = Float.parseFloat(importo);
		extra  = totale - lordo_base;
		
		netto    = totale - ritenute;	
		
	}	
	
	public Info returnInfo() {
		return (Info) this;
	}
	
	
	static public float[] getOre(String a[]) {
		String ore; 
		String[] tmp = new String[10];
		float[] ore_ret = new float[9];
		
		int i=22;
		for(; i < a.length ; i++  ){
			if (a[i].equalsIgnoreCase("Spettanti Godute Residue Spettanti Godute Residue Spettanti Godute Residue"))
				break;			
		}
		i= i+3;    // anno corrente ac
		tmp = a[i].split(" ");
		
		ore = tmp[1];
		ore = ore.replace(",", ".");		
		ore_ret[0] = Float.parseFloat(ore);
		ore = tmp[2];
		ore = ore.replace(",", ".");		
		ore_ret[1]  = Float.parseFloat(ore);		
		ore = tmp[3];
		ore = ore.replace(",", ".");		
		ore_ret[2]  = Float.parseFloat(ore);
		
		ore = tmp[4];
		ore = ore.replace(",", ".");		
		ore_ret[3]  = Float.parseFloat(ore);
		ore = tmp[5];
		ore = ore.replace(",", ".");		
		ore_ret[4]  = Float.parseFloat(ore);
		ore = tmp[6];
		ore = ore.replace(",", ".");		
		ore_ret[5]  = Float.parseFloat(ore);
		
		ore = tmp[7];
		ore = ore.replace(",", ".");		
		ore_ret[6]  = Float.parseFloat(ore);
		ore = tmp[8];
		ore = ore.replace(",", ".");		
		ore_ret[7]  = Float.parseFloat(ore);
		ore = tmp[9];
		ore = ore.replace(",", ".");		
		ore_ret[8]  = Float.parseFloat(ore);
		
		return ore_ret;
	} 
	
}
