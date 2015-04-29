package it.bestapp.paganino.utility.db.DAO;

import java.util.List;

import it.bestapp.paganino.utility.db.bin.Busta;
import it.bestapp.paganino.utility.db.bin.Ore;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public interface IOreDAO {
    public String SELECT_BY_PK = "SELECT * FROM BUSTA_PAGA WHERE ID = ?";

    public void insertOre(List<Ore> b);
    public List<Ore> getSingleOre(String id);
}
