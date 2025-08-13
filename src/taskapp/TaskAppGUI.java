package taskapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * A Swing GUI to display and add tasks.
 */
public class TaskAppGUI {
	private TaskManager taskManager;
	private JFrame frame;
	private DefaultListModel<Task> listModel;
	private JList<Task> taskList;
	private JTextField taskField;

	public TaskAppGUI(TaskManager manager) {
		this.taskManager = manager;
		createGUI();
	}

	private void createGUI() {
		frame = new JFrame("Task & Reminder App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);

		listModel = new DefaultListModel<>();
		taskList = new JList<>(listModel);
		JScrollPane scrollPane = new JScrollPane(taskList);

		
		taskField = new JTextField("Enter a task...");
		taskField.setPreferredSize(new Dimension(0, 35));
        taskField.setForeground(Color.GRAY);
        taskField.setFont(new Font("Segoe UI", Font.PLAIN, 18)); // bigger text, taller bar


        taskField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (taskField.getText().equals("Enter a task...")) {
                    taskField.setText("");
                    taskField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (taskField.getText().isEmpty()) {
                    taskField.setForeground(Color.GRAY);
                    taskField.setText("Enter a task...");
                }
            }
        });

        taskField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (taskField.getText().equals("Enter a task...")) {
                    taskField.setText("");
                    taskField.setForeground(Color.BLACK);
                }
            }
        });
        
		JButton addButton = new JButton("Add Task");

		// When the button is clicked.
		addButton.addActionListener(e -> {
			String name = taskField.getText().trim();
			if(!name.isEmpty() && !name.equals("Enter a task...")) {
				JPanel dialogPanel = new JPanel(new BorderLayout(5,5));
				
				JTextField descField = new JTextField();
				descField.setBorder(BorderFactory.createTitledBorder("Description"));
				
				JTextField dateField = new JTextField();
				dateField.setBorder(BorderFactory.createTitledBorder("Due Date [yyyy-MM-dd HH:mm]"));
				
				JPanel fields = new JPanel(new GridLayout(2,1));
				fields.add(descField);
				fields.add(dateField);
				
				dialogPanel.add(fields, BorderLayout.CENTER);
				
				int result = JOptionPane.showConfirmDialog(
						frame,
						dialogPanel,
						"Add Task Details",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE
				);

				if (result == JOptionPane.OK_OPTION) {
					String description = descField.getText().trim();
		            String dateInput = dateField.getText().trim();

		            LocalDateTime dueDate = null;
		            if (!dateInput.isEmpty()) {
		                try {
		                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		                    dueDate = LocalDateTime.parse(dateInput, formatter);
		                } catch (DateTimeParseException ex) {
		                    JOptionPane.showMessageDialog(frame,
		                            "Invalid date format!\nPlease use: yyyy-MM-dd HH:mm",
		                            "Error",
		                            JOptionPane.ERROR_MESSAGE);
		                    return; // stop adding task if date is wrong
		                }
		            }

		            Task task = new Task(name, description, dueDate);
		            taskManager.addTask(task);
		            listModel.addElement(task);
		            taskField.setText("");
		        }
			}
				/*
				Task task = new Task(name, "", null);
				taskManager.addTask(task);
				listModel.addElement(task);
			}
				//taskField.setText("");
				taskField.setForeground(Color.GRAY);
				taskField.setText("Enter a task...");
				*/
			
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(taskField, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(addButton, BorderLayout.SOUTH);

		frame.add(panel);
		frame.setVisible(true);
		SwingUtilities.invokeLater(() -> addButton.requestFocusInWindow());
	}

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		new TaskAppGUI(manager);
	}

}
