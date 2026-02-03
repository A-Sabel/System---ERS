package ers.group;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ScoreTab extends JPanel {

    private JPanel leftPanel, rightPanel;
    private JTextField studentIdField, studentNameField, schoolYearField, semesterField;
    private java.util.List<JComboBox<String>> courseDropdowns = new ArrayList<>();
    private java.util.List<JTextField> courseScores = new ArrayList<>();
    private JTable table;
    private DefaultTableModel model;

    // 20 possible courses
    private String[] courseOptions = {
        "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems",
        "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies",
        "Linear Algebra for Computing", "Human-Computer Interaction", "Data Structures and Algorithms",
        "Design and Analysis of Algorithms", "Database Management Systems", "Computer Networks",
        "Advanced Programming Concepts", "Operating Systems", "Compiler Design", "Information Security",
        "Distributed Systems", "Systems Integration Project"
    };

    public ScoreTab() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(31, 58, 95));

        initLeftPanel();
        initRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void initLeftPanel() {
    leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBackground(new Color(0, 30, 58));
    leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
    leftPanel.setPreferredSize(new Dimension(460, 703));

    Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
    Color labelColor = Color.WHITE;
    Color fieldBg = new Color(146, 190, 219);

    // Student ID
    leftPanel.add(makeRow("Student ID", labelFont, labelColor, fieldBg));
    studentIdField = (JTextField) ((JPanel) leftPanel.getComponent(leftPanel.getComponentCount()-1)).getComponent(1);

    // Student Name
    leftPanel.add(makeRow("Student Name", labelFont, labelColor, fieldBg));
    studentNameField = (JTextField) ((JPanel) leftPanel.getComponent(leftPanel.getComponentCount()-1)).getComponent(1);


    // School Year
    leftPanel.add(makeRow("Year Level", labelFont, labelColor, fieldBg));
    schoolYearField = (JTextField) ((JPanel) leftPanel.getComponent(leftPanel.getComponentCount()-1)).getComponent(1);

    // Semester
    leftPanel.add(makeRow("Semester", labelFont, labelColor, fieldBg));
    semesterField = (JTextField) ((JPanel) leftPanel.getComponent(leftPanel.getComponentCount()-1)).getComponent(1);

    // 5 Course rows
    for (int i = 1; i <= 5; i++) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(0, 30, 58));

        JLabel label = new JLabel("Course " + i);
        label.setFont(labelFont);
        label.setForeground(labelColor);
        label.setPreferredSize(new Dimension(120, 32));

        JComboBox<String> dropdown = new JComboBox<>(courseOptions);
        dropdown.setPreferredSize(new Dimension(220, 32));
        dropdown.setBackground(fieldBg);
        dropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField scoreField = new JTextField("0.0", 5);
        scoreField.setPreferredSize(new Dimension(60, 32));
        scoreField.setBackground(fieldBg);
        scoreField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        row.add(label);
        row.add(dropdown);
        row.add(scoreField);

        leftPanel.add(row);

        courseDropdowns.add(dropdown);
        courseScores.add(scoreField);
        }
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(0, 30, 58));
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 4, true));

    // Search section (TOP)
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    searchPanel.setBackground(new Color(0, 30, 58));

    JLabel searchLabel = new JLabel("Search Student");
    searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    searchLabel.setForeground(Color.WHITE);

    // Student ID field
    JTextField searchIdField = makeField("", new Color(146, 190, 219));
    searchIdField.setPreferredSize(new Dimension(150, 32));

    // Semester field
    JTextField searchSemesterField = makeField("", new Color(146, 190, 219));
    searchSemesterField.setPreferredSize(new Dimension(100, 32));

    // Year Level field
    JTextField searchYearField = makeField("", new Color(146, 190, 219));
    searchYearField.setPreferredSize(new Dimension(100, 32));

    // Search button
    JButton searchBtn = makeButton("Search");
    searchBtn.addActionListener(e -> {
        String searchId = searchIdField.getText();
        String searchSemester = searchSemesterField.getText();
        String searchYear = searchYearField.getText();
        searchStudentData(searchId, searchSemester, searchYear);
    });

    // Refresh button
    JButton refreshBtn = makeButton("Refresh");
    refreshBtn.addActionListener(e -> {
        model.setRowCount(0);
        loadAllStudents();
    });

    // Add everything to the panel
    searchPanel.add(searchLabel);
    searchPanel.add(new JLabel("ID"));
    searchPanel.add(searchIdField);
    searchPanel.add(new JLabel("Semester"));
    searchPanel.add(searchSemesterField);
    searchPanel.add(new JLabel("Year"));
    searchPanel.add(searchYearField);
    searchPanel.add(searchBtn);
    searchPanel.add(refreshBtn);

        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Table (CENTER)
        String[] columns = {
            "ID","Student Name","Year Level","Semester",
            "Course 1","Grade 1","Passed 1",
            "Course 2","Grade 2","Passed 2",
            "Course 3","Grade 3","Passed 3",
            "Course 4","Grade 4","Passed 4",
            "Course 5","Grade 5","Passed 5",
            "GPA"
        };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setBackground(new Color(146, 190, 219));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 2));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel (Save/Update/Print/Clear/Logout)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(0, 30, 58));
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        String[] buttons = {"Save", "Update", "Print", "Clear", "Logout"};
        for (String text : buttons) {
            JButton btn = makeButton(text);
            if (text.equals("Save")) {
                btn.addActionListener(e -> saveStudentData());
            }
            bottomPanel.add(btn);
        }

        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Save student data to marksheet.txt in CSV format
    private void saveStudentData() {
        try (FileWriter writer = new FileWriter(
            "C:/Users/Katri/OneDrive/Networking/System---ERS/ERS-group/marksheet.txt", true)) {

            StringBuilder sb = new StringBuilder();
            sb.append(studentIdField.getText()).append(",");
            sb.append(studentNameField.getText()).append(",");
            sb.append(schoolYearField.getText()).append(",");
            sb.append(semesterField.getText()).append(",");

            double total = 0;
            for (int i = 0; i < 5; i++) {
                String course = (String) courseDropdowns.get(i).getSelectedItem();
                double grade = Double.parseDouble(courseScores.get(i).getText());
                boolean passed = grade <= 3.0;
                total += grade;

                sb.append(course).append(",");
                sb.append(grade).append(",");
                sb.append(passed).append(",");
            }

            double gpa = total / 5.0;
            sb.append(gpa);

            writer.write(sb.toString() + "\n");
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    // Search for a specific student record
    private void searchStudentData(String searchId, String searchSemester, String searchYear) {
        try (BufferedReader reader = new BufferedReader(
            new FileReader("C:/Users/Katri/OneDrive/Networking/System---ERS/ERS-group/marksheet.txt"))) {

            String line;
            model.setRowCount(0);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 20) {
                    String studentId = parts[0].trim();
                    String yearLevel = parts[2].trim();
                    String semester = parts[3].trim();

                    if (studentId.equals(searchId.trim()) &&
                        semester.equals(searchSemester.trim()) &&
                        yearLevel.equals(searchYear.trim())) {
                        model.addRow(parts);
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(this,
                "No record found for Student ID " + searchId +
                " Semester " + searchSemester +
                " Year " + searchYear);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching data: " + e.getMessage());
        }
    }

    // Load all students into the table
    private void loadAllStudents() {
        try (BufferedReader reader = new BufferedReader(
            new FileReader("C:/Users/Katri/OneDrive/Networking/System---ERS/ERS-group/marksheet.txt"))) {

            String line;
            model.setRowCount(0); // clear table before loading

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 20) {
                    model.addRow(parts);
                } else {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }


        // Helper methods
    private JPanel makeRow(String labelText, Font font, Color labelColor, Color fieldBg) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(0, 30, 58));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(labelColor);
        label.setPreferredSize(new Dimension(120, 32));

        JTextField field = new JTextField("", 12);
        field.setBackground(fieldBg);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 32));
        field.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 2, true));

        row.add(label);
        row.add(field);

        return row;
    }

    private JTextField makeField(String defaultText, Color bg) {
        JTextField field = new JTextField(defaultText, 12);
        field.setBackground(bg);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(200, 32));
        field.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 2, true));
        return field;
    }

    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(146, 190, 219));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(100, 32));
        return btn;
    }

    // For standalone testing
    public static void main(String[] args) {
        JFrame frame = new JFrame("STUDENT MANAGEMENT SYSTEM - SCORE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new ScoreTab());
        frame.setVisible(true);
    }
}
