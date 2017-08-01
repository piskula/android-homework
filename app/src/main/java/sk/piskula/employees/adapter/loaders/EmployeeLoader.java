package sk.piskula.employees.adapter.loaders;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.piskula.employees.data.AppDatabase;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */

public class EmployeeLoader extends AbstractAsyncTaskLoader<List<Employee>> {
    public static final int ID = 1;

    private Context context;

    public EmployeeLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<Employee> loadInBackground() {
        try {
            Thread.sleep(1000);
            AppDatabase database = AppDatabase.getDatabase(context);
            return database.employeeModel().getAlLEmployees();
        } catch (InterruptedException e) {
            Toast.makeText(context, "Error: Cannot load employees", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }
}
