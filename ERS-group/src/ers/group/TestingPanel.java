package ers.group;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Admin panel for managing academic progression (2-year program)
 */
public class TestingPanel extends JPanel {
    
    private JPanel semesterManagementPanel;
    private JPanel infoPanel;
    private StudentFileLoader studentFileLoader;
    private StudentFileSaver studentFileSaver;
    
    public TestingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 245));
        
        // Initialize file loaders/savers
        studentFileLoader = new StudentFileLoader();
        studentFileSaver = new StudentFileSaver();
        
        // Title
        JLabel titleLabel = new JLabel("Academic Administration Panel (2-Year Program)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(25, 118, 210));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content area with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Add panels
        tabbedPane.addTab("Semester Management", createSemesterPanel());
        tabbedPane.addTab("Year Promotion", createPromotionPanel());
        tabbedPane.addTab("Graduation", createGraduationPanel());
        tabbedPane.addTab("Reports", createReportsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createSemesterPanel() {
        semesterManagementPanel = new JPanel(new GridBagLayout());
        semesterManagementPanel.setBackground(Color.WHITE);
        semesterManagementPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Current academic info display
        infoPanel = createInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        semesterManagementPanel.add(infoPanel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        // Semester control buttons
        JButton btnCompleteSemester = createStyledButton("Complete Current Semester", new Color(76, 175, 80));
        btnCompleteSemester.addActionListener(e -> completeSemester());
        gbc.gridx = 0;
        semesterManagementPanel.add(btnCompleteSemester, gbc);
        
        JButton btnAdvanceSemester = createStyledButton("Advance to Next Semester", new Color(33, 150, 243));
        btnAdvanceSemester.addActionListener(e -> advanceSemester());
        gbc.gridx = 1;
        semesterManagementPanel.add(btnAdvanceSemester, gbc);
        
        gbc.gridy++;
        JButton btnResetYear = createStyledButton("Reset Academic Year", new Color(255, 87, 34));
        btnResetYear.addActionListener(e -> resetAcademicYear());
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        semesterManagementPanel.add(btnResetYear, gbc);
        
        return semesterManagementPanel;
    }
    
    /**
     * Refresh the info panel with current academic data
     */
    private void refreshInfoPanel() {
        if (semesterManagementPanel != null && infoPanel != null) {
            semesterManagementPanel.remove(infoPanel);
            infoPanel = createInfoPanel();
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.weightx = 1.0;
            
            semesterManagementPanel.add(infoPanel, gbc, 0); // Add at position 0 (top)
            semesterManagementPanel.revalidate();
            semesterManagementPanel.repaint();
        }
    }
    
    private JPanel createPromotionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Year Level Promotion (2-Year Program)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        
        // Promotion button for 1st Year → 2nd Year ONLY
        JButton btnPromote1stYear = createStyledButton("Promote 1st Year → 2nd Year", new Color(156, 39, 176));
        btnPromote1stYear.addActionListener(e -> promoteYearLevel("1st Year"));
        gbc.gridx = 0;
        panel.add(btnPromote1stYear, gbc);
        
        // View promotion candidates
        JButton btnViewCandidates = createStyledButton("View Promotion Candidates", new Color(63, 81, 181));
        btnViewCandidates.addActionListener(e -> viewPromotionCandidates());
        gbc.gridx = 1;
        panel.add(btnViewCandidates, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        // Info label
        JLabel infoLabel = new JLabel("<html><i>Note: Only 1st Year students who completed both semesters will be promoted to 2nd Year.<br>" +
                                       "2nd Year students are candidates for graduation.</i></html>");
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, gbc);
        
        return panel;
    }
    
    private JPanel createGraduationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("Graduation Processing (2nd Year Students)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        
        // Graduation buttons
        JButton btnViewCandidates = createStyledButton("View Graduation Candidates", new Color(0, 150, 136));
        btnViewCandidates.addActionListener(e -> viewGraduationCandidates());
        gbc.gridx = 0;
        panel.add(btnViewCandidates, gbc);
        
        JButton btnProcessGraduation = createStyledButton("Process Graduation", new Color(255, 152, 0));
        btnProcessGraduation.addActionListener(e -> processGraduation());
        gbc.gridx = 1;
        panel.add(btnProcessGraduation, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        // Requirements label
        JLabel reqLabel = new JLabel("<html><b>Graduation Requirements (2-Year Program):</b><br>" +
                                      "• Must be 2nd Year student<br>" +
                                      "• All courses passed (no FAILED/INC/DROPPED)<br>" +
                                      "• Minimum 60 units earned<br>" +
                                      "• GWA ≤ 3.0</html>");
        reqLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 136), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(reqLabel, gbc);
        
        return panel;
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Year level selector
        JLabel yearLabel = new JLabel("Select Year Level:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(yearLabel, gbc);
        
        String[] yearLevels = {"1st Year", "2nd Year"};
        JComboBox<String> yearCombo = new JComboBox<>(yearLevels);
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panel.add(yearCombo, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        // Report buttons
        JButton btnPromotionReport = createStyledButton("Generate Promotion Report", new Color(103, 58, 183));
        btnPromotionReport.addActionListener(e -> {
            String yearLevel = (String) yearCombo.getSelectedItem();
            showPromotionReport(yearLevel);
        });
        panel.add(btnPromotionReport, gbc);
        
        gbc.gridy++;
        JButton btnGraduationReport = createStyledButton("Generate Graduation Report", new Color(233, 30, 99));
        btnGraduationReport.addActionListener(e -> showGraduationReport());
        panel.add(btnGraduationReport, gbc);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(new Color(232, 245, 233));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String currentSemester = AcademicUtilities.getCurrentSemester();
        String academicYear = AcademicUtilities.getAcademicYear();
        
        addInfoRow(panel, "Current Semester:", currentSemester);
        addInfoRow(panel, "Academic Year:", academicYear);
        addInfoRow(panel, "Program Type:", "2-Year Program");
        
        return panel;
    }
    
    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblKey);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblValue);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    // ==================== ACTION HANDLERS ====================
    
    private void completeSemester() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Complete current semester and process all grades?\n" +
            "This will update ENROLLED courses to PASSED/FAILED based on grades.\n" +
            "All schedules will be cleared.",
            "Complete Semester",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String semester = AcademicUtilities.getCurrentSemester();
            String academicYear = AcademicUtilities.getAcademicYear();
            
            // Process grades
            AcademicUtilities.processEndOfSemester(semester, academicYear);
            
            // Clear all schedules for new semester
            Schedule.clearAllSchedules();
            
            JOptionPane.showMessageDialog(this,
                "Semester completed successfully!\nAll grades have been processed.\nSchedules have been cleared.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the info panel
            refreshInfoPanel();
        }
    }
    
    private void advanceSemester() {
        String current = AcademicUtilities.getCurrentSemester();
        String currentYear = AcademicUtilities.getAcademicYear();
        String next;
        String nextAcademicYear = currentYear;
        
        switch (current) {
            case "1st Semester":
                next = "2nd Semester";
                break;
            case "2nd Semester":
                next = "Summer";
                break;
            case "Summer":
                next = "1st Semester";
                // Increment academic year (e.g., 2024-2025 -> 2025-2026)
                String[] years = currentYear.split("-");
                if (years.length == 2) {
                    int year1 = Integer.parseInt(years[0]) + 1;
                    int year2 = Integer.parseInt(years[1]) + 1;
                    nextAcademicYear = year1 + "-" + year2;
                }
                break;
            default:
                next = "1st Semester";
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Advance from " + current + " to " + next + "?\n" +
            "Academic Year: " + currentYear + " → " + nextAcademicYear + "\n\n" +
            "(Auto-generated dates for testing purposes)",
            "Advance Semester",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Auto-generate reasonable date ranges for testing
            LocalDate startDate = LocalDate.now();
            LocalDate endDate;
            
            // Set semester duration
            if ("Summer".equals(next)) {
                endDate = startDate.plusMonths(2); // Summer: 2 months
            } else {
                endDate = startDate.plusMonths(5); // Regular semester: 5 months
            }
            
            AcademicUtilities.setActiveSemester(nextAcademicYear, next, startDate, endDate);
            
            // Clear all schedules for new semester
            Schedule.clearAllSchedules();
            
            // Update all students' currentSemester to the new semester
            int studentsUpdated = updateAllStudentsSemester(next);
            
            JOptionPane.showMessageDialog(this,
                "Semester advanced successfully!\n\n" +
                "New Semester: " + next + "\n" +
                "Academic Year: " + nextAcademicYear + "\n" +
                "Duration: " + startDate + " to " + endDate + "\n\n" +
                "Students synced: " + studentsUpdated + "\n" +
                "All schedules have been cleared.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the info panel to show new semester
            refreshInfoPanel();
        }
    }
    
    /**
     * Update all students' currentSemester field to match the new active semester
     * @param newSemester The semester to set for all students
     * @return Number of students updated
     */
    private int updateAllStudentsSemester(String newSemester) {
        try {
            // Load students
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/student.txt",
                "src/ers/group/master files/student.txt",
                "master files/student.txt",
                "student.txt"
            };
            
            String studentFilePath = null;
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    studentFilePath = path;
                    break;
                }
            }
            
            if (studentFilePath == null) {
                System.err.println("Student file not found");
                return 0;
            }
            
            studentFileLoader.load(studentFilePath);
            ArrayList<Student> students = new ArrayList<>(studentFileLoader.getAllStudents());
            
            if (students == null || students.isEmpty()) {
                return 0;
            }
            
            // Update all students' semester
            int count = 0;
            for (Student student : students) {
                student.setCurrentSemester(newSemester);
                count++;
            }
            
            // Save back to file
            studentFileSaver.save(studentFilePath, students);
            
            System.out.println("Updated " + count + " students to semester: " + newSemester);
            return count;
            
        } catch (Exception e) {
            System.err.println("Error updating student semesters: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    private void resetAcademicYear() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reset to 1st Semester of a new academic year?\n" +
            "This will set the active semester back to start.\n\n" +
            "(Auto-generated dates for testing purposes)",
            "Reset Academic Year",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Get current academic year and increment it
            String currentYear = AcademicUtilities.getAcademicYear();
            String newAcademicYear = currentYear;
            String[] years = currentYear.split("-");
            if (years.length == 2) {
                int year1 = Integer.parseInt(years[0]) + 1;
                int year2 = Integer.parseInt(years[1]) + 1;
                newAcademicYear = year1 + "-" + year2;
            }
            
            // Auto-generate dates for 1st Semester
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(5);
            
            AcademicUtilities.setActiveSemester(newAcademicYear, "1st Semester", startDate, endDate);
            
            JOptionPane.showMessageDialog(this,
                "Academic year reset to 1st Semester.\n\n" +
                "Academic Year: " + newAcademicYear + "\n" +
                "Duration: " + startDate + " to " + endDate,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the info panel to show reset
            refreshInfoPanel();
        }
    }
    
    private void promoteYearLevel(String yearLevel) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Promote all eligible " + yearLevel + " students to next year level?\n" +
            "Only students who completed both semesters will be promoted.",
            "Promote Year Level",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            List<String> promoted = AcademicUtilities.bulkPromoteYearLevel(yearLevel);
            
            JOptionPane.showMessageDialog(this,
                "Promotion completed!\n" +
                "Total students promoted: " + promoted.size(),
                "Promotion Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void viewPromotionCandidates() {
        String[] yearLevels = {"1st Year", "2nd Year"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select year level to view promotion candidates:",
            "View Candidates",
            JOptionPane.QUESTION_MESSAGE,
            null,
            yearLevels,
            yearLevels[0]);
        
        if (selected != null) {
            String report = AcademicUtilities.generatePromotionReport(selected);
            showReportDialog("Promotion Candidates - " + selected, report);
        }
    }
    
    private void viewGraduationCandidates() {
        String report = AcademicUtilities.generateGraduationReport();
        showReportDialog("Graduation Candidates (2nd Year)", report);
    }
    
    private void processGraduation() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Process graduation for all eligible 2nd Year students?\n" +
            "This will move student records to graduates.txt.",
            "Process Graduation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            List<String> graduates = AcademicUtilities.bulkProcessGraduation();
            
            StringBuilder message = new StringBuilder();
            message.append("Graduation processing completed!\n\n");
            message.append("Total graduates: ").append(graduates.size()).append("\n\n");
            
            if (!graduates.isEmpty()) {
                message.append("Graduates:\n");
                for (String grad : graduates) {
                    message.append("• ").append(grad).append("\n");
                }
            }
            
            JOptionPane.showMessageDialog(this, message.toString(),
                "Graduation Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showPromotionReport(String yearLevel) {
        String report = AcademicUtilities.generatePromotionReport(yearLevel);
        showReportDialog("Promotion Report - " + yearLevel, report);
    }
    
    private void showGraduationReport() {
        String report = AcademicUtilities.generateGraduationReport();
        showReportDialog("Graduation Report", report);
    }
    
    private void showReportDialog(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
