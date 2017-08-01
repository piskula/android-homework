package sk.piskula.employees.data;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.piskula.employees.entity.Department;
import sk.piskula.employees.entity.Employee;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */

public class SampleDataUtils {

    static List<Department> addSampleDepartments(Dao<Department, Long> departmentDao) throws SQLException {
        List<String> departments = Arrays.asList("RD", "HR", "FN");
        List<Department> result = new ArrayList<>();
        for (String name : departments) {
            departmentDao.create(new Department(name));
            result.add(departmentDao.queryBuilder().where().eq("name", name).query().get(0));
        }
        return Collections.unmodifiableList(result);
    }

    static void addSampleEmployees(Dao<Employee, Long> employeeDao, List<Department> departments) throws SQLException {
        List<Department> departmentsSorted = new ArrayList<>(departments);
        Collections.sort(departmentsSorted, new Comparator<Department>() {
            @Override
            public int compare(Department d1, Department d2) {
                return d1.getName().compareTo(d2.getName());
            }
        });

        // RD
        employeeDao.create(createEmployee("John", "Smith", departmentsSorted.get(2), "http://icons.iconarchive.com/icons/bevel-and-emboss/character/64/chef-icon.png"));
        employeeDao.create(createEmployee("Joe", "Smith", departmentsSorted.get(2), "http://icons.iconarchive.com/icons/martin-berube/people/64/chef-icon.png"));
        employeeDao.create(createEmployee("Peter", "Frank", departmentsSorted.get(2), "http://icons.iconarchive.com/icons/martin-berube/character/64/Chef-icon.png"));

        // HR
        employeeDao.create(createEmployee("John", "Doe", departmentsSorted.get(1), "http://icons.iconarchive.com/icons/martin-berube/character/64/Doctor-icon.png"));
        employeeDao.create(createEmployee("Anna", "Smith", departmentsSorted.get(1), "http://icons.iconarchive.com/icons/martin-berube/character/64/Snowman-icon.png"));
        employeeDao.create(createEmployee("Peter", "Jones", departmentsSorted.get(1), "http://icons.iconarchive.com/icons/martin-berube/character/64/Santa-icon.png"));

        // FN
        employeeDao.create(createEmployee("John", "King", departmentsSorted.get(0), "http://icons.iconarchive.com/icons/martin-berube/character/64/Angel-icon.png"));
        employeeDao.create(createEmployee("Anna", "Smith", departmentsSorted.get(0), "http://icons.iconarchive.com/icons/martin-berube/character/64/Doctor-icon.png"));
        employeeDao.create(createEmployee("Peter", "Falk", departmentsSorted.get(0), "http://icons.iconarchive.com/icons/martin-berube/character/64/Chef-icon.png"));
    }

    private static Employee createEmployee(String firstName, String lastName, Department department, String avatar) {
        Employee employee = new Employee();

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(department);
        employee.setAvatar(avatar);

        return employee;
    }

}
