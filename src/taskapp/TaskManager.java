package taskapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Add, remove and return tasks from a list.
 */
public class TaskManager {
	private List<Task> tasks;
	private static final String FILE_NAME = "tasks.csv";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public TaskManager() {
		tasks = new ArrayList<>();
	}

	public void addTask(Task task) {
		tasks.add(task);
		Collections.sort(tasks);
		saveTasks();
	}

	public void removeTask(Task task) {
		tasks.remove(task);
		saveTasks();
	}

	public List<Task> getTask() {
		return tasks;
	}

	public void saveTasks() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
			for (Task task : tasks) {
				String dueDate = task.getDueDate() != null ? task.getDueDate().format(formatter) : "";
				writer.printf("%s,%s,%s%n",
						escape(task.getName()),
						escape(task.getDescription()),
						dueDate);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadTasks() {
        tasks.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSV(line);
                if (parts.length >= 2) {
                    String name = parts[0];
                    String description = parts[1];
                    LocalDateTime dueDate = null;
                    if (parts.length >= 3 && !parts[2].isEmpty()) {
                        dueDate = LocalDateTime.parse(parts[2], formatter);
                    }
                    tasks.add(new Task(name, description, dueDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Escape commas and quotes
    private String escape(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // Parse a CSV line (handles quotes)
    private String[] parseCSV(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        values.add(sb.toString());
        return values.toArray(new String[0]);
    }
}
