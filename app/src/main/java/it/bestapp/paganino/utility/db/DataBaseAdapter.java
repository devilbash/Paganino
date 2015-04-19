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



    public Info getBusta(String id) {
        SQLiteDatabase db = this.getDatabaseInstance();
        Cursor c = db.rawQuery("SELECT ID,LORDO,LORDO_BASE,RITENUTE,TOTALE FROM BUSTA_PAGA WHERE name = ?",
                new String[] {id});
        Info i = null;
        if(c.moveToFirst()){
            do{
                i = new Info(id,
                        c.getFloat(c.getColumnIndex("LORDO")),
                        c.getFloat(c.getColumnIndex("LORDO_BASE")),
                        c.getFloat(c.getColumnIndex("RITENUTE")),
                        c.getFloat(c.getColumnIndex("TOTALE"))     );
            }while(c.moveToNext());
        }
        c.close();
        return i;
    }
}
