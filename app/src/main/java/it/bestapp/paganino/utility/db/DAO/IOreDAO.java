package it.bestapp.paganino.utility.db.DAO;

import java.util.List;
import java.util.Map;

import it.bestapp.paganino.utility.db.bin.Ore;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public interface IOreDAO {
    String SELECT_BY_PK = "SELECT * FROM BUSTA_ORE WHERE ID = ?";

    void insertOre(Map<String,Ore> ore,String id);
    Map<String,Ore> getSingleOre(String id);
}
