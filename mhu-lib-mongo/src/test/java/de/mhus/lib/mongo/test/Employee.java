package de.mhus.lib.mongo.test;


import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

import de.mhus.lib.adb.Persistable;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;

@Entity("employees")
@Indexes(
    @Index(value = "salary", fields = @Field("salary"))
)
public class Employee implements Persistable {
    
    public Employee() {}

    public Employee(String name, double d) {
        this.setName(name);
        this.setSalary(d);
    }
    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Id
    private ObjectId id;
    private String name;
    @Reference
    private Employee manager;
    @Reference
    private List<Employee> directReports = new LinkedList<Employee>();
    @Property("wage")
    private Double salary;
    
}