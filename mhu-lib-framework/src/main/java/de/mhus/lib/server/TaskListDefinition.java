package de.mhus.lib.server;

public class TaskListDefinition implements Comparable<TaskListDefinition> {

	private String name;
	private String description;
	private Class<? extends Task>[] tasks;

	public TaskListDefinition(String name, String description, Class<? extends Task>[] tasks) {
		this.name = name;
		this.description = description;
		this.tasks = tasks;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Class<? extends Task>[] getTasks() {
		return tasks;
	}
	
	@Override
	public String toString() {
		return name + ": " + description;
	}
	
	@Override
	public int compareTo(TaskListDefinition in) {
		return toString().compareTo(String.valueOf(in));
	}
	
}
