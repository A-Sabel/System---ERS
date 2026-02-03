package ers.group;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ScoreTab extends JPanel {

    private JPanel leftPanel, rightPanel;

    public ScoreTab() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(31, 58, 95)); // dark background for main tab

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

        // 🔳 Top Section Panel (Search + Semester)
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(new Color(0, 30, 58));
        topSection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 216, 233), 2),
            "Student Search",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            labelFont,
            Color.WHITE
        ));

        // Student's ID row
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchRow.setBackground(new Color(0, 30, 58));
        JLabel searchLabel = new JLabel("Student's ID:");
        searchLabel.setFont(labelFont);
        searchLabel.setForeground(labelColor);
        searchLabel.setPreferredSize(new Dimension(120, 32));

        JTextField searchField = makeField("", fieldBg);
        searchField.setPreferredSize(new Dimension(200, 32));

        JButton searchBtn = makeButton("Search");

        searchRow.add(searchLabel);
        searchRow.add(searchField);
        searchRow.add(searchBtn);
        topSection.add(searchRow);

        // Semester row
        JPanel semesterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        semesterRow.setBackground(new Color(0, 30, 58));
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(labelFont);
        semesterLabel.setForeground(labelColor);
        semesterLabel.setPreferredSize(new Dimension(120, 32));

        JTextField semesterField = makeField("", fieldBg);
        semesterField.setPreferredSize(new Dimension(200, 32));

        semesterRow.add(semesterLabel);
        semesterRow.add(semesterField);
        topSection.add(semesterRow);

        leftPanel.add(topSection);

        // 🧾 ID and Student ID fields
        leftPanel.add(makeRow("ID", labelFont, labelColor, fieldBg));
        leftPanel.add(makeRow("Student ID", labelFont, labelColor, fieldBg));

        // 📘 Course Scores with dropdown + decimal field
        String[] courseOptions = {
            "C++ Programming", "Report Writing", "Advanced Mathematics I",
            "Chinese Traditional Culture", "Cloud Computing"
        };

        for (int i = 1; i <= 5; i++) {
            leftPanel.add(makeCourseRow("Course " + i, labelFont, labelColor, fieldBg, courseOptions));
        }
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(0, 30, 58));
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 4, true));

        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(new Color(0, 30, 58));

        JLabel searchLabel = new JLabel("Search Student");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(Color.WHITE);

        JTextField searchField = makeField("", new Color(146, 190, 219));
        searchField.setPreferredSize(new Dimension(300, 32));

        JButton searchBtn = makeButton("Search");
        JButton refreshBtn = makeButton("Refresh");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);

        // Table
        String[] columns = {
            "ID", "Student ID", "Semester", "Course 1", "Score 1",
            "Course 2", "Score 2", "Course 3", "Score 3", "Course 4", "Score 4", "Course 5", "Score 5"
        };
        JTable table = new JTable(new DefaultTableModel(columns, 0));
        table.setBackground(new Color(146, 190, 219));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 2));

        // Bottom panel INSIDE rightPanel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(0, 30, 58));
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        String[] buttons = {"Save", "Update", "Print", "Clear", "Logout"};
        for (String text : buttons) {
            bottomPanel.add(makeButton(text));
        }

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // 🔧 Helper methods for consistent styling
    private JPanel makeRow(String labelText, Font font, Color labelColor, Color fieldBg) {
        return makeRow(labelText, font, labelColor, fieldBg, "");
    }

    private JPanel makeRow(String labelText, Font font, Color labelColor, Color fieldBg, String defaultText) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(0, 30, 58));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(labelColor);
        label.setPreferredSize(new Dimension(100, 32));

        JTextField field = new JTextField(defaultText, 12);
        field.setBackground(fieldBg);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 32));
        field.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 2, true));

        row.add(label);
        row.add(field);
        return row;
    }

    private JPanel makeCourseRow(String labelText, Font font, Color labelColor, Color fieldBg, String[] courseOptions) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(0, 30, 58));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(labelColor);
        label.setPreferredSize(new Dimension(100, 32));

        JComboBox<String> courseDropdown = new JComboBox<>(courseOptions);
        courseDropdown.setBackground(fieldBg);
        courseDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        courseDropdown.setPreferredSize(new Dimension(180, 32));

        JTextField scoreField = new JTextField("0.0", 5);
        scoreField.setBackground(fieldBg);
        scoreField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        scoreField.setPreferredSize(new Dimension(60, 32));
        scoreField.setBorder(BorderFactory.createLineBorder(new Color(189, 216, 233), 2, true));

        row.add(label);
        row.add(courseDropdown);
        row.add(scoreField);
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
}