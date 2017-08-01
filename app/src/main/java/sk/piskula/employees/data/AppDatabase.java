package sk.piskula.employees.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import sk.piskula.employees.data.dao.EmployeeDao;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 1.8.2017
 */
@Database(entities = {Employee.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract EmployeeDao employeeModel();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "tasks")
                 // Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                 // To simplify the exercise, allow queries on the main thread.
                 // Don't do this on a real app!
                 .allowMainThreadQueries()
                 .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
