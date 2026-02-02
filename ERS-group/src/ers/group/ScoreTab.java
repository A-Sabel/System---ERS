package ers.group;

import javax.swing.*;
import java.awt.*;

public class ScoreTab extends JPanel {

    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;

    public ScoreTab() {
        this.setBackground(Color.BLUE);
        setLayout(new BorderLayout(10, 10));

        initLeftPanel();
        initRightPanel();
        initBottomPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void initLeftPanel() {
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Mark Sheet"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Student ID
        gbc.gridx = 0; gbc.gridy = y;
        leftPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(new JTextField(12), gbc);

        // Semester
        gbc.gridx = 0; gbc.gridy = ++y;
        leftPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(new JTextField(12), gbc);

        // Divider
        gbc.gridx = 0; gbc.gridy = ++y;
        gbc.gridwidth = 2;
        leftPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Course 1
        addCourseRow("Course 1:", ++y, gbc);
        addScoreRow("Score 1:", y, gbc);

        addCourseRow("Course 2:", ++y, gbc);
        addScoreRow("Score 2:", y, gbc);

        addCourseRow("Course 3:", ++y, gbc);
        addScoreRow("Score 3:", y, gbc);

        addCourseRow("Course 4:", ++y, gbc);
        addScoreRow("Score 4:", y, gbc);

        addCourseRow("Course 5:", ++y, gbc);
        addScoreRow("Score 5:", y, gbc);
    }


    private void addCourseRow(String label, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        leftPanel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        leftPanel.add(new JTextField(12), gbc);
    }

    private void addScoreRow(String label, int y, GridBagConstraints gbc) {
        gbc.gridx = 2;
        leftPanel.add(new JTextField(5), gbc);
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Search Student"));

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JTextField(), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Search"));
        buttons.add(new JButton("Refresh"));

        searchPanel.add(buttons, BorderLayout.EAST);

        // Table
        String[] columns = {
            "ID", "Student ID", "Semester",
            "Course 1", "Score 1",
            "Course 2", "Score 2",
            "Course 3", "Score 3",
            "Course 4", "Score 4",
            "Course 5", "Score 5"
        };

        JTable table = new JTable(new Object[0][columns.length], columns);
        JScrollPane scrollPane = new JScrollPane(table);

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
    }

        private void initBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());

        bottomPanel.add(new JButton("Save"));
        bottomPanel.add(new JButton("Update"));
        bottomPanel.add(new JButton("Print"));
        bottomPanel.add(new JButton("Clear"));
        bottomPanel.add(new JButton("Logout"));
    }
}




