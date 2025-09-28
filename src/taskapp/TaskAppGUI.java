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
    private DefaultComboBoxModel<String> groupModel = new DefaultComboBoxModel<>();
    private JComboBox<String> groupFilterCombo;

    private static final Color PRIMARY = new Color(33, 150, 243); // Blue accent
    private static final Color BACKGROUND = new Color(245, 247, 250); // Light blue
    private static final Color CARD_BG = Color.WHITE;
    private static final Color CARD_BORDER = new Color(220, 220, 220); // Light grey
    private static final Color GROUP_BG = new Color(236, 239, 241); // Darkened light grey

    public TaskAppGUI(TaskManager manager) {
    	groupModel.addElement("No group");
        this.taskManager = manager;
        createGUI();
    }

    /**
     * Sets up the main GUI components.
     */
    private void createGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("Task & Reminder App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 520);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        frame.setContentPane(mainPanel);

        JLabel title = new JLabel("My Tasks", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(PRIMARY);
        mainPanel.add(title, BorderLayout.NORTH);

        // List setup
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCardRenderer());
        taskList.setBackground(BACKGROUND);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input
        taskField = new JTextField();
        taskField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taskField.setPreferredSize(new Dimension(0, 40));
        taskField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 2, true),
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
        styleAccentButton(addButton);

        JButton removeButton = new JButton("Remove Selected");
        styleButton(removeButton);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(CARD_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        inputPanel.add(taskField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Group filter panel
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(GROUP_BG);
        groupPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel groupLabel = new JLabel("Groups:");
        groupLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        groupLabel.setForeground(PRIMARY);
        groupPanel.add(groupLabel, BorderLayout.NORTH);

        groupFilterCombo = new JComboBox<>(groupModel);
        groupFilterCombo.insertItemAt("All groups", 0);
        groupFilterCombo.setSelectedIndex(0);
        groupFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        groupFilterCombo.setBackground(Color.WHITE);
        groupFilterCombo.setForeground(Color.DARK_GRAY);
        groupFilterCombo.addActionListener(e -> refreshTasks());

        groupPanel.add(groupFilterCombo, BorderLayout.CENTER);

        mainPanel.add(groupPanel, BorderLayout.WEST);

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
            String group = task.getGroup();
            if (group != null && groupModel.getIndexOf(group) == -1) {
                groupModel.addElement(group);
            }
        }

        frame.setVisible(true);
    }

    // Style for accent buttons
    private void styleAccentButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Style for normal buttons
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(224, 224, 224));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        // Card layout
        public TaskCardRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 10, 10, 10)
            ));
            setBackground(CARD_BG);

            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            descLabel.setForeground(new Color(120, 120, 120));
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

        // Render each task as a card
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
                setBackground(new Color(232, 244, 253));
            } else {
                setBackground(CARD_BG);
            }
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isSelected ? PRIMARY : CARD_BORDER, 2, true),
                new EmptyBorder(10, 10, 10, 10)
            ));
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

        // Group field
        JComboBox<String> groupCombo = new JComboBox<>(groupModel);
        groupCombo.setEditable(true);

        inputPanel.add(descField);
        inputPanel.add(groupCombo);

        dialog.add(inputPanel, BorderLayout.CENTER);

        
        // Add Button
        JButton addBtn = new JButton("Add Task");
        styleAccentButton(addBtn);

        // Button logic
        addBtn.addActionListener(e -> {
            String desc = descField.getText().trim();
            Date date = (Date) dateSpinner.getValue();
            Date time = (Date) timeSpinner.getValue();

            java.time.LocalDateTime dueDate = java.time.LocalDateTime.of(
            		date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                    time.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime()
            );

            String group = (String) groupCombo.getSelectedItem();
            if (group == null || group.isEmpty()) {
                group = "No group";
            }

            // If it's a new group, add it to the dropdown model
            if (groupModel.getIndexOf(group) == -1) {
                groupModel.addElement(group);
            }

            // Creating the task
            Task task = new Task(name, desc.isEmpty() ? "No description" : desc, dueDate, group);

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

    // Order tasks based on group filter
    private void refreshTasks() {
    	listModel.clear();
        String selectedGroup = (String) groupFilterCombo.getSelectedItem();
        for (Task t : taskManager.getTask()) {
            if ("All groups".equals(selectedGroup) || t.getGroup().equals(selectedGroup)) {
                listModel.addElement(t);
            }
        }
    }


    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        new TaskAppGUI(manager);
    }
}
