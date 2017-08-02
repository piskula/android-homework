package sk.piskula.employees.adapter.loaders;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sk.piskula.employees.data.AppDatabase;
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
            Thread.sleep(1000);
            AppDatabase database = AppDatabase.getDatabase(context);
            if (departments.isEmpty())
                return database.employeeModel().getAlLEmployees();
            else {
                List<Employee> result = new ArrayList<>();
                for (String department : departments)
                    result.addAll(database.employeeModel().getEmployeesOfDepartment(department));

                return result;
            }
        } catch (InterruptedException e) {
            Toast.makeText(context, "Error: Cannot load employees", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }

}
