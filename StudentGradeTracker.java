import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentGradeTracker extends JFrame {

    // Store student records using ArrayList
    private ArrayList<Student> students = new ArrayList<>();

    private JTextField nameField, usnField;
    private JTextField[] markFields;

    private DefaultTableModel tableModel;
    private JTable studentTable;

    private JLabel totalLabel;
    private JLabel averageLabel;
    private JLabel highestLabel;
    private JLabel lowestLabel;

    private final String[] subjects = {
        "Java", "Python", "DBMS", "Computer Networks", "Mathematics"
    };

    // Student class
    static class Student {
        String name;
        String usn;
        int[] marks;
        int total;
        double average;
        int highest;
        int lowest;
        String grade;

        Student(String name, String usn, int[] marks) {
            this.name = name;
            this.usn = usn;
            this.marks = marks.clone();

            total = 0;
            highest = marks[0];
            lowest = marks[0];

            for (int mark : marks) {
                total += mark;

                if (mark > highest) {
                    highest = mark;
                }

                if (mark < lowest) {
                    lowest = mark;
                }
            }

            average = (double) total / marks.length;

            if (average >= 90) {
                grade = "A+";
            } else if (average >= 80) {
                grade = "A";
            } else if (average >= 70) {
                grade = "B";
            } else if (average >= 60) {
                grade = "C";
            } else if (average >= 50) {
                grade = "D";
            } else if (average >= 40) {
                grade = "E";
            } else {
                grade = "F";
            }
        }
    }

    // Constructor
    public StudentGradeTracker() {

        setTitle("Student Grade Tracker");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Heading
        JLabel heading = new JLabel(
            "STUDENT GRADE TRACKER",
            JLabel.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(new Color(30, 65, 110));

        JLabel subtitle = new JLabel(
            "Student Marks Management and Performance Analysis",
            JLabel.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.setBackground(Color.WHITE);
        header.add(heading);
        header.add(subtitle);

        // Student details form
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
            BorderFactory.createTitledBorder("Enter Student Details")
        );

        nameField = new JTextField();
        usnField = new JTextField();

        markFields = new JTextField[5];

        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("USN:"));
        formPanel.add(usnField);

        for (int i = 0; i < subjects.length; i++) {

            markFields[i] = new JTextField();

            formPanel.add(
                new JLabel(subjects[i] + " Marks (0-100):")
            );

            formPanel.add(markFields[i]);
        }

        // Buttons
        JButton addButton = new JButton("Add Student");
        JButton clearButton = new JButton("Clear Fields");

        addButton.setBackground(new Color(35, 130, 85));
        addButton.setForeground(Color.WHITE);

        clearButton.setBackground(new Color(65, 100, 160));
        clearButton.setForeground(Color.WHITE);

        formPanel.add(addButton);
        formPanel.add(clearButton);

        // Student records table
        String[] columns = {
            "Name", "USN", "Java", "Python",
            "DBMS", "CN", "Maths", "Total",
            "Average", "Highest", "Lowest", "Grade"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(27);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));

        studentTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Student Grade Records")
        );

        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        summaryPanel.setBackground(Color.WHITE);

        summaryPanel.setBorder(
            BorderFactory.createTitledBorder("Performance Summary")
        );

        totalLabel = new JLabel("Total Students: 0");
        averageLabel = new JLabel("Class Average: 0.00");
        highestLabel = new JLabel("Highest Mark: 0");
        lowestLabel = new JLabel("Lowest Mark: 0");

        summaryPanel.add(totalLabel);
        summaryPanel.add(averageLabel);
        summaryPanel.add(highestLabel);
        summaryPanel.add(lowestLabel);

        // Bottom buttons
        JButton summaryButton = new JButton("Generate Summary Report");
        JButton deleteButton = new JButton("Delete Selected Student");

        summaryButton.setBackground(new Color(30, 100, 160));
        summaryButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(180, 55, 55));
        deleteButton.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setBackground(Color.WHITE);

        bottomPanel.add(summaryButton);
        bottomPanel.add(deleteButton);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(12, 12));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(summaryPanel, BorderLayout.SOUTH);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add student action
        addButton.addActionListener(e -> addStudent());

        // Clear fields action
        clearButton.addActionListener(e -> clearFields());

        // Delete student action
        deleteButton.addActionListener(e -> deleteStudent());

        // Summary report action
        summaryButton.addActionListener(e -> generateSummary());

        setVisible(true);
    }

    // Add student details
    private void addStudent() {

        String name = nameField.getText().trim();
        String usn = usnField.getText().trim();

        if (name.isEmpty() || usn.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter student name and USN."
            );
            return;
        }

        // Check duplicate USN
        for (Student student : students) {
            if (student.usn.equalsIgnoreCase(usn)) {
                JOptionPane.showMessageDialog(
                    this,
                    "This USN already exists!"
                );
                return;
            }
        }

        int[] marks = new int[5];

        try {

            for (int i = 0; i < 5; i++) {

                String input = markFields[i].getText().trim();

                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Enter marks for " + subjects[i]
                    );
                    return;
                }

                marks[i] = Integer.parseInt(input);

                if (marks[i] < 0 || marks[i] > 100) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Marks must be between 0 and 100."
                    );
                    return;
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter valid numeric marks."
            );
            return;
        }

        Student student = new Student(name, usn, marks);

        students.add(student);

        tableModel.addRow(new Object[] {
            student.name,
            student.usn,
            marks[0],
            marks[1],
            marks[2],
            marks[3],
            marks[4],
            student.total,
            String.format("%.2f", student.average),
            student.highest,
            student.lowest,
            student.grade
        });

        updateSummary();

        JOptionPane.showMessageDialog(
            this,
            "Student added successfully!\nGrade: " + student.grade
        );

        clearFields();
    }

    // Clear input fields
    private void clearFields() {

        nameField.setText("");
        usnField.setText("");

        for (JTextField field : markFields) {
            field.setText("");
        }
    }

    // Delete selected student
    private void deleteStudent() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a student to delete."
            );

            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            students.remove(modelRow);
            tableModel.removeRow(modelRow);

            updateSummary();
        }
    }

    // Update class summary
    private void updateSummary() {

        int count = students.size();

        if (count == 0) {

            totalLabel.setText("Total Students: 0");
            averageLabel.setText("Class Average: 0.00");
            highestLabel.setText("Highest Mark: 0");
            lowestLabel.setText("Lowest Mark: 0");

            return;
        }

        double classAverage = 0;
        int highestMark = -1;
        int lowestMark = 101;

        for (Student student : students) {

            classAverage += student.average;

            if (student.highest > highestMark) {
                highestMark = student.highest;
            }

            if (student.lowest < lowestMark) {
                lowestMark = student.lowest;
            }
        }

        classAverage = classAverage / count;

        totalLabel.setText("Total Students: " + count);

        averageLabel.setText(
            String.format("Class Average: %.2f", classAverage)
        );

        highestLabel.setText("Highest Mark: " + highestMark);
        lowestLabel.setText("Lowest Mark: " + lowestMark);
    }

    // Generate complete summary report
    private void generateSummary() {

        if (students.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No student records available."
            );

            return;
        }

        Student topper = students.get(0);
        int highestMark = -1;
        int lowestMark = 101;

        for (Student student : students) {

            if (student.average > topper.average) {
                topper = student;
            }

            if (student.highest > highestMark) {
                highestMark = student.highest;
            }

            if (student.lowest < lowestMark) {
                lowestMark = student.lowest;
            }
        }

        StringBuilder report = new StringBuilder();

        report.append("STUDENT GRADE TRACKER REPORT\n");
        report.append("--------------------------------\n");

        report.append("Total Students: ")
              .append(students.size()).append("\n");

        report.append(String.format(
            "Class Average: %.2f\n",
            getClassAverage()
        ));

        report.append("Highest Subject Mark: ")
              .append(highestMark).append("\n");

        report.append("Lowest Subject Mark: ")
              .append(lowestMark).append("\n");

        report.append("\nTOPPER DETAILS\n");
        report.append("--------------------------------\n");

        report.append("Name: ").append(topper.name).append("\n");
        report.append("USN: ").append(topper.usn).append("\n");

        report.append("Total Marks: ")
              .append(topper.total).append("/500\n");

        report.append(String.format(
            "Average: %.2f\n",
            topper.average
        ));

        report.append("Grade: ").append(topper.grade).append("\n");

        report.append("\nALL STUDENT RECORDS\n");
        report.append("--------------------------------\n");

        for (Student student : students) {

            report.append(student.name)
                  .append(" | ")
                  .append(student.usn)
                  .append(" | Average: ")
                  .append(String.format("%.2f", student.average))
                  .append(" | Grade: ")
                  .append(student.grade)
                  .append("\n");
        }

        JTextArea reportArea = new JTextArea(
            report.toString()
        );

        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane reportScroll = new JScrollPane(reportArea);
        reportScroll.setPreferredSize(new Dimension(550, 400));

        JOptionPane.showMessageDialog(
            this,
            reportScroll,
            "Student Summary Report",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private double getClassAverage() {

        double total = 0;

        for (Student student : students) {
            total += student.average;
        }

        return total / students.size();
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentGradeTracker();
        });
    }
}