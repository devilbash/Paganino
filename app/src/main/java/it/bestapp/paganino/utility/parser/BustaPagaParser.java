package it.bestapp.paganino.utility.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.bestapp.paganino.adapter.bustapaga.Busta;
import it.bestapp.paganino.utility.db.bin.BustaPaga;
import it.bestapp.paganino.utility.db.bin.Ore;

public class BustaPagaParser {


    static public BustaPaga getBusta (String a[], Busta bP ){
        String id = bP.getID();
        BustaPaga b = new BustaPaga(id);

        for(int i=0; i < a.length ; i++  ){

            if (a[i].contains("PAGA BASE")){
                b.setPagaBase(string2float(split2space(a[i], 2)));
                continue;
            }

            if (a[i].equalsIgnoreCase("I DATI DELLE PRESENZE ED I DATI RETRIBUTIVI VARIABILI VALORIZZATI SI RIFERISCONO A PRESTAZIONI E ASSENZE AL MESE PRECEDENTE")) {
                b.setSuperMin(string2float(split2space(a[i - 1], 1)) - b.getPagaBase());
                continue;
            }

            if (a[i].equalsIgnoreCase("Totale Ritenute Totale Competenze")) {
                b.setTotRit(string2float(split2space(a[i + 1])));
                b.setTotComp(string2float(split2space(a[i+1],1)));

                if ( b.getTotComp() != 0 &&
                        b.getTotRit() != 0 ){
                    b.setNetto( b.getTotComp() - b.getTotRit() );
                }
            }

            if (a[i].equalsIgnoreCase("Spettanti Godute Residue Spettanti Godute Residue Spettanti Godute Residue")){
                b.setOre(splitOre(a[i + 3], id));
                break;
            }

            if (a[i].equalsIgnoreCase("Spettanti Godute Residue Spettanti Godute Residue Spettanti Godute Residue")){
                b.setOre(splitOre(a[i+3], id));
                break;
            }

            if (a[i].startsWith("0085 STRAORD.25% MESE PRECED")){
                b.setStraOrd(string2float(split2space(a[i],3)));
                break;
            }
            if (a[i].startsWith("0288 MAGGIORAZIONE 30% MESE PRECED")){
                b.setStraOrd(string2float(split2space(a[i],3)));
                break;
            }
            if (a[i].startsWith("0291 MAGGIORAZIONE 50% MESE PRECED")){
                b.setStraOrd(string2float(split2space(a[i],3)));
                break;
            }
            if (a[i].startsWith("0663 ANTICIPI TRASFERTE")){
                b.setTrasf(string2float(split2space(a[i],3)));
                break;
            }
            if (a[i].startsWith("0014 ELEMENTO PEREQUATIVO CCNL")){
                b.setPereq(string2float(split2space(a[i],4)));
                break;
            }
            if (a[i].startsWith("1655 CM Credito DL 66 del 24/04/2014")){
                b.setPereq(string2float(split2space(a[i],6)));
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



    static public Map<String,Ore> splitOre(String a, String id) {
        Map<String,Ore> list = new HashMap<String,Ore>();
        String[] tmp = a.split(" ");
        Ore ora;

        ora = new Ore(id);
        ora.setRes(tmp[1]);
        ora.setGod(tmp[2]);
        ora.setSpe(tmp[3]);
        list.put("F", ora);

        ora = new Ore(id);
        ora.setRes(tmp[4]);
        ora.setGod(tmp[5]);
        ora.setSpe(tmp[6]);
        list.put("R", ora);

        ora = new Ore(id);
        ora.setRes(tmp[7]);
        ora.setGod(tmp[8]);
        ora.setSpe(tmp[9]);
        list.put("B", ora);

        return list;
    }

}
