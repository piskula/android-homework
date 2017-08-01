package sk.piskula.employees.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 1.8.2017
 */
@Dao
public interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEmployee(Employee employee);

    @Query("SELECT * FROM employee")
    List<Employee> getAlLEmployees();

    @Query("SELECT DISTINCT department FROM employee")
    List<String> getAllDepartments();

    @Query("SELECT * FROM employee WHERE department = :department")
    List<Employee> getEmployeesOfDepartment(String department);

}
