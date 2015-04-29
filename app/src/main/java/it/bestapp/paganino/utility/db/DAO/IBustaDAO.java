package it.bestapp.paganino.utility.db.DAO;

import java.util.List;

import it.bestapp.paganino.utility.db.Info;
import it.bestapp.paganino.utility.db.bin.Busta;

/**
 * Created by marco.compostella on 20/04/2015.
 */
public interface IBustaDAO {
    public String SELECT_BY_PK = "SELECT * FROM BUSTA_PAGA WHERE ID = ?";
    public String SELECT_ALL   = "SELECT * FROM BUSTA_PAGA";

    public void insertBusta(Busta b);
    public Busta getSingleBusta(String id);
    public List<Busta> getAllBusta();
}
