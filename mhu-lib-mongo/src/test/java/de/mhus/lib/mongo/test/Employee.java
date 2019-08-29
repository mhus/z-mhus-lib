package de.mhus.lib.mongo.test;


import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
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
    public UUID getId() {
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

    @DbPrimaryKey
    private UUID id;
    private String name;
    @Reference
    private Employee manager;
    @Reference
    private List<Employee> directReports = new LinkedList<Employee>();
    @DbPersistent
    private Double salary;
    
}