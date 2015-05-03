package it.bestapp.paganino.utility.db.DAO;

import java.util.List;

import it.bestapp.paganino.utility.db.bin.BustaPaga;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public interface IBustaDAO {
    String SELECT_BY_PK      = "SELECT * FROM BUSTA_PAGA WHERE ID = ?";
    String SELECT_ALL        = "SELECT * FROM BUSTA_PAGA";
    String SELECT_DATE_RANGE = "SELECT * FROM BUSTA_PAGA WHERE ANNO >= ? AND MESE >= ? AND ANNO <= ? AND MESE <= ? ";

    public void insertBusta(BustaPaga b);
    public BustaPaga getSingleBusta(String id);
    public List<BustaPaga> getIntervalBusta(int a1,int m1,int a2,int m2);
    public List<BustaPaga> getAllBusta();
}
