package it.bestapp.paganino.utility.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import it.bestapp.paganino.utility.db.bin.BustaPaga;
import it.bestapp.paganino.utility.db.bin.Ore;
import it.bestapp.paganino.utility.db.bin.Voci;
import it.bestapp.paganino.utility.db.DAO.IBustaDAO;
import it.bestapp.paganino.utility.db.DAO.IOreDAO;
import it.bestapp.paganino.utility.db.DAO.IVociDAO;

public class DataBaseAdapter implements IBustaDAO, IOreDAO, IVociDAO {


    static final String DATABASE_NAME = "paganino.db";
    public static final int DATABASE_VERSION = 7 ;


    // SQL Statement to create a new database.
    static final String[] DATABASE_CREATE = {
            "create table "
                    + "BUSTA_PAGA " + "( "
                    + "ID "      	+ "text UNIQUE primary key, "
                    + "ANNO "       + "INTEGER, "
                    + "MESE "       + "INTEGER, "
                    + "PAGABASE "   + "REAL, "
                    + "SUPERMIN "	+ "REAL, "
                    + "TOTRIT "     + "REAL, "
                    + "TOTCOMP "	+ "REAL, "
                    + "NETTO "	    + "REAL "
                    + ");",
            "create table "
                    + "BUSTA_ORE "  + "( "
                    + "ID "      	+ " text UNIQUE primary key, "
                    + "F_GO " 		+ " REAL, "
                    + "F_SP "		+ " REAL, "
                    + "F_RE "		+ " REAL, "
                    + "R_GO " 		+ " REAL, "
                    + "R_SP "		+ " REAL, "
                    + "R_RE "		+ " REAL, "
                    + "B_GO " 		+ " REAL, "
                    + "B_SP "		+ " REAL, "
                    + "B_RE "		+ " REAL"
                    + ");",
            "create table "
                    + "BUSTA_VOCI " + "( "
                    + "ID "      	+ "TEXT UNIQUE primary key , "
                    + "POS " 		+ "TEXT "
                    + "IDVOCE " 	+ "INTEGER "
                    + "IMPORTO " 	+ "INTEGER "
                    + "SHKZG " 		+ "INTEGER "
                    + ");"
    };


    // Variable to hold the database instance
    public SQLiteDatabase db;
    private final Context context;
    private DataBaseHelper dbHelper;

    public DataBaseAdapter(Context _context) {
        context = _context;
    }
    // Method to openthe Database
    public DataBaseAdapter open() throws SQLException {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
        return this;
    }
    // Method to close the Database
    public void close() {
        db.close();
    }
    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    @Override
    public void insertBusta(BustaPaga b) {
        ContentValues newValues = new ContentValues();
        // Assign values for each column.
        newValues.put("ID",        b.getId());

        newValues.put("ANNO",      b.getNumAnno());
        newValues.put("MESE",      b.getNumMese());

        newValues.put("PAGABASE",  b.getPagaBase());
        newValues.put("SUPERMIN",  b.getSuperMin());
        newValues.put("TOTRIT",    b.getTotRit());
        newValues.put("TOTCOMP",   b.getTotComp());
        newValues.put("NETTO",     b.getNetto());
        // Insert the row into your table
        db.insert("BUSTA_PAGA", null, newValues);


        insertOre(b.getOre(), b.getId());
    }

    @Override
    public BustaPaga getSingleBusta(String id) {
        BustaPaga busta = null;
        String sql = IBustaDAO.SELECT_BY_PK;
        Cursor c = db.rawQuery(sql, new String[] { id });

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    busta = new BustaPaga(c.getString(c.getColumnIndex("ID")));
                    busta.setPagaBase(c.getFloat(c.getColumnIndex("PAGABASE")));
                    busta.setSuperMin(c.getFloat(c.getColumnIndex("SUPERMIN")));
                    busta.setTotRit(c.getFloat(c.getColumnIndex("TOTRIT")));
                    busta.setTotComp(c.getFloat(c.getColumnIndex("TOTCOMP")));
                    busta.setNetto(c.getFloat(c.getColumnIndex("NETTO")));
                    busta.setOre(getSingleOre(busta.getId()));
                } while (c.moveToNext());
            }
        }
        // return contact list
        c.close();
        return busta;
    }

    @Override
    public List<BustaPaga> getAllBusta() {
        String sql = IBustaDAO.SELECT_ALL;
        List<BustaPaga> list = new ArrayList<BustaPaga>();
        BustaPaga busta = null;
        Cursor c = db.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    busta.setId      (c.getString(c.getColumnIndex("ID")));
                    busta.setPagaBase(c.getFloat(c.getColumnIndex("PAGABASE")));
                    busta.setSuperMin(c.getFloat(c.getColumnIndex("SUPERMIN")));
                    busta.setTotRit(c.getFloat(c.getColumnIndex("TOTRIT")));
                    busta.setTotComp(c.getFloat(c.getColumnIndex("TOTCOMP")));
                    busta.setNetto(c.getFloat(c.getColumnIndex("NETTO")));
                    busta.setOre(getSingleOre(busta.getId()));
                    list.add(busta);
                } while (c.moveToNext());
            }
        }
        c.close();
        return list;
    }

    @Override
    public ArrayList<BustaPaga> getIntervalBusta(int a1,int m1,int a2,int m2) {
        String sql = IBustaDAO.SELECT_DATE_RANGE;
        ArrayList<BustaPaga> list = new ArrayList<BustaPaga>();
        BustaPaga busta = null;
        String [] whereCond = new String[]{String.valueOf(a1),
                String.valueOf(m1),
                String.valueOf(a2),
                String.valueOf(m2)};

        Cursor c = db.rawQuery(sql,whereCond );
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    busta = new BustaPaga(c.getString(c.getColumnIndex("ID")));
                    busta.setPagaBase(c.getFloat(c.getColumnIndex("PAGABASE")));
                    busta.setSuperMin(c.getFloat(c.getColumnIndex("SUPERMIN")));
                    busta.setTotRit(c.getFloat(c.getColumnIndex("TOTRIT")));
                    busta.setTotComp(c.getFloat(c.getColumnIndex("TOTCOMP")));
                    busta.setNetto(c.getFloat(c.getColumnIndex("NETTO")));
                    busta.setOre(getSingleOre(busta.getId()));
                    list.add(busta);
                } while (c.moveToNext());
            }
        }
        c.close();
        return list;
    }

    @Override
    public Map<String,Ore> getSingleOre(String id) {
        Map<String,Ore> ore = new HashMap<String,Ore>();

        Ore ora = null;
        String sql = IOreDAO.SELECT_BY_PK;
        Cursor c = db.rawQuery(sql, new String[] { id });


        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    ora = new Ore(c.getString(c.getColumnIndex("ID")));
                    ora.setGod(c.getFloat(c.getColumnIndex("F_GO")));
                    ora.setSpe(c.getFloat(c.getColumnIndex("F_SP")));
                    ora.setRes(c.getFloat(c.getColumnIndex("F_RE")));
                    ore.put("F", ora);

                    ora = new Ore(c.getString(c.getColumnIndex("ID")));
                    ora.setGod(c.getFloat(c.getColumnIndex("B_GO")));
                    ora.setSpe(c.getFloat(c.getColumnIndex("B_SP")));
                    ora.setRes(c.getFloat(c.getColumnIndex("B_RE")));
                    ore.put("R", ora);

                    ora = new Ore(c.getString(c.getColumnIndex("ID")));
                    ora.setGod(c.getFloat(c.getColumnIndex("R_GO")));
                    ora.setSpe(c.getFloat(c.getColumnIndex("R_SP")));
                    ora.setRes(c.getFloat(c.getColumnIndex("R_RE")));
                    ore.put("B", ora);

                } while (c.moveToNext());
            }
        }
        c.close();

        // return contact list
        return ore;
    }

    @Override
    public void insertOre(Map<String,Ore> ore, String id) {
        ContentValues newValues = new ContentValues();
        newValues.put("ID", id);
        for (Map.Entry<String, Ore> entry : ore.entrySet()) {
            newValues.put(entry.getKey().concat("_GO"), entry.getValue().getGod());
            newValues.put(entry.getKey().concat("_SP"), entry.getValue().getSpe());
            newValues.put(entry.getKey().concat("_RE"), entry.getValue().getRes());
        }
        // Insert the row into your table
        db.insert("BUSTA_ORE", null, newValues);
    }

    @Override
    public void insertVoci(Voci v) {
        ContentValues newValues = new ContentValues();
        // Assign values for each column.
        newValues.put("ID",      v.getId());
        newValues.put("POS",     v.getPos());
        newValues.put("IDVOCE",  v.getIdVoce());
        newValues.put("IMPORTO", v.getImporto());
        newValues.put("SHKZG",   v.getShkzg());
        // Insert the row into your table
        db.insert("BUSTA_VOCI", null, newValues);

    }

    @Override
    public Voci getSingleVoci(String id, int i) {
        Voci voce = null;
        String sql = IVociDAO.SELECT_BY_PK;
        Cursor c = db.rawQuery(sql, new String[] { id , ((Integer)i).toString()});

        if (c != null) {
            if (c.moveToFirst()) {
                voce = new Voci();
                do {
                    voce.setId      (c.getString(c.getColumnIndex("ID")));
                    voce.setPos     (c.getInt(c.getColumnIndex("POS")));
                    voce.setIdVoce  (c.getInt(c.getColumnIndex("IDVOCE")));
                    voce.setImporto (c.getFloat(c.getColumnIndex("IMPORTO")));
                    voce.setShkzg   (c.getString(c.getColumnIndex("SHKZG")));
                } while (c.moveToNext());
            }
        }
        // return contact list
        c.close();
        return voce;

    }

    @Override
    public List<Voci> getAllVoci(String id) {
        String sql = IVociDAO.SELECT_ALL;
        List<Voci> list = new ArrayList<Voci>();
        Voci voci = null;
        Cursor c = db.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    voci.setId      (c.getString(c.getColumnIndex("ID")));
                    voci.setPos     (c.getInt(c.getColumnIndex("POS")));
                    voci.setIdVoce  (c.getInt(c.getColumnIndex("IDVOCE")));
                    voci.setImporto (c.getFloat(c.getColumnIndex("IMPORTO")));
                    voci.setShkzg   (c.getString(c.getColumnIndex("SHKZG")));
                    list.add(voci);
                } while (c.moveToNext());
            }
        }
        c.close();
        return list;
    }
}

