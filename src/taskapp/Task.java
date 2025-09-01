package taskapp;

import java.time.LocalDateTime;

public class Task implements Comparable<Task>{
	private String name;
	private String description;
	private LocalDateTime dueDate;
	private String group;

	/**
	 * Represents a single task.
	 * 
	 * @param name
	 * @param description
	 * @param dueDate
	 */
	public Task(String name, String description, LocalDateTime dueDate, String group) {
		this.name = name;
		this.description = description;
		this.dueDate = dueDate;
		this.group = (group == null || group.isEmpty()) ? "No group" : group;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = (group == null || group.isEmpty()) ? "No Group" : group;
	}

	@Override
	public int compareTo(Task other) {
		if (this.dueDate == null && other.dueDate == null) return 0;
		if (this.dueDate == null) return 1;
		if (other.dueDate == null) return -1;
		return this.dueDate.compareTo(other.dueDate);
		
	}

	@Override
	public String toString() {
		if (dueDate != null) {
            return name + " - " + description + " (Due: " + dueDate + ")";
        } else {
            return name + " - " + description;
        }
	}
}
