package ers.group;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


/**
 *
 * @author fedoc
 */
public class Marksheettab extends javax.swing.JPanel {
        // Load student data from file and populate studentMap
        private void loadStudentData() {
            try {
                String[] possiblePaths = {
                    "ERS-group/src/ers/group/master files/student.txt",
                    "src/ers/group/master files/student.txt",
                    "master files/student.txt",
                    "student.txt"
                };
                for (String path : possiblePaths) {
                    File f = new File(path);
                    if (f.exists()) {
                        studentFileLoader.load(path);
                        studentMap = studentFileLoader.getStudentMap();
                        logger.info("Loaded " + studentMap.size() + " students");
                        return;
                    }
                }
                logger.warning("Student file not found in any expected location");
            } catch (Exception e) {
                logger.severe("Error loading student data: " + e.getMessage());
            }
        }
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Marksheettab.class.getName());
    private List<Marksheet> marksheets;
    private MarksheetFileLoader marksheetFileLoader;
    private MarksheetFileSaver marksheetFileSaver;
    private String marksheetFilePath;
    private CourseSubjectFileLoader courseFileLoader;
    private java.util.Map<String, String> courseNameMap;
    private StudentFileLoader studentFileLoader;
    private java.util.Map<String, Student> studentMap;


    /**
     * Creates new form Marksheettab
     */
    public Marksheettab() {
        initComponents();
        marksheets = new ArrayList<>();
        marksheetFileLoader = new MarksheetFileLoader();
        marksheetFileSaver = new MarksheetFileSaver();
        courseFileLoader = new CourseSubjectFileLoader();
        courseNameMap = new java.util.HashMap<>();
        studentFileLoader = new StudentFileLoader();
        studentMap = new java.util.HashMap<>();
        loadStudentData();
        loadCourseData();
        loadMarksheetData();
    }

    private void loadCourseData() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/coursesubject.txt",
                "src/ers/group/master files/coursesubject.txt",
                "master files/coursesubject.txt",
                "coursesubject.txt"
            };

            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    courseFileLoader.load(path);
                    java.util.Collection<CourseSubject> courses = courseFileLoader.getAllSubjects();
                    for (CourseSubject course : courses) {
                        courseNameMap.put(course.getCourseSubjectID(), course.getCourseSubjectName());
                    }
                    logger.info("Loaded " + courses.size() + " courses");
                    return;
                }
            }
            logger.warning("Course file not found in any expected location");
        } catch (Exception e) {
            logger.severe("Error loading course data: " + e.getMessage());
        }
    }

    private String getCourseName(String courseID) {
        if (courseID == null || courseID.isEmpty()) {
            return "";
        }
        return courseNameMap.getOrDefault(courseID, courseID);
    }
    
    /**
     * Gets student name with ID in format "Name (ID)" or just "ID" if student not found.
     * @param studentID The student ID to look up
     * @return Formatted string with student name and ID
     */
    private String getStudentNameWithID(String studentID) {
        if (studentID == null || studentID.isEmpty()) {
            return "";
        }
        Student student = studentMap.get(studentID);
        if (student != null) {
            return student.getStudentName() + " (" + studentID + ")";
        }
        return studentID; // Return just ID if student not found
    }

    private void loadMarksheetData() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/marksheet.txt",
                "src/ers/group/master files/marksheet.txt",
                "master files/marksheet.txt",
                "marksheet.txt"
            };
            for (String path : possiblePaths) {
                File f = new File(path);
                if (f.exists()) {
                    marksheetFilePath = path;
                    logger.info("Found marksheet data at: " + f.getAbsolutePath());
                    marksheetFileLoader.load(path);
                    marksheets = new ArrayList<>(marksheetFileLoader.getAllMarksheets());
                    logger.info("Loaded " + marksheets.size() + " marksheets from file");
                    loadMarksheetTableData();
                    return;
                }
            }
            logger.warning("Marksheet file not found in any expected location");
        } catch (Exception e) {
            logger.severe("Error loading marksheet data: " + e.getMessage());
        }
    }

    /**
     * Load all marksheet data from file into the table
     * Made public to allow refreshing from tab change events
     */
    public void loadMarksheetTableData() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        if (marksheetFilePath == null) return;

        try (BufferedReader br = new BufferedReader(new FileReader(marksheetFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 15) continue;

                Object[] row = new Object[15];
                row[0] = d[0]; // MRK ID
                row[1] = getStudentNameWithID(d[1]); // Student Name (ID)
                row[2] = expandSemester(d[2]); // Semester - expand compressed format
                row[3] = d[3]; // Year Level (Index 3)

                // Courses & Scores (Indices 4-13)
                for (int i = 0; i < 5; i++) {
                    row[4 + (i * 2)] = getCourseName(d[4 + (i * 2)]); // Course Name
                    row[5 + (i * 2)] = d[5 + (i * 2)]; // Score
                }
                row[14] = d[14]; // GPA (Index 14)
                model.addRow(row);
            }
        } catch (Exception e) {
            logger.severe("Error loading marksheet table data: " + e.getMessage());
        }
    }
    
    /**
     * Expand compressed semester format for display
     * 1 -> 1st Semester, 2 -> 2nd Semester, 3 -> Summer
     */
    private String expandSemester(String compressed) {
        if (compressed == null) return "1st Semester";
        switch (compressed.trim()) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return compressed; // Already expanded or unknown
        }
    }

    private void SearchbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        String searchText = SearchbarID.getText().trim();
        if (searchText.isEmpty()) {
            loadMarksheetTableData();
            GWA.setText("GWA: --");
            return;
        }
        
        if (marksheetFilePath == null) return;

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();
        model.setRowCount(0);
        boolean found = false;
        
        // 1. Try Match by Student ID
        try (BufferedReader br = new BufferedReader(new FileReader(marksheetFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 15) continue;

                if (d[1].equalsIgnoreCase(searchText)) {
                    addFileRowToModel(model, d);
                    GWA.setText("GWA: " + d[14]);
                    found = true;
                    break; // Stop after finding the student ID match
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (found) return;

        // If not found by ID, try by year level (case-insensitive, partial match allowed)
        int yearLevelCount = 0;
        double totalGwa = 0.0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(marksheetFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 15) continue;

                // Index 3 is Year Level
                if (d[3].toLowerCase().contains(searchText.toLowerCase())) {
                    addFileRowToModel(model, d);
                    try {
                        totalGwa += Double.parseDouble(d[14]);
                        yearLevelCount++;
                    } catch (NumberFormatException ignored) {}
                    found = true;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (found && yearLevelCount > 0) {
            GWA.setText("GWA: " + String.format("%.2f", totalGwa / yearLevelCount));
            return;
        }

    // If not found by ID or year level, try by semester (case-insensitive, partial match allowed)
        int semesterCount = 0;
        totalGwa = 0.0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(marksheetFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 15) continue;

                // Index 2 is Semester
                if (d[2].toLowerCase().contains(searchText.toLowerCase())) {
                    addFileRowToModel(model, d);
                    try {
                        totalGwa += Double.parseDouble(d[14]);
                        semesterCount++;
                    } catch (NumberFormatException ignored) {}
                    found = true;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (found && semesterCount > 0) {
            GWA.setText("GWA: " + String.format("%.2f", totalGwa / semesterCount));
        } else if (!found) {
            JOptionPane.showMessageDialog(this, "Student, Year Level, or Semester not found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            GWA.setText("GWA: --");
        }
    }
    
    // Helper to add a row from file data array
    private void addFileRowToModel(javax.swing.table.DefaultTableModel model, String[] d) {
        Object[] row = new Object[16]; // Increased from 15 to 16 for Status column
        row[0] = d[0]; // MRK ID
        row[1] = getStudentNameWithID(d[1]); // Student Name (ID)
        row[2] = d[2]; // Semester
        row[3] = d[3]; // Year Level
        row[4] = getEnrollmentStatus(d[1], d[2], d[3]); // Status (NEW)
        for (int i = 0; i < 5; i++) {
            row[5 + (i * 2)] = getCourseName(d[4 + (i * 2)]); // Shifted by 1
            row[6 + (i * 2)] = d[5 + (i * 2)]; // Shifted by 1
        }
        row[15] = d[14]; // GPA (shifted from index 14 to 15)
        model.addRow(row);
    }

    /* Original code removed for clarity
    if (!found) {
        JOptionPane.showMessageDialog(this, "Student, Year Level, or Semester not found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        GWA.setText("GWA: --");
    }
    */

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Background = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        StudentIDPanel = new javax.swing.JPanel();
        StudentID = new javax.swing.JLabel();
        SearchbarID = new javax.swing.JTextField();
        Searchbutton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        GWA = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scoretable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        printbutton = new javax.swing.JButton();
        clearbutton = new javax.swing.JButton();
        logoutbutton = new javax.swing.JButton();

        Background.setBackground(new java.awt.Color(31, 58, 95));

        jPanel15.setBackground(new java.awt.Color(0, 30, 58));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        StudentIDPanel.setBackground(new java.awt.Color(0, 30, 58));
        StudentIDPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        StudentID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        StudentID.setForeground(new java.awt.Color(255, 255, 255));
        StudentID.setText("Student ID");

        SearchbarID.setBackground(new java.awt.Color(146, 190, 219));

        Searchbutton.setBackground(new java.awt.Color(189, 216, 233));
        Searchbutton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        Searchbutton.setText("Search");
        Searchbutton.setForeground(new java.awt.Color(0, 0, 0));
        Searchbutton.addActionListener(this::SearchbuttonActionPerformed);

        javax.swing.GroupLayout StudentIDPanelLayout = new javax.swing.GroupLayout(StudentIDPanel);
        StudentIDPanel.setLayout(StudentIDPanelLayout);
        StudentIDPanelLayout.setHorizontalGroup(
            StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentIDPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(StudentIDPanelLayout.createSequentialGroup()
                        .addComponent(SearchbarID, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(Searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        StudentIDPanelLayout.setVerticalGroup(
            StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentIDPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(StudentID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchbarID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Searchbutton))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 30, 58));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        GWA.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        GWA.setForeground(new java.awt.Color(255, 255, 255));
        GWA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GWA.setText("GWA: --");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(GWA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(GWA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentIDPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(StudentIDPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(0, 30, 58));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        scoretable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Student ID", "Semester", "Year Level", "Status", "Course 1", "Score 1", "Course 2", "Score 2", "Course 3", "Score 3", "Course 4", "Score 4", "Course 5", "Score 5", "Average"
            }
        ));
        scoretable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        // Apply status color renderer to the Status column (index 4)
        scoretable.getColumnModel().getColumn(4).setCellRenderer(new StatusColorRenderer());
        
        // Add row selection listener to highlight and show details
        scoretable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = scoretable.getSelectedRow();
                if (row >= 0) {
                    highlightSelectedRow(row);
                }
            }
        });
        
        jScrollPane1.setViewportView(scoretable);

        jPanel2.setBackground(new java.awt.Color(0, 30, 58));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        printbutton.setBackground(new java.awt.Color(73, 118, 159));
        printbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        printbutton.setText("Print");
        printbutton.setForeground(new java.awt.Color(0, 0, 0));
        printbutton.addActionListener(this::printbuttonActionPerformed); 

        clearbutton.setBackground(new java.awt.Color(73, 118, 159));
        clearbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        clearbutton.setText("Clear");
        clearbutton.setForeground(new java.awt.Color(0, 0, 0));
        clearbutton.addActionListener(this::clearbuttonActionPerformed);

        logoutbutton.setBackground(new java.awt.Color(73, 118, 159));
        logoutbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        logoutbutton.setText("Logout");
        logoutbutton.setForeground(new java.awt.Color(0, 0, 0));
        logoutbutton.addActionListener(this::logoutbuttonActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clearbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(logoutbutton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );


        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1314, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );


        javax.swing.GroupLayout BackgroundLayout = new javax.swing.GroupLayout(Background);
        Background.setLayout(BackgroundLayout);
        BackgroundLayout.setHorizontalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        BackgroundLayout.setVerticalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    private void printbuttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
    try {
        boolean complete = scoretable.print(
                javax.swing.JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat("Marksheet"),
                new java.text.MessageFormat("Page {0}")
        );

        if (complete) {
            JOptionPane.showMessageDialog(this, "Printing completed!");
        } else {
            JOptionPane.showMessageDialog(this, "Printing cancelled.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Print failed: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
    }


}

    private void clearbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        // Clear search bar
        SearchbarID.setText("");
        // Reset GWA label
        GWA.setText("GWA: --");
        // Reload all data to table
        loadMarksheetTableData();
    }

    private void logoutbuttonActionPerformed(java.awt.event.ActionEvent evt) {
    int choice = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (choice == javax.swing.JOptionPane.YES_OPTION) {
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose(); // close parent window
        }
    }
}

    /**
     * Highlight selected row and populate search field
     */
    private void highlightSelectedRow(int row) {
        try {
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();
            
            // Get student ID from selected row
            Object studentIDObj = model.getValueAt(row, 1);
            if (studentIDObj != null) {
                String studentID = studentIDObj.toString();
                // Extract just the ID if it contains name (format: "Name (ID)")
                if (studentID.contains("(") && studentID.contains(")")) {
                    studentID = studentID.substring(studentID.lastIndexOf("(") + 1, studentID.lastIndexOf(")"));
                }
                SearchbarID.setText(studentID);
                
                // Optionally show a message with row details
                String semester = model.getValueAt(row, 2).toString();
                String yearLevel = model.getValueAt(row, 3).toString();
                String status = model.getValueAt(row, 4).toString();
                System.out.println("Selected: " + studentID + " - " + semester + " " + yearLevel + " (" + status + ")");
            }
        } catch (Exception e) {
            System.err.println("Error highlighting row: " + e.getMessage());
        }
    }
    
    /**
     * Look up the enrollment status for a student in a given semester and year level
     */
    private String getEnrollmentStatus(String studentID, String semester, String yearLevel) {
        try {
            String[] enrollmentPaths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            String enrollmentPath = FilePathResolver.resolveFilePath(enrollmentPaths);
            
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(enrollmentPath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] d = line.split(",");
                    if (d.length < 5) continue;
                    
                    // Format: StudentID, CourseList, YearLevel, Semester, Status, SectionList, AcademicYear
                    if (d[0].equals(studentID) && d[3].equals(semester) && d[2].equals(yearLevel)) {
                        return d[4]; // Return status
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN"; // Default if not found
    }

    /**
     * Custom TableCellRenderer to color-code enrollment status
     */
    static class StatusColorRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                switch (status.toUpperCase()) {
                    case "PASSED":
                        c.setBackground(new java.awt.Color(144, 238, 144)); // Light green
                        c.setForeground(java.awt.Color.BLACK);
                        break;
                    case "FAILED":
                        c.setBackground(new java.awt.Color(255, 99, 71)); // Tomato red
                        c.setForeground(java.awt.Color.WHITE);
                        break;
                    case "INC":
                        c.setBackground(new java.awt.Color(255, 255, 102)); // Light yellow
                        c.setForeground(java.awt.Color.BLACK);
                        break;
                    case "DROPPED":
                        c.setBackground(new java.awt.Color(169, 169, 169)); // Dark gray
                        c.setForeground(java.awt.Color.WHITE);
                        break;
                    case "ENROLLED":
                        c.setBackground(new java.awt.Color(173, 216, 230)); // Light blue
                        c.setForeground(java.awt.Color.BLACK);
                        break;
                    default:
                        c.setBackground(java.awt.Color.WHITE);
                        c.setForeground(java.awt.Color.BLACK);
                }
            } else {
                c.setBackground(java.awt.Color.WHITE);
                c.setForeground(java.awt.Color.BLACK);
            }
            
            return c;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("Marksheet Tab");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new Marksheettab());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Variables declaration - do not modify                    
    private javax.swing.JPanel Background;
    private javax.swing.JLabel GWA;
    private javax.swing.JTextField SearchbarID;
    private javax.swing.JButton Searchbutton;
    private javax.swing.JLabel StudentID;
    private javax.swing.JPanel StudentIDPanel;
    private javax.swing.JButton clearbutton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logoutbutton;
    private javax.swing.JButton printbutton;
    private javax.swing.JTable scoretable;
    // End of variables declaration                  
}
