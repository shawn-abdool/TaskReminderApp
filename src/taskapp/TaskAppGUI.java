package taskapp;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * A Swing GUI to display and add tasks.
 */
public class TaskAppGUI {
	private TaskManager taskManager;
	private JFrame frame;
	private DefaultListModel<Task> listModel;
	private JList<Task> taskList;

	public TaskAppGUI(TaskManager manager) {
		this.taskManager = manager;
		createGUI();
	}

	private void createGUI() {
		frame = new JFrame("Task & Reminder App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);

		listModel = new DefaultListModel<>();
		taskList = new JList<>(listModel);
		JScrollPane scrollPane = new JScrollPane(taskList);

		JTextField taskField = new JTextField();
		JButton addButton = new JButton("Add Task");

		// When the button is clicked.
		addButton.addActionListener(e -> {
			String name = taskField.getText();
			if(!name.isEmpty()) {
				Task task = new Task(name, "", null);
				taskManager.addTask(task);
				listModel.addElement(task);
				taskField.setText("");
			}
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(taskField, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(addButton, BorderLayout.SOUTH);

		frame.add(panel);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		new TaskAppGUI(manager);
	}

}
