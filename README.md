# TaskReminderApp

A simple desktop task reminder application written in pure Java.  
The app allows users to manage tasks, organize them by category, and sort them by due date.

## Features

- **Pure Java** — No external frameworks required; runs on any system with Java installed.
- **Task Management** — Add, edit, and delete tasks.
- **Ordering** — Tasks are automatically sorted by due date and category for easy viewing.
- **Simple GUI** — Launches a graphical interface for intuitive interaction.

> **Note:** This project is minimal and may lack advanced features found in other reminder apps.

## Getting Started

### Prerequisites

- **Java 8 or newer**  
  Make sure you have the Java Development Kit (JDK) installed.

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/shawn-abdool/TaskReminderApp.git
   ```
2. **Navigate to the project directory**
   ```bash
   cd TaskReminderApp
   ```

### Running the Application

Compile and run the main GUI class:

```bash
javac TaskAppGUI.java
java TaskAppGUI
```

Alternatively, you can compile all `.java` files:

```bash
javac *.java
java TaskAppGUI
```

## Usage

- **Add Tasks:** Use the GUI to create new tasks with a due date and category.
- **View & Sort:** Tasks will display ordered first by due date, then by category.
- **Edit/Delete:** Select a task in the list to edit or delete it.

## Project Structure

```
TaskReminderApp/
├── TaskAppGUI.java      # Main GUI application entry point
├── Task.java            # Task model
├── TaskManager.java     # Logic for managing tasks
└── ...                  # Other helper classes
```

## Contributing

Pull requests and suggestions are welcome!  
If you’d like to contribute, please fork the repository and submit a PR.

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

## Author

- [Shawn Abdool](https://github.com/shawn-abdool)
