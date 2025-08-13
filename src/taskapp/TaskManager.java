package taskapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Add, remove and return tasks from a list.
 */
public class TaskManager {
	private List<Task> tasks;

	public TaskManager() {
		tasks = new ArrayList<>();
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void removeTask(Task task) {
		tasks.remove(task);
	}

	public List<Task> getTask() {
		return tasks;
	}

}
