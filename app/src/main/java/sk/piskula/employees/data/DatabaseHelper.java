package sk.piskula.employees.data;

import android.content.Context;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import sk.piskula.employees.R;
import sk.piskula.employees.entity.Department;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Department, Long> departmentDao;
    private Dao<Employee, Long> employeeDao;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once
            // of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, Department.class);
            TableUtils.createTable(connectionSource, Employee.class);
            Log.i(TAG, "Tables created successfully.");
        } catch (SQLException e) {
            Log.e(TAG, "Unable to create database.", e);
        }

        try {
            initSamlpeData();
            Log.i(TAG, "Sample data initialized.");
        } catch (SQLException e) {
            Log.e(TAG, "Unable to create sample data.", e);
        }
    }

    private void initSamlpeData() throws SQLException {
        List<Department> departments = SampleDataUtils.addSampleDepartments(getDepartmentDao());
        SampleDataUtils.addSampleEmployees(getEmployeeDao(), departments);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application,
            // please increase the value of DATABASE_VERSION variable, then this method will
            // be invoked automatically. Developer needs to handle the upgrade logic here, i.e.
            // create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Department.class, true);
            TableUtils.dropTable(connectionSource, Employee.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(TAG, "Unable to upgrade database from version " + oldVer + " to new " + newVer, e);
        }
    }

    public Dao<Employee, Long> getEmployeeDao() {
        if (employeeDao == null) {
            try {
                employeeDao = getDao(Employee.class);
            } catch (SQLException e) {
                Log.i(TAG, "Can not create employee dao.");
            }
        }
        return employeeDao;
    }

    public Dao<Department, Long> getDepartmentDao() {
        if (departmentDao == null) {
            try {
                departmentDao = getDao(Department.class);
            } catch (SQLException e) {
                Log.i(TAG, "Can not create department dao.");
            }
        }
        return departmentDao;
    }

}
