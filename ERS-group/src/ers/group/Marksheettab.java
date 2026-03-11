package ers.group;

import ers.group.StudentCourseTab.StyledButton;
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
    private java.util.Map<String, String> sectionMap;
    private String selectedStatusFilter = "All";


    /**
     * Creates new form Marksheettab
     */
    public Marksheettab() {
        marksheets = new ArrayList<>();
        marksheetFileLoader = new MarksheetFileLoader();
        marksheetFileSaver = new MarksheetFileSaver();
        courseFileLoader = new CourseSubjectFileLoader();
        courseNameMap = new java.util.HashMap<>();
        studentFileLoader = new StudentFileLoader();
        studentMap = new java.util.HashMap<>();
        sectionMap = new java.util.HashMap<>();
        initComponents();
        addFocusRing(SearchbarID);
        loadStudentData();
        loadSectionData();
        loadCourseData();
        loadMarksheetData();
    }

    private void loadSectionData() {
        try {
            String[] paths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            for (String path : paths) {
                if (!new java.io.File(path).exists()) continue;
                EnrollmentFileLoader el = new EnrollmentFileLoader();
                el.load(path);
                java.util.Map<String, java.util.Set<String>> suffixMap = new java.util.LinkedHashMap<>();
                for (Enrollment enr : el.getAllEnrollments()) {
                    String secID = enr.getSectionID();
                    if (secID == null || secID.isEmpty()) continue;
                    int sep = secID.lastIndexOf("-SEC");
                    String suffix = sep >= 0 ? secID.substring(sep + 1) : secID;
                    suffixMap.computeIfAbsent(enr.getStudentID(), k -> new java.util.LinkedHashSet<>()).add(suffix);
                }
                for (java.util.Map.Entry<String, java.util.Set<String>> e : suffixMap.entrySet()) {
                    sectionMap.put(e.getKey(), String.join(", ", e.getValue()));
                }
                break;
            }
        } catch (Exception e) {
            logger.warning("Could not load section data: " + e.getMessage());
        }
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

    public void loadMarksheetData() {
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
        for (Marksheet m : marksheets) {
            String[] subjects = m.getSubjects();
            double[] marks = m.getMarks();

            // Compute semester and year/schoolYear
            String semester = m.getSemester();
            String yearLevel = m.getSchoolYear();

            // Try to enrich with Student object if available
            Student student = studentMap.get(m.getStudentID());
            if (student != null) {
                if (student.getYearLevel() != null && !student.getYearLevel().isEmpty()) {
                    yearLevel = student.getYearLevel();
                }
            }

            // Determine status (prefer Student.status when available, fall back to studentType)
            String status = "UNKNOWN";
            if (student != null) {
                if (student.getStatus() != null && !student.getStatus().isEmpty()) {
                    status = student.getStatus();
                } else if (student.getStudentType() != null && !student.getStudentType().isEmpty()) {
                    status = student.getStudentType();
                }
            }
            if (status.equals("UNKNOWN")) {
                status = getEnrollmentStatus(m.getStudentID(), semester, yearLevel);
            }

            // Apply status filter
            if (selectedStatusFilter != null && !"All".equalsIgnoreCase(selectedStatusFilter)) {
                if (status == null || !status.toLowerCase().contains(selectedStatusFilter.toLowerCase())) {
                    continue; // skip this marksheet as it doesn't match filter
                }
            }

            // Calculate average
            double average = 0.0;
            int count = 0;
            for (double mark : marks) {
                if (mark > 0) {
                    average += mark;
                    count++;
                }
            }
            average = count > 0 ? average / count : 0.0;

            Object[] row = new Object[16];
            row[0] = "MRK-" + (marksheets.indexOf(m) + 1); // ID
            row[1] = m.getStudentID(); // keep Student ID in column 1 (used by TOR and other logic)
            row[2] = semester; // Semester
            row[3] = yearLevel; // Year Level / School Year
            row[4] = status; // Status

            // Course and score columns start at index 5
            for (int i = 0; i < 5; i++) {
                String courseID = (subjects != null && subjects.length > i && subjects[i] != null) ? subjects[i] : "";
                int courseIndex = 5 + (i * 2);
                int scoreIndex = 6 + (i * 2);
                row[courseIndex] = getCourseName(courseID);
                row[scoreIndex] = (marks != null && marks.length > i && marks[i] > 0) ? marks[i] : "";
            }

            row[15] = String.format("%.2f", average);
            model.addRow(row);
        }
    }

    private void SearchbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        String searchText = SearchbarID.getText().trim();
        if (searchText.equals("Search by ID or Name...")) searchText = "";
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
                line = Encryption.decrypt(line);
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
                line = Encryption.decrypt(line);
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
                line = Encryption.decrypt(line);
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
        // Expecting file format: [0]=MRK ID, [1]=StudentID, [2]=Semester, [3]=YearLevel, [4]=Course1, [5]=Score1, ... [13]=Score5, [14]=GWA
        Object[] row = new Object[16];
        row[0] = d[0]; // MRK ID
        row[1] = d[1]; // Student ID (keep plain for other logic)
        row[2] = d[2]; // Semester
        row[3] = d[3]; // Year Level

        // Determine status (prefer Student.status, then studentType)
        String status = "UNKNOWN";
        Student st = studentMap.get(d[1]);
        if (st != null) {
            if (st.getStatus() != null && !st.getStatus().isEmpty()) {
                status = st.getStatus();
            } else if (st.getStudentType() != null && !st.getStudentType().isEmpty()) {
                status = st.getStudentType();
            }
        }
        if (status.equals("UNKNOWN")) {
            status = getEnrollmentStatus(d[1], d[2], d[3]);
        }
        row[4] = status;

        for (int i = 0; i < 5; i++) {
            String courseID = (4 + (i * 2) < d.length) ? d[4 + (i * 2)] : "";
            String score = (5 + (i * 2) < d.length) ? d[5 + (i * 2)] : "";
            int courseIndex = 5 + (i * 2);
            int scoreIndex = 6 + (i * 2);
            row[courseIndex] = getCourseName(courseID);
            row[scoreIndex] = (score != null && !score.isEmpty()) ? score : "";
        }
        row[15] = (d.length > 14) ? d[14] : ""; // GWA
        // Apply status filter before adding
        if (selectedStatusFilter != null && !"All".equalsIgnoreCase(selectedStatusFilter)) {
            if (status == null || !status.toLowerCase().contains(selectedStatusFilter.toLowerCase())) {
                return; // skip adding this row
            }
        }
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
        Searchbutton = new StyledButton("Search", new java.awt.Color(189, 216, 233), new java.awt.Color(150, 170, 195));
        jPanel5 = new javax.swing.JPanel();
        GWA = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scoretable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        printbutton = new StyledButton("Print", new java.awt.Color(73, 118, 159), new java.awt.Color(53, 93, 134));
        clearbutton = new StyledButton("Clear", new java.awt.Color(73, 118, 159), new java.awt.Color(53, 93, 134));
        logoutbutton = new StyledButton("Logout", new java.awt.Color(40, 55, 75), new java.awt.Color(25, 38, 55));

        Background.setBackground(new java.awt.Color(31, 58, 95));

        jPanel15.setBackground(new java.awt.Color(0, 30, 58));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 30, 58), 3));

        StudentIDPanel.setBackground(new java.awt.Color(0, 30, 58));
        StudentIDPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        StudentID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        StudentID.setForeground(new java.awt.Color(255, 255, 255));
        StudentID.setText("Student ID");

        SearchbarID.setBackground(new java.awt.Color(146, 190, 219));
        SearchbarID.setToolTipText("Search by Student ID or Name");
        SearchbarID.addActionListener(this::SearchbuttonActionPerformed);
        SearchbarID.setText("Search by ID or Name...");
        SearchbarID.setForeground(java.awt.Color.BLACK);
        SearchbarID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (SearchbarID.getText().equals("Search by ID or Name...")) {
                    SearchbarID.setText(""); SearchbarID.setForeground(java.awt.Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (SearchbarID.getText().isEmpty()) {
                    SearchbarID.setForeground(java.awt.Color.BLACK);
                    SearchbarID.setText("Search by ID or Name...");
                }
            }
        });

        Searchbutton.setFont(new java.awt.Font("Segoe UI", 1, 14));
        Searchbutton.setForeground(new java.awt.Color(255, 255, 255));
        Searchbutton.addActionListener(this::SearchbuttonActionPerformed);

        javax.swing.JLabel statusLabel = new javax.swing.JLabel();
        statusLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
        statusLabel.setForeground(new java.awt.Color(255, 255, 255));
        statusLabel.setText("Status Filter:");

        javax.swing.JComboBox<String> statusFilterCombo = new javax.swing.JComboBox<>(new String[]{"All", "Active", "Graduate"});
        statusFilterCombo.setBackground(new java.awt.Color(146, 190, 219));
        statusFilterCombo.addActionListener(e -> {
            selectedStatusFilter = (String) statusFilterCombo.getSelectedItem();
            loadMarksheetTableData();
        });

        // Semester filter removed per UI update request

        javax.swing.GroupLayout StudentIDPanelLayout = new javax.swing.GroupLayout(StudentIDPanel);
        StudentIDPanel.setLayout(StudentIDPanelLayout);
        StudentIDPanelLayout.setHorizontalGroup(
            StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentIDPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(StudentIDPanelLayout.createSequentialGroup()
                        .addComponent(SearchbarID, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(StudentIDPanelLayout.createSequentialGroup()
                        .addComponent(statusLabel)
                        .addGap(10, 10, 10)
                        .addComponent(statusFilterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                )
                .addContainerGap(20, Short.MAX_VALUE))
        );
        StudentIDPanelLayout.setVerticalGroup(
            StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentIDPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(StudentID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchbarID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StudentIDPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusFilterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                
                .addContainerGap(10, Short.MAX_VALUE))
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
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 30, 58), 3));

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
        scoretable.setRowHeight(24);
        scoretable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        scoretable.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        scoretable.getTableHeader().setBackground(new java.awt.Color(0, 30, 58));
        scoretable.getTableHeader().setForeground(new java.awt.Color(0, 0, 0));
        
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
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 30, 58), 3));

        printbutton.setForeground(new java.awt.Color(255, 255, 255));
        printbutton.addActionListener(this::rprintbuttonActionPerformed);

        clearbutton.setForeground(new java.awt.Color(255, 255, 255));
        clearbutton.addActionListener(this::clearbuttonActionPerformed);

        logoutbutton.setForeground(new java.awt.Color(255, 255, 255));
        logoutbutton.addActionListener(this::logoutbuttonActionPerformed);

        StyledButton torButton = new StyledButton("TOR", new java.awt.Color(73, 118, 159), new java.awt.Color(53, 93, 134));
        torButton.setForeground(new java.awt.Color(255, 255, 255));
        torButton.addActionListener(this::torButtonActionPerformed);

        StyledButton yearSemesterButton = new StyledButton("Year / Semester", new java.awt.Color(73, 118, 159), new java.awt.Color(53, 93, 134));
        yearSemesterButton.setForeground(new java.awt.Color(255, 255, 255));
        yearSemesterButton.addActionListener(this::yearSemesterButtonActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(torButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(yearSemesterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(clearbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(logoutbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(torButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearSemesterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
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

    private static final String SCHOOL_NAME = "Algorithmia University";
    private void rprintbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) scoretable.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No data to print. Please search for a student first.",
                    "Print Rating Slip", JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.util.List<PrintSlipData> slips = buildSlipsFromTable();
        if (slips.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Could not build slip data.",
                    "Print", JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
        job.setJobName("Student Rating Slip");
        java.awt.print.PageFormat pf = job.defaultPage();
        pf.setOrientation(java.awt.print.PageFormat.PORTRAIT);
        job.setPrintable((g, pageFormat, pageIndex) -> {
            if (pageIndex >= slips.size()) return java.awt.print.Printable.NO_SUCH_PAGE;
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            drawRatingSlip(g2, (float) pageFormat.getImageableWidth(), slips.get(pageIndex));
            return java.awt.print.Printable.PAGE_EXISTS;
        }, pf);
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Rating Slip printed successfully!");
            } catch (java.awt.print.PrinterException e) {
                JOptionPane.showMessageDialog(this,
                        "Print failed: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void torButtonActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) scoretable.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No data to print. Please search for a student first.",
                    "Print TOR", JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.util.List<PrintSlipData> allSlips = buildSlipsFromTable();
        if (allSlips.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Could not build TOR data.",
                    "Print TOR", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Group by studentID — one TOR page per student
        java.util.Map<String, java.util.List<PrintSlipData>> torByStudent =
                new java.util.LinkedHashMap<>();
        for (PrintSlipData slip : allSlips) {
            torByStudent.computeIfAbsent(slip.studentID,
                    k -> new java.util.ArrayList<>()).add(slip);
        }
        java.util.List<java.util.List<PrintSlipData>> pages =
                new java.util.ArrayList<>(torByStudent.values());

        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
        job.setJobName("Transcript of Records");
        java.awt.print.PageFormat pf = job.defaultPage();
        pf.setOrientation(java.awt.print.PageFormat.PORTRAIT);
        job.setPrintable((g, pageFormat, pageIndex) -> {
            if (pageIndex >= pages.size()) return java.awt.print.Printable.NO_SUCH_PAGE;
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            drawTOR(g2, (float) pageFormat.getImageableWidth(), pages.get(pageIndex));
            return java.awt.print.Printable.PAGE_EXISTS;
        }, pf);
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "TOR printed successfully!");
            } catch (java.awt.print.PrinterException e) {
                JOptionPane.showMessageDialog(this,
                        "TOR Print failed: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static final class PrintSlipData {
        final String studentID, studentName, yearLevel, section, semester, gwa;
        final java.util.List<String[]> courseRows; // each entry: {courseName, teacher, units, grade}
        PrintSlipData(String id, String name, String yr, String sec, String sem,
                    java.util.List<String[]> courses, String g) {
            studentID  = id;   studentName = name; yearLevel = yr;
            section    = sec;  semester    = sem;  gwa       = g;
            courseRows = courses;
        }
    }


    private java.util.List<PrintSlipData> buildSlipsFromTable() {
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) scoretable.getModel();
        java.util.Map<String, String>        teacherLookup = buildCourseToTeacherMap();
        java.util.Map<String, CourseSubject> subjectMap    = courseFileLoader.getSubjectMap();
        java.util.List<PrintSlipData>        slips         = new java.util.ArrayList<>();
        java.util.Set<String>                seen          = new java.util.LinkedHashSet<>();

        for (int row = 0; row < model.getRowCount(); row++) {
            String studentID = String.valueOf(model.getValueAt(row, 1));
            String semester  = String.valueOf(model.getValueAt(row, 2));
            String key = studentID + "|" + semester;
            if (seen.contains(key)) continue;
            seen.add(key);

            for (Marksheet m : marksheets) {
                if (!m.getStudentID().equals(studentID)) continue;
                if (!m.getSemester().equalsIgnoreCase(semester)) continue;

                Student st   = studentMap.get(studentID);
                String  name = (st != null) ? st.getStudentName() : studentID;
                String  yr   = String.valueOf(model.getValueAt(row, 3));
                String  sec  = sectionMap.getOrDefault(studentID, (st != null && st.getSection() != null) ? st.getSection() : "");

                java.util.List<String[]> courses = new java.util.ArrayList<>();
                String[] subjects = m.getSubjects();
                double[] marks    = m.getMarks();
                for (int i = 0; i < subjects.length; i++) {
                    if (subjects[i] == null || subjects[i].isEmpty()) continue;
                    String cid     = subjects[i];
                    String cname   = courseNameMap.getOrDefault(cid, cid);
                    String teacher = teacherLookup.getOrDefault(cid, "-");
                    int    units   = subjectMap.containsKey(cid) ? subjectMap.get(cid).getUnits() : 0;
                    String grade   = (marks[i] > 0) ? String.format("%.2f", marks[i]) : "PENDING";
                    courses.add(new String[]{cname, teacher, String.valueOf(units), grade});
                }
                String gwa = String.valueOf(model.getValueAt(row, 15));
                slips.add(new PrintSlipData(studentID, name, yr, sec, semester, courses, gwa));
                break;
            }
        }
        return slips;
    }

    private java.util.Map<String, String> buildCourseToTeacherMap() {
        java.util.Map<String, String> map = new java.util.LinkedHashMap<>();

        try {
            String[] teacherPaths = {
                "ERS-group/src/ers/group/master files/Teachers.txt",
                "src/ers/group/master files/Teachers.txt",
                "master files/Teachers.txt",
                "Teachers.txt"
            };
            for (String path : teacherPaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    TeacherFileLoader loader = new TeacherFileLoader();
                    loader.load(path);
                    for (Teachers t : loader.getAllTeachers()) {
                        for (String subjectID : t.getQualifiedSubjectIDs()) {
                            map.putIfAbsent(subjectID, t.getTeacherName());
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            logger.warning("Could not load teachers file: " + e.getMessage());
        }

        // Secondary: Schedule.txt overrides with whoever is actually scheduled for a course.
        try {
            String[] schedulePaths = {
                "ERS-group/src/ers/group/master files/Schedule.txt",
                "src/ers/group/master files/Schedule.txt",
                "master files/Schedule.txt",
                "Schedule.txt"
            };
            for (String path : schedulePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    ScheduleFileLoader loader = new ScheduleFileLoader();
                    loader.load(path);
                    for (Schedule s : loader.getAllSchedules()) {
                        if (s.getTeacherName() != null && !s.getTeacherName().isEmpty()) {
                            map.put(s.getCourseID(), s.getTeacherName());
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            logger.warning("Could not load schedule for teacher lookup: " + e.getMessage());
        }

        return map;
    }

    private int drawLetterhead(java.awt.Graphics2D g2, int x, int w, String docTitle) {
        java.awt.FontMetrics fm;
        int y = 28;

        // School name
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16));
        fm = g2.getFontMetrics();
        g2.drawString(SCHOOL_NAME, x + (w - fm.stringWidth(SCHOOL_NAME)) / 2, y);
        y += 20;

        // Document title
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 13));
        fm = g2.getFontMetrics();
        g2.drawString(docTitle, x + (w - fm.stringWidth(docTitle)) / 2, y);
        y += 8;

        // Heavy rule
        g2.fillRect(x, y, w, 2);
        return y + 16; // return Y position for content below letterhead
    }

    private void drawRatingSlip(java.awt.Graphics2D g2, float pageWidth, PrintSlipData slip) {
        g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int MARGIN = 40;
        final int LH     = 20;
        int w = (int) pageWidth - MARGIN * 2;
        int x = MARGIN;

        int y = drawLetterhead(g2, x, w, "STUDENT'S RATING SLIP");

        // Student info block
        String[] lbls = {"Student Name", "Student ID  ", "Year Level  ", "Section     ", "Semester    "};
        String[] vals = {
            slip.studentName, slip.studentID, slip.yearLevel,
            slip.section.isEmpty() ? "-" : slip.section,
            slip.semester
        };
        for (int i = 0; i < lbls.length; i++) {
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD,  11));
            g2.drawString(lbls[i] + " :", x + 4, y);
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
            g2.drawString(vals[i], x + 132, y);
            y += LH;
        }
        y += 4;
        g2.drawLine(x, y, x + w, y);
        y += LH;

        // Table header  (Subject 42% | Teacher 32% | Units 10% | Grade 16%)
        int[] colW = {(int)(w * 0.42f), (int)(w * 0.32f), (int)(w * 0.10f), (int)(w * 0.16f)};
        String[] hdrs = {"Subject / Course", "Teacher / Instructor", "Units", "Grade"};
        g2.setColor(new java.awt.Color(210, 230, 245));
        g2.fillRect(x, y - LH + 4, w, LH);
        g2.setColor(java.awt.Color.BLACK);
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
        int cx = x + 4;
        for (int i = 0; i < hdrs.length; i++) { g2.drawString(hdrs[i], cx, y); cx += colW[i]; }
        y += 4;
        g2.drawLine(x, y, x + w, y);
        y += LH;

        // Course rows (at least 5 rows shown)
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
        int drawn = 0;
        for (String[] row : slip.courseRows) {
            cx = x + 4;
            g2.drawString(truncate(g2, row[0], colW[0] - 6), cx, y); cx += colW[0];
            g2.drawString(truncate(g2, row[1], colW[1] - 6), cx, y); cx += colW[1];
            g2.drawString(row[2], cx, y);                             cx += colW[2];
            g2.drawString(row[3], cx, y);
            y += LH;
            drawn++;
        }
        while (drawn < 5) { y += LH; drawn++; }

        g2.drawLine(x, y, x + w, y);
        y += LH;

        // GWA (right-aligned)
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        java.awt.FontMetrics fm = g2.getFontMetrics();
        String gwaStr = "General Weighted Average (GWA) :  " + slip.gwa;
        g2.drawString(gwaStr, x + w - fm.stringWidth(gwaStr), y);
        y += LH * 2 + 10;

        // Signature lines
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
        int sigW = (int)(w * 0.35f);
        g2.drawLine(x,            y, x + sigW, y);
        g2.drawLine(x + w - sigW, y, x + w,    y);
        fm = g2.getFontMetrics();
        g2.drawString("Student's Signature", x, y + 14);
        String reg = "Registrar / Authorized Signatory";
        g2.drawString(reg, x + w - fm.stringWidth(reg), y + 14);
        y += 30;
        g2.fillRect(x, y, w, 2);
    }

    private void drawTOR(java.awt.Graphics2D g2, float pageWidth, java.util.List<PrintSlipData> slips) {
        g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int MARGIN = 40;
        final int LH     = 18;
        int w = (int) pageWidth - MARGIN * 2;
        int x = MARGIN;

        int y = drawLetterhead(g2, x, w, "TRANSCRIPT OF RECORDS");

        PrintSlipData first = slips.get(0);

        // Student info block
        String[] lbls = {"Student Name", "Student ID  ", "Year Level  ", "Section     "};
        String[] vals = {
            first.studentName, first.studentID, first.yearLevel,
            first.section.isEmpty() ? "-" : first.section
        };
        for (int i = 0; i < lbls.length; i++) {
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD,  11));
            g2.drawString(lbls[i] + " :", x + 4, y);
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
            g2.drawString(vals[i], x + 132, y);
            y += LH;
        }
        y += 4;
        g2.drawLine(x, y, x + w, y);
        y += LH;

        // Column widths: Subject 38% | Teacher 31% | Units 10% | Grade 21%
        int[] colW = {(int)(w * 0.38f), (int)(w * 0.31f), (int)(w * 0.10f), (int)(w * 0.21f)};
        String[] hdrs = {"Subject / Course", "Teacher / Instructor", "Units", "Grade"};

        double totalGwa = 0;
        int    gwaCount = 0;

        for (PrintSlipData slip : slips) {
            // Semester heading in navy
            y += 2;
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
            g2.setColor(new java.awt.Color(10, 50, 110));
            g2.drawString(slip.semester + "  —  " + slip.yearLevel, x + 4, y);
            g2.setColor(java.awt.Color.BLACK);
            y += 4;
            g2.drawLine(x, y, x + w, y);
            y += LH - 2;

            // Table header with blue-tinted background
            g2.setColor(new java.awt.Color(210, 230, 245));
            g2.fillRect(x, y - LH + 4, w, LH);
            g2.setColor(java.awt.Color.BLACK);
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 10));
            int cx = x + 4;
            for (int i = 0; i < hdrs.length; i++) { g2.drawString(hdrs[i], cx, y); cx += colW[i]; }
            y += 4;
            g2.drawLine(x, y, x + w, y);
            y += LH - 2;

            // Course rows
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
            for (String[] row : slip.courseRows) {
                cx = x + 4;
                g2.drawString(truncate(g2, row[0], colW[0] - 6), cx, y); cx += colW[0];
                g2.drawString(truncate(g2, row[1], colW[1] - 6), cx, y); cx += colW[1];
                g2.drawString(row[2], cx, y);                             cx += colW[2];
                g2.drawString(row[3], cx, y);
                y += LH;
            }

            // Semester GWA (right-aligned)
            g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 10));
            java.awt.FontMetrics fm = g2.getFontMetrics();
            String semGwa = "Semester GWA :  " + slip.gwa;
            g2.drawString(semGwa, x + w - fm.stringWidth(semGwa), y);
            y += LH + 4;
            g2.drawLine(x, y, x + w, y);
            y += LH;

            try { totalGwa += Double.parseDouble(slip.gwa); gwaCount++; }
            catch (NumberFormatException ignored) {}
        }

        // Overall GWA (right-aligned)
        y += 4;
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        java.awt.FontMetrics fm2 = g2.getFontMetrics();
        String ov = gwaCount > 0
                ? String.format("Overall GWA :  %.2f", totalGwa / gwaCount)
                : "Overall GWA :  N/A";
        g2.drawString(ov, x + w - fm2.stringWidth(ov), y);
        y += LH * 2 + 10;

        // Signature lines
        g2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
        int sigW = (int)(w * 0.35f);
        g2.drawLine(x,            y, x + sigW, y);
        g2.drawLine(x + w - sigW, y, x + w,    y);
        fm2 = g2.getFontMetrics();
        g2.drawString("Student's Signature", x, y + 14);
        String reg = "Registrar / Authorized Signatory";
        g2.drawString(reg, x + w - fm2.stringWidth(reg), y + 14);
        y += 30;
        g2.fillRect(x, y, w, 2);
    }

    // Clip text that is too wide for its column; appends "..." if cut
    private String truncate(java.awt.Graphics2D g2, String text, int maxWidth) {
        java.awt.FontMetrics fm = g2.getFontMetrics();
        if (fm.stringWidth(text) <= maxWidth) return text;
        while (text.length() > 1 && fm.stringWidth(text + "...") > maxWidth) {
            text = text.substring(0, text.length() - 1);
        }
        return text + "...";
    }

    private void yearSemesterButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Create report grouped by year level and semester
            java.util.Map<String, java.util.Map<String, java.util.List<Marksheet>>> yearSemesterMap = new java.util.LinkedHashMap<>();

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();

            // Group data by year level and semester
            for (int row = 0; row < model.getRowCount(); row++) {
                String semester = (String) model.getValueAt(row, 2);
                String yearLevel = (String) model.getValueAt(row, 3);

                if (yearLevel == null || yearLevel.isEmpty()) continue;
                if (semester == null || semester.isEmpty()) continue;

                // No semester filter (removed) — include all semesters

                yearSemesterMap.putIfAbsent(yearLevel, new java.util.LinkedHashMap<>());
                java.util.Map<String, java.util.List<Marksheet>> semesterMap = yearSemesterMap.get(yearLevel);
                semesterMap.putIfAbsent(semester, new java.util.ArrayList<>());

                String studentID = (String) model.getValueAt(row, 1);
                for (Marksheet m : marksheets) {
                    if (m.getStudentID().equals(studentID) && m.getSemester().equalsIgnoreCase(semester)) {
                        semesterMap.get(semester).add(m);
                    }
                }
            }

            if (yearSemesterMap.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No data to generate year/semester report!", "Print Year/Semester", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Create report table
            javax.swing.table.DefaultTableModel reportModel = new javax.swing.table.DefaultTableModel(
                new String[]{"Year Level", "Semester", "Student Count", "Average Grade", "Total Courses"}, 0
            );

            for (String year : yearSemesterMap.keySet()) {
                for (String semester : yearSemesterMap.get(year).keySet()) {
                    java.util.List<Marksheet> marksheetList = yearSemesterMap.get(year).get(semester);
                    
                    double totalGrade = 0.0;
                    int courseCount = 0;
                    java.util.Set<String> studentSet = new java.util.HashSet<>();

                    for (Marksheet m : marksheetList) {
                        studentSet.add(m.getStudentID());
                        double[] marks = m.getMarks();
                        for (double mark : marks) {
                            if (mark > 0) {
                                totalGrade += mark;
                                courseCount++;
                            }
                        }
                    }

                    double avgGrade = courseCount > 0 ? totalGrade / courseCount : 0.0;

                    reportModel.addRow(new Object[]{
                        year,
                        semester,
                        studentSet.size(),
                        String.format("%.2f", avgGrade),
                        courseCount
                    });
                }
            }

            // Create temporary table for report
            javax.swing.JTable reportTable = new javax.swing.JTable(reportModel);
            reportTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

            String reportTitle = "Year and Semester Report";

            boolean complete = reportTable.print(
                    javax.swing.JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat(reportTitle),
                    new java.text.MessageFormat("Page {0}")
            );

            if (complete) {
                JOptionPane.showMessageDialog(this, "Year/Semester report printing completed!");
            } else {
                JOptionPane.showMessageDialog(this, "Year/Semester report printing cancelled.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Year/Semester Print failed: " + e.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        // Clear search bar
        SearchbarID.setText("");
        // Reset GWA label
        GWA.setText("GWA: --");
        // Clear table rows (do not reload data)
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();
        model.setRowCount(0);
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
                    line = Encryption.decrypt(line);
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

    private void addFocusRing(javax.swing.JTextField field) {
        javax.swing.border.Border def = field.getBorder();
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(66, 133, 244), 2));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(def);
            }
        });
    }

    // Variables declaration - do not modify                    
    private javax.swing.JPanel Background;
    private javax.swing.JLabel GWA;
    private javax.swing.JTextField SearchbarID;
    private StyledButton Searchbutton;
    private javax.swing.JLabel StudentID;
    private javax.swing.JPanel StudentIDPanel;
    private StyledButton clearbutton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private StyledButton logoutbutton;
    private StyledButton printbutton;
    private javax.swing.JTable scoretable;
    // End of variables declaration                  
}
