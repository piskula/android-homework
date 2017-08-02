package sk.piskula.employees.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Ondrej Oravcok
 * @version 2.8.2017
 */

public class EmployeeContract {

    private EmployeeContract() {
    }

    public static final String CONTENT_AUTHORITY = "sk.piskula.employees";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_EMPLOYEES = "employees";

    /**
     * Inner class that defines constant values for the employees database table.
     * Each entry in the table represents a single employee.
     */
    public static final class EmployeeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EMPLOYEES);
        public static final String TABLE_NAME = "employees";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FIRST_NAME ="first_name";
        public final static String COLUMN_LAST_NAME = "last_name";
        public final static String COLUMN_DEPARTMENT = "department";
        public final static String COLUMN_AVATAR = "avatar";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEES;

    }

}
