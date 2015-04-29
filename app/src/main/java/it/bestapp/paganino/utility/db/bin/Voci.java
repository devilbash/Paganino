package it.bestapp.paganino.utility.db.bin;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public class Voci {

    private String id;
    private int pos;
    private int idVoce;
    private float importo;
    private String shkzg;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getIdVoce() {
        return idVoce;
    }

    public void setIdVoce(int idVoce) {
        this.idVoce = idVoce;
    }

    public float getImporto() {
        return importo;
    }

    public void setImporto(float importo) {
        this.importo = importo;
    }

    public String getShkzg() {
        return shkzg;
    }

    public void setShkzg(String shkzg) {
        this.shkzg = shkzg;
    }
}
