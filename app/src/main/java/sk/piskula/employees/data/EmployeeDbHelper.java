package sk.piskula.employees.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */
public class EmployeeDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = EmployeeDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 1;

    EmployeeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // String that contains the SQL statement to create the employees table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + EmployeeEntry.TABLE_NAME + " ("
                + EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmployeeEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + EmployeeEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + EmployeeEntry.COLUMN_DEPARTMENT + " TEXT NOT NULL, "
                + EmployeeEntry.COLUMN_AVATAR + " TEXT);";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
