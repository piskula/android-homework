package sk.piskula.employees.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author Ondrej Oravcok
 * @version 31.7.2017
 */
@DatabaseTable(tableName = "department")
public class Department implements Serializable {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String name;

    // end of attributes

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Department))
            return false;

        Department other = (Department) obj;
        return name != null ? name.equals(other.name) : other.name == null;
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public String toString() {
        return "Department{"
                + "id=" + id
                + ", name=" + name
                + "}";
    }

}
