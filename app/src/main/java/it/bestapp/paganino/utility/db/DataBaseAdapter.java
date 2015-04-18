package it.bestapp.paganino.utility.db;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import it.bestapp.paganino.adapter.bustapaga.Info;

public class DataBaseAdapter {
	static final String DATABASE_NAME = "config.db";

	public static final int DATABASE_VERSION = 1 ;

	
	// SQL Statement to create a new database.
	static final String[] DATABASE_CREATE = {
		"create table "
			+ "BUSTA_PAGA " + "( "
			+ "ID "      	+ "text UNIQUE primary key , "			
			+ "LORDO " 		+ "REAL, "			
			+ "LORDO_BASE "	+ "REAL, "
			+ "RITENUTE "	+ "REAL, "
			+ "TOTALE "		+ "REAL, "
			+ "STRAORD "	+ "REAL "			
			+ ");",
		"create table "
			+ "ORE " + "( "
			+ "ID"      	+ " text UNIQUE primary key, "			
			+ "F_GO" 		+ " REAL, "			
			+ "F_SP"		+ " REAL, "
			+ "F_RE"		+ " REAL, "						
			+ "R_GO" 		+ " REAL, "			
			+ "R_SP"		+ " REAL, "
			+ "R_RE"		+ " REAL, "			
			+ "B_GO" 		+ " REAL, "			
			+ "B_SP"		+ " REAL, "
			+ "B_RE"		+ " REAL"
			+ ");"/*,
		"create table "
			+ "BUSTA_DWLD " + "( "
			+ "ID "      	+ "text UNIQUE primary key , "		
			+ "DATA" 		+ "text "
			+ "CLOUD" 		+ "INTEGER "
			+ "DWNLD" 		+ "INTEGER "
			+ "GRAFI" 		+ "INTEGER "
			+ ");",*/
	};
			

	/* constants */
	public static final String RICORDAMI = "1";
	public static final String USERNAME  = "2";
	public static final String PATH 	 = "3";
	
	public static final String DAY 	 	 = "4";
	public static final String TIME 	 = "5";
	public static final String NOTIFY 	 = "6";
	public static final String ARCHI 	 = "7";
	public static final String LAST_PAGA = "8";
	public static final String DBOX 	 = "9";
//	public static final String DBUSER 	 = "10";
//	public static final String DBPSWD 	 = "11";
	
	public static final String NOT_EXIST = null;

	
	// Variable to hold the database instance
	public SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
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


    /*
    public void insertDWLD(String id, String data) {
        String[] values = data.split(",");

        ContentValues newValues = new ContentValues();
        newValues.put("ID", id);
        newValues.put("DATA",  values[0]);
        newValues.put("CLOUD", Integer.parseInt(values[1]));
        newValues.put("DWNLD", Integer.parseInt(values[2]));
        newValues.put("GRAFI", Integer.parseInt(values[3]));
     db.insert("BUSTA_DWLD", null, newValues);
    }
    /*
    public HashMap<String, BustaPagaDWLD> getAllDWLD() {
        HashMap<String, BustaPagaDWLD> list = new HashMap<String, BustaPagaDWLD>();

        int x;
        String data;
        boolean c;
        boolean a;
        boolean d;
        BustaPagaDWLD store;

        Cursor cursor = db.query("BUSTA_DWLD", null, null, null, null, null, null);
        if (cursor.getCount() < 1)
            return null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    data = cursor.getString(cursor.getColumnIndex("DATA"));

                    x = cursor.getInt(cursor.getColumnIndex("CLOUD"));
                    c =	(x == 1)? true : false;
                    x = cursor.getInt(cursor.getColumnIndex("DWNLD"));
                    d =	(x == 1)? true : false;
                    x = cursor.getInt(cursor.getColumnIndex("GRAFI"));
                    a =	(x == 1)? true : false;
                    store = new BustaPagaDWLD(data, c,a,d );

                    list.put(cursor.getString(cursor.getColumnIndex("ID")),store);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return list;
    }
    */
	public void insertBusta(Info i) {
		ContentValues newValues = new ContentValues();
		// Assign values for each column.
		newValues.put("ID", i.getID());
		newValues.put("LORDO", i.getLordo());
		newValues.put("LORDO_BASE", i.getLordoB());
		newValues.put("RITENUTE", i.getRitenute());
		newValues.put("TOTALE", i.getTotale());

		// Insert the row into your table
		db.insert("BUSTA_PAGA", null, newValues);
	}
	
	public List<Info> getAllBuste() {
		List<Info> list = new ArrayList<Info>();
		String id; 			
		float lordo ;
		float lordo_b;
		float ritenute;
		float totale ;			
		Info i;
		Cursor cursor = db.query("BUSTA_PAGA", null, null, null, null, null, null);
		
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					id       = cursor.getString(cursor.getColumnIndex("ID"));
					lordo    = cursor.getFloat(cursor.getColumnIndex("LORDO"));
					lordo_b  = cursor.getFloat(cursor.getColumnIndex("LORDO_BASE"));
					ritenute = cursor.getFloat(cursor.getColumnIndex("RITENUTE"));
					totale 	 = cursor.getFloat(cursor.getColumnIndex("TOTALE"));

					i = new Info(id, lordo, lordo_b, ritenute, totale);
					list.add(i); 
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		return list;
	}
	
	public float[] getOre(String chiave) {
		String id = null;
		float[] ore = null ;
		String[] whereArgs = new String[] {chiave};
	
		Cursor cursor = db.query("ORE", null, " ID =? " , whereArgs , null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				ore = new float[9] ; 
				do {
					id      = cursor.getString(cursor.getColumnIndex("ID"));
					ore[0]  = cursor.getFloat(cursor.getColumnIndex("F_SP"));
					ore[1]	= cursor.getFloat(cursor.getColumnIndex("F_GO"));
					ore[2]  = cursor.getFloat(cursor.getColumnIndex("F_RE"));
					ore[3]  = cursor.getFloat(cursor.getColumnIndex("R_SP"));
					ore[4]  = cursor.getFloat(cursor.getColumnIndex("R_GO"));
					ore[5]  = cursor.getFloat(cursor.getColumnIndex("R_RE"));
					ore[6]  = cursor.getFloat(cursor.getColumnIndex("B_SP"));
					ore[7]  = cursor.getFloat(cursor.getColumnIndex("B_GO"));
					ore[8]  = cursor.getFloat(cursor.getColumnIndex("B_RE"));
 				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		return ore;
	}

	public void setOre(String id, float ore []) {
		ContentValues newValues = new ContentValues();
		// Assign values for each column.
	
		newValues.put("ID", id);
		newValues.put("F_SP", ore[0]);
		newValues.put("F_GO", ore[1]);
		newValues.put("F_RE", ore[2]);
		newValues.put("R_SP", ore[3]);
		newValues.put("R_GO", ore[4]);
		newValues.put("R_RE", ore[5]);
		newValues.put("B_SP", ore[6]);
		newValues.put("B_GO", ore[7]);
		newValues.put("B_RE", ore[8]);

		db.insert("ORE", null, newValues);
	}	

	
	public void disconnetti() {
		db.delete("CONFIG", null, null);
		db.delete("ORE", null, null);
		db.delete("BUSTA_PAGA", null, null);
	}
	
	
	
}
