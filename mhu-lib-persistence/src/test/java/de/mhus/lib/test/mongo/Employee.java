package de.mhus.lib.test.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import de.mhus.lib.adb.Persistable;

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
    private List<Employee> directReports;
    @Property("wage")
    private Double salary;
    
}
