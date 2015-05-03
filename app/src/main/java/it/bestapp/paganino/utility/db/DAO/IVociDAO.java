package it.bestapp.paganino.utility.db.DAO;

import java.util.List;

import it.bestapp.paganino.utility.db.bin.Voci;

/**
 * Created by marco.compostella on 21/04/2015.
 */
public interface IVociDAO {
    public String SELECT_ALL = "SELECT * FROM BUSTA_VOCI WHERE ID = ?";
    public String SELECT_BY_PK = "SELECT * FROM BUSTA_VOCI WHERE ID = ? AND POS = ?";

    public void insertVoci(Voci v);
    public Voci getSingleVoci(String id, int i);
    public List<Voci> getAllVoci(String id);
}
