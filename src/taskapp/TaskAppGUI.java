package taskapp;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("Task & Reminder App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        frame.setContentPane(mainPanel);

        JLabel title = new JLabel("My Tasks", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainPanel.add(title, BorderLayout.NORTH);

        // List setup
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCardRenderer());
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input
        taskField = new JTextField();
        taskField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taskField.setPreferredSize(new Dimension(0, 40));
        taskField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        
        taskField.setText("Enter a task...");
        taskField.setForeground(Color.GRAY);

        // Focus listener to handle placeholder text
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

        JButton addButton = new JButton("Add");
        styleButton(addButton);

        JButton removeButton = new JButton("Remove Selected");
        styleButton(removeButton);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(taskField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Button logic
        addButton.addActionListener(e -> {
        	String name = taskField.getText().trim();
        	if (!name.isEmpty()) {
        		showAddTaskDialog(name);
        	}
        });

        removeButton.addActionListener(e -> {
            Task selected = taskList.getSelectedValue();
            if (selected != null) {
                taskManager.removeTask(selected);
                listModel.removeElement(selected);
            }
        });

        // Load saved tasks
        taskManager.loadTasks();
        for (Task task : taskManager.getTask()) {
            listModel.addElement(task);
        }

        frame.setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBackground(null);
        btn.setForeground(null);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Custom renderer = task card
    private static class TaskCardRenderer extends JPanel implements ListCellRenderer<Task> {
    	// Default serial ID
		private static final long serialVersionUID = 1L;
		private JLabel nameLabel = new JLabel();
        private JLabel descLabel = new JLabel();
        private JLabel dateLabel = new JLabel();

        private DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm");

        public TaskCardRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(new EmptyBorder(10, 10, 10, 10));

            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            descLabel.setForeground(Color.GRAY);
            dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dateLabel.setForeground(new Color(100, 100, 100));

            JPanel textPanel = new JPanel(new GridLayout(0, 1));
            textPanel.setOpaque(false);
            textPanel.add(nameLabel);
            textPanel.add(descLabel);
            textPanel.add(dateLabel);

            add(textPanel, BorderLayout.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            nameLabel.setText(task.getName());
            descLabel.setText(task.getDescription() == null ? "" : task.getDescription());
            if (task.getDueDate() != null) {
                dateLabel.setText(task.getDueDate().format(formatter));
            } else {
                dateLabel.setText("No due date");
            }

            if (isSelected) {
                setBackground(new Color(200, 220, 250));
            } else {
                setBackground(Color.WHITE);
            }
            setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true));
            return this;
        }
    }

    private void showAddTaskDialog(String name) {
        JDialog dialog = new JDialog(frame, "Add Task", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setResizable(false);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        inputPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Description Field
        JTextField descField = new JTextField("Enter description...");
        descField.setForeground(Color.GRAY);
        descField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addPlaceholderBehavior(descField, "Enter description...");

        //Pick date
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = (new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setEditor(dateEditor);
        dateEditor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Due Date Field
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = (new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        timeSpinner.setEditor(timeEditor);
        timeEditor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 16));

        //timeEditor.getTextField().setFont(new Font("Arial", Font.PLAIN, 16));

        inputPanel.add(descField);
        //inputPanel.add(dateField);

        dialog.add(inputPanel, BorderLayout.CENTER);

        // Add Button
        JButton addBtn = new JButton("Add Task");
        addBtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addBtn.addActionListener(e -> {
            String desc = descField.getText().trim();
            //String dateStr = dateField.getText().trim();
            //LocalDateTime due = null;
            Date date = (Date) dateSpinner.getValue();
            Date time = (Date) timeSpinner.getValue();

            java.time.LocalDateTime dueDate = java.time.LocalDateTime.of(
            		date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                    time.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime()
            );

            Task task = new Task(name, desc.isEmpty() ? "No description" : desc, dueDate);

            taskManager.addTask(task);
            listModel.addElement(task);
            refreshTasks();
            taskField.setText("");
            dialog.dispose();
        });

        inputPanel.add(descField);
        inputPanel.add(dateSpinner);
        inputPanel.add(timeSpinner);
        inputPanel.add(addBtn);
        dialog.add(addBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Helper method to handle placeholder behavior
    private void addPlaceholderBehavior(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void refreshTasks() {
    	listModel.clear();
    	for (Task t : taskManager.getTask()) {
    		listModel.addElement(t);
    	}
    }


    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        new TaskAppGUI(manager);
    }
}
