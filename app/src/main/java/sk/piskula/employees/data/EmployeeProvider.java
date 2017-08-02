package sk.piskula.employees.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */

public class EmployeeProvider extends ContentProvider {

    public static final String LOG_TAG = EmployeeProvider.class.getSimpleName();

    private static final int EMPLOYEES = 100;
    private static final int EMPLOYEE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY, EmployeeContract.PATH_EMPLOYEES, EMPLOYEES);
        sUriMatcher.addURI(EmployeeContract.CONTENT_AUTHORITY, EmployeeContract.PATH_EMPLOYEES + "/#", EMPLOYEE_ID);
    }

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/employee";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/employee";

    private EmployeeDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new EmployeeDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEES:
                if (projection != null && projection.length == 1
                        && projection[0].equals(EmployeeEntry.COLUMN_DEPARTMENT)) {
                    cursor = database.query(true, EmployeeEntry.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, sortOrder, null);
                } else {
                    cursor = database.query(EmployeeEntry.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, sortOrder);
                }
                break;
            case EMPLOYEE_ID:
                selection = EmployeeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(EmployeeEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEES:
                return EmployeeEntry.CONTENT_LIST_TYPE;
            case EMPLOYEE_ID:
                return EmployeeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EMPLOYEES:
                return insertEmployee(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    private Uri insertEmployee(Uri uri, ContentValues values) {

        String firstName = values.getAsString(EmployeeEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("Employee requires a first name.");
        }

        String lastName = values.getAsString(EmployeeEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("Employee requires a last name.");
        }

        String department = values.getAsString(EmployeeEntry.COLUMN_DEPARTMENT);
        if (department == null) {
            throw new IllegalArgumentException("Employee requires a department.");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(EmployeeEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }
}
