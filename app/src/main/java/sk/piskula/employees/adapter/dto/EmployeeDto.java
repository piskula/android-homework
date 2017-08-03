package sk.piskula.employees.adapter.dto;

import java.util.Objects;

/**
 * @author Ondrej Oravcok
 * @version 3.8.2017
 */
public class EmployeeDto {

    private int id;
    private String firstName;
    private String lastName;
    private String department;
    private String avatar;

    // end of attributes

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EmployeeDto))
            return false;

        EmployeeDto other = (EmployeeDto) obj;

        if (firstName != null ? !firstName.equals(other.firstName) : other.firstName != null) return false;
        if (lastName != null ? !lastName.equals(other.lastName) : other.lastName != null) return false;
        if (department != null ? !department.equals(other.department) : other.department != null) return false;
        if (avatar != null ? !avatar.equals(other.avatar) : other.avatar != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, department);
    }

    @Override
    public String toString() {
        return "EmployeeDto{"
                + "id=" + id
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", department=" + department
                + ", avatar=" + avatar
                + "}";
    }

}
