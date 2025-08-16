package taskapp;

import java.time.LocalDateTime;

public class Task {
	private String name;
	private String description;
	private LocalDateTime dueDate;

	/**
	 * Represents a single task.
	 * 
	 * @param name
	 * @param description
	 * @param dueDate
	 */
	public Task(String name, String description, LocalDateTime dueDate) {
		this.name = name;
		this.description = description;
		this.dueDate = dueDate;
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

	@Override
	public String toString() {
		if (dueDate != null) {
            return name + " - " + description + " (Due: " + dueDate + ")";
        } else {
            return name + " - " + description;
        }
	}
}
