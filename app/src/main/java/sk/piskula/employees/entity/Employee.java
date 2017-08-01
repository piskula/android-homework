package sk.piskula.employees.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */
@Entity
public class Employee implements Parcelable {

    @PrimaryKey
    private Long id;

    private String firstName;
    private String lastName;
    private String department;
    private String avatar;

    // end of attributes

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        if (!(obj instanceof Employee))
            return false;

        Employee other = (Employee) obj;

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
        return "Employee{"
                + "id=" + id
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", department=" + department
                + ", avatar=" + avatar
                + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // TODO
    }

    public Employee() {
    }

    protected Employee(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        // TODO
//        this.vehicle = in.readParcelable(Vehicle.class.getClassLoader());
//        this.distanceFromLastFillUp = (Long) in.readValue(Long.class.getClassLoader());
//        this.fuelVolume = (BigDecimal) in.readSerializable();
//        this.fuelPricePerLitre = (BigDecimal) in.readSerializable();
//        this.fuelPriceTotal = (BigDecimal) in.readSerializable();
//        this.isFullFillUp = in.readByte() != 0;
//        this.fuelConsumption = (BigDecimal) in.readSerializable();
//        long tmpDate = in.readLong();
//        this.date = tmpDate == -1 ? null : new Date(tmpDate);
//        this.info = in.readString();
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel parcel) {
            return new Employee(parcel);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
