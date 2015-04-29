package it.bestapp.paganino.utility.parser;

import java.util.ArrayList;
import java.util.List;

import it.bestapp.paganino.utility.db.Info;
import it.bestapp.paganino.utility.db.bin.Busta;
import it.bestapp.paganino.utility.db.bin.Ore;

public class BustaPagaParser {


    static public Busta getBusta (String a[], String id){
        Busta b = new Busta(id);

        for(int i=0; i < a.length ; i++  ){

            if (a[i].contains("PAGA BASE")){
                b.setPagaBase(string2float(split2space(a[i],2)));
                continue;
            }

            if (a[i].equalsIgnoreCase("I DATI DELLE PRESENZE ED I DATI RETRIBUTIVI VARIABILI VALORIZZATI SI RIFERISCONO A PRESTAZIONI E ASSENZE AL MESE PRECEDENTE")) {
                b.setSuperMin(string2float(split2space(a[i-1],1))- b.getPagaBase());
                continue;
            }

            if (a[i].equalsIgnoreCase("Totale Ritenute Totale Competenze")) {
                b.setTotRit(string2float(split2space(a[i+1])));
                b.setTotComp(string2float(split2space(a[i+1],1)));

                if ( b.getTotComp() != 0 &&
                        b.getTotRit() != 0 ){
                    b.setNetto( b.getTotComp() - b.getTotRit() );
                }

            }

            if (a[i].equalsIgnoreCase("Spettanti Godute Residue Spettanti Godute Residue Spettanti Godute Residue")){
                b.setOre(splitOre(a[i+3], id));
                break;
            }
        }
        return b;
    }


    static public String split2space(String s, int pos){
        String tmp [];
        tmp  = s.split(" ");
        return tmp[pos];
    }
    static public String split2space(String s){
        return split2space(s, 0);
    }
    static public float string2float(String imp){
        imp = imp.replace(".", "");
        imp = imp.replace(",", ".");
        return Float.parseFloat(imp);
    }



    static public List<Ore> splitOre(String a, String id) {
        List<Ore> list = new ArrayList<Ore>();
        String[] tmp = a.split(" ");
        Ore ora;

        ora = new Ore(id,"F");
        ora.setRes(tmp[1]);
        ora.setGod(tmp[2]);
        ora.setSpe(tmp[3]);
        list.add(ora);

        ora = new Ore(id,"R");
        ora.setRes(tmp[4]);
        ora.setGod(tmp[5]);
        ora.setSpe(tmp[6]);
        list.add(ora);

        ora = new Ore(id,"B");
        ora.setRes(tmp[7]);
        ora.setGod(tmp[8]);
        ora.setSpe(tmp[9]);
        list.add(ora);

        return list;
    }

}
