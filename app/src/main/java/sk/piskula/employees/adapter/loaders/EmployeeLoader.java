package sk.piskula.employees.adapter.loaders;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sk.piskula.employees.data.EmployeeContract.EmployeeEntry;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */

public class EmployeeLoader extends AbstractAsyncTaskLoader<List<Employee>> {
    public static final int ID = 1;

    private Context context;
    private Set<String> departments;

    public EmployeeLoader(Context context, Set<String> departments) {
        super(context);
        this.context = context;
        this.departments = departments;
    }

    @Override
    public List<Employee> loadInBackground() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        // TODO respect selection
        String[] projection = {
                EmployeeEntry.COLUMN_LAST_NAME,
                EmployeeEntry.COLUMN_FIRST_NAME,
                EmployeeEntry.COLUMN_DEPARTMENT,
                EmployeeEntry.COLUMN_AVATAR
        };

        Cursor cursor = context.getContentResolver().query(
                EmployeeEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        int columnIndexId = cursor.getColumnIndex(EmployeeEntry._ID);
        int columnIndexLastName = cursor.getColumnIndex(EmployeeEntry.COLUMN_LAST_NAME);
        int columnIndexFirstName = cursor.getColumnIndex(EmployeeEntry.COLUMN_FIRST_NAME);
        int columnIndexAvatar = cursor.getColumnIndex(EmployeeEntry.COLUMN_AVATAR);
        int columnIndexDepartment = cursor.getColumnIndex(EmployeeEntry.COLUMN_DEPARTMENT);

        List<Employee> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Employee employee = new Employee();

            // TODO repair employee.setId(cursor.getLong(columnIndexId));
            employee.setLastName(cursor.getString(columnIndexLastName));
            employee.setFirstName(cursor.getString(columnIndexFirstName));
            employee.setDepartment(cursor.getString(columnIndexDepartment));
            employee.setAvatar(cursor.getString(columnIndexAvatar));

            result.add(employee);
        }
        cursor.close();
        return result;
    }

}
