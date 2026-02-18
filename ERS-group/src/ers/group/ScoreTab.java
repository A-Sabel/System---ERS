//scoretab

package ers.group;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ScoreTab extends JPanel {

    private JTextField studentIdField;
    private JComboBox<String> semesterField;
    private JComboBox<String> yearLevelField;

    @SuppressWarnings("unchecked")
    private JComboBox<String>[] courseDropdowns = new JComboBox[5];
    @SuppressWarnings("unchecked")
    private JComboBox<String>[] courseScores = new JComboBox[5];

    private JTextField searchIdField;
    private JComboBox<String> searchSemField;
    private JComboBox<String> searchYearField;
    private JTable table;
    private DefaultTableModel model;

    private JButton saveBtn, clearBtn, updateBtn, deleteBtn, autoFillBtn, droppedBtn;
    
    // Enrollment integration
    private Map<String, List<String>> studentEnrollments; // StudentID -> List of CourseIDs
    private Map<String, String> studentStatusMap; // StudentID -> Status
    
    // Course data
    private final Map<String, String> courseMap = new LinkedHashMap<>(); // CourseCode -> CourseName
    private List<String> courseNames = new ArrayList<>(); // List of course names for dropdowns
    
    // Standard Filipino Grade Scale
    private final String[] gradeScale = {
        "1.00", "1.25", "1.50", "1.75", "2.00", 
        "2.25", "2.50", "2.75", "3.00", "5.00"
    };
    
    private final String[] semesters = {"1st Semester", "2nd Semester", "Summer"};
    private final String[] yearLevels = {"1st Year", "2nd Year"};

    private String getMarksheetPath() {
        return FilePathResolver.resolveMarksheetFilePath();
    }

    public ScoreTab() {
        setLayout(new BorderLayout());
        setBackground(new Color(31, 58, 95));

        initCourseMap();
        loadEnrollmentData();
        loadStudentStatusData();

        add(createTopSpacer(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadAllRecordsToTable();
        setCurrentActiveSemester(); // Auto-select current semester from calendar
    }

    /**
     * Load course data from courseSubject.txt file
     */
    private void initCourseMap() {
        try {
            String coursePath = FilePathResolver.resolveCourseSubjectFilePath();
            File courseFile = new File(coursePath);
            
            if (!courseFile.exists()) {
                System.err.println("Course file not found: " + coursePath);
                // Fallback to hardcoded courses if file not found
                loadFallbackCourses();
                return;
            }
            
            CourseSubjectFileLoader courseLoader = new CourseSubjectFileLoader();
            courseLoader.load(coursePath);
            
            Collection<CourseSubject> courses = courseLoader.getAllSubjects();
            courseNames.clear();
            courseMap.clear();
            
            for (CourseSubject course : courses) {
                String courseID = course.getCourseSubjectID();
                String courseName = course.getCourseSubjectName();
                courseMap.put(courseID, courseName);
                courseNames.add(courseName);
            }
            
            System.out.println("Loaded " + courseMap.size() + " courses from file");
        } catch (Exception e) {
            System.err.println("Error loading course data: " + e.getMessage());
            e.printStackTrace();
            // Fallback to hardcoded courses on error
            loadFallbackCourses();
        }
    }
    
    /**
     * Fallback method to load a minimal set of hardcoded courses if file loading fails
     */
    private void loadFallbackCourses() {
        String[] codes = {"CS101", "CS102", "CS103", "CS104", "CS105",
                         "CS201", "CS202", "CS203", "CS204", "CS205"};
        String[] names = {"Programming 1", "Computer Fundamentals", "Discrete Mathematics",
                         "Introduction to IT Systems", "Ethics in Computing",
                         "Programming 2", "Object-Oriented Programming", "Web Technologies",
                         "Linear Algebra for Computing", "Human-Computer Interaction"};
        
        courseMap.clear();
        courseNames.clear();
        for (int i = 0; i < codes.length; i++) {
            courseMap.put(codes[i], names[i]);
            courseNames.add(names[i]);
        }
        System.out.println("Using fallback course list (" + courseMap.size() + " courses)");
    }
    
    /**
     * Load enrollment data from enrollment.txt to enable validation and auto-population
     */
    private void loadEnrollmentData() {
        studentEnrollments = new HashMap<>();
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            File enrollFile = new File(enrollmentPath);
            if (!enrollFile.exists()) {
                System.err.println("Enrollment file not found: " + enrollmentPath);
                return;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(enrollFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String studentID = parts[0];
                        String coursesStr = parts[1];
                        String status = parts[4];
                        
                        // Only include ENROLLED students (status can be ENROLLED, PASSED, FAILED, INC, DROPPED)
                        // For entering grades, we accept ENROLLED status
                        if ("ENROLLED".equals(status)) {
                            String[] courses = coursesStr.split(";");
                            studentEnrollments.put(studentID, Arrays.asList(courses));
                        }
                    }
                }
            }
            System.out.println("Loaded enrollment data for " + studentEnrollments.size() + " students");
        } catch (Exception e) {
            System.err.println("Error loading enrollment data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load student status data (specifically checking for graduates)
     */
    private void loadStudentStatusData() {
        studentStatusMap = new HashMap<>();
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            File studentFile = new File(studentPath);
            System.out.println("Loading student statuses from: " + studentFile.getAbsolutePath()); // Debug print
            
            if (studentFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(studentFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",", -1); // Use -1 to keep trailing empty strings
                        // Format: ID, Name, Age, DOB, YearLevel, Semester, Section, StudentType, Status, ...
                        if (parts.length >= 9) {
                            String studentID = parts[0].trim();
                            String status = parts[8].trim();
                            if (!studentID.isEmpty() && !status.isEmpty()) {
                                studentStatusMap.put(studentID, status);
                            }
                        }
                    }
                }
                System.out.println("Loaded statuses for " + studentStatusMap.size() + " students."); // Debug print
            } else {
                System.err.println("Student file not found at: " + studentPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading student status data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get list of enrolled courses for a student in a specific semester and year level
     * @param studentID The student ID
     * @param semester The semester ("1st Semester", "2nd Semester", or "Summer")
     * @param yearLevel The year level ("1st Year", "2nd Year", etc.)
     * @return List of course IDs the student is enrolled in
     */
    private List<String> getEnrolledCourses(String studentID, String semester, String yearLevel) {
        if (studentID == null || studentID.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Convert semester name to number for file lookup
        String semesterNum = convertSemesterToNumber(semester);
        
        try {
            String enrollmentPath = FilePathResolver.resolveEnrollmentFilePath();
            try (BufferedReader br = new BufferedReader(new FileReader(enrollmentPath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        // Format: StudentID, CourseList, YearLevel, Semester, Status, SectionList, AcademicYear
                        if (parts[0].equals(studentID) && 
                            parts[2].equals(yearLevel) && 
                            parts[3].equals(semesterNum) &&
                            "ENROLLED".equals(parts[4])) {
                            String[] courses = parts[1].split(";");
                            return Arrays.asList(courses);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting enrolled courses: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    private String convertSemesterToNumber(String semester) {
        if (semester == null) return "1";
        switch (semester) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return "1";
        }
    }
    
    /**
     * Check if a student is enrolled in a specific course
     * @param studentID The student ID
     * @param courseCode The course code (e.g., "CS101")
     * @return true if enrolled, false otherwise
     */
    private boolean isStudentEnrolledInCourse(String studentID, String courseCode) {
        List<String> enrolledCourses = studentEnrollments.get(studentID);
        return enrolledCourses != null && enrolledCourses.contains(courseCode);
    }
    
    /**
     * Validate that all entered courses are ones the student is actually enrolled in
     * @param studentID The student ID
     * @return true if all courses are valid enrollments, false otherwise
     */
    private boolean validateCoursesAgainstEnrollment(String studentID) {
        if (studentID == null || studentID.isEmpty()) {
            return false;
        }
        
        // Get enrollment for the specific semester and year level
        String semester = semesterField.getSelectedItem().toString();
        String yearLevel = yearLevelField.getSelectedItem().toString();
        
        // SKIP enrollment validation if marksheet record already exists
        // This allows updating existing records even if enrollment status changed
        if (recordExists(studentID, semester, yearLevel)) {
            System.out.println("Existing marksheet found - skipping enrollment validation");
            return true;
        }
        
        List<String> enrolledCourses = getEnrolledCourses(studentID, semester, yearLevel);
        
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Student " + studentID + " has no enrolled courses for " + semester + ", " + yearLevel + ".\n" +
                "Scores can only be entered for courses the student is enrolled in.",
                "No Enrollment Found",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check each selected course against the semester/year-specific enrollment
        List<String> unenrolledCourses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String selectedCourseName = courseDropdowns[i].getSelectedItem().toString();
            String courseCode = getCourseCode(selectedCourseName);
            
            if (!courseCode.isEmpty() && !enrolledCourses.contains(courseCode)) {
                unenrolledCourses.add(courseCode + " (" + selectedCourseName + ")");
            }
        }
        
        if (!unenrolledCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Cannot enter scores for courses the student is not enrolled in:\n\n" +
                String.join("\n", unenrolledCourses) + "\n\n" +
                "Student " + studentID + " is only enrolled in:\n" +
                String.join(", ", enrolledCourses),
                "Enrollment Validation Failed",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    /**
     * Validate that a student ID exists in the student.txt file
     * @param studentID the student ID to validate
     * @return true if student exists, false otherwise
     */
    private boolean validateStudentExists(String studentID) {
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            File studentFile = new File(studentPath);
            
            if (!studentFile.exists()) {
                System.err.println("Student file not found: " + studentPath);
                return false;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].trim().equals(studentID)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error validating student existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get the student's actual year level from student.txt
     * @param studentID the student ID
     * @return the year level string, or null if not found
     */
    private String getStudentYearLevel(String studentID) {
        try {
            String studentPath = FilePathResolver.resolveStudentFilePath();
            File studentFile = new File(studentPath);
            
            if (!studentFile.exists()) {
                return null;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Student format: ID, Name, Age, DOB, YearLevel, Section, StudentType, SubjectsEnrolled, GWA...
                    // Index:          0    1     2    3      4         5         6            7            8
                    if (parts.length >= 5 && parts[0].trim().equals(studentID)) {
                        // Year level is at index 4
                        return parts[4].trim();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting student year level: " + e.getMessage());
        }
        return null;
    }

    /**
     * Validate year level matches the student's actual year level
     * @param studentID the student ID
     * @param selectedYearLevel the year level selected in the form
     * @return true if year levels match, false otherwise
     */
    private boolean validateYearLevelMatch(String studentID, String selectedYearLevel) {
        String actualYearLevel = getStudentYearLevel(studentID);
        
        if (actualYearLevel == null) {
            JOptionPane.showMessageDialog(this,
                "Unable to verify year level for student " + studentID + ".\n" +
                "Please ensure the student data is properly loaded.",
                "Year Level Verification Failed",
                JOptionPane.WARNING_MESSAGE);
            return true; // Allow to proceed if we can't verify
        }
        
        if (!actualYearLevel.equals(selectedYearLevel)) {
            JOptionPane.showMessageDialog(this,
                "Year level mismatch!\n\n" +
                "Selected Year Level: " + selectedYearLevel + "\n" +
                "Student's Actual Year Level: " + actualYearLevel + "\n\n" +
                "Please select the correct year level.",
                "Year Level Mismatch",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    /**
     * Comprehensive validation of all inputs before saving or updating
     * @param studentID the student ID
     * @return true if all validations pass, false otherwise
     */
    private boolean validateAllInputs(String studentID) {
        // 1. Validate student ID is not empty
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a Student ID.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return false;
        }
        
        // 2. Validate student exists
        if (!validateStudentExists(studentID)) {
            JOptionPane.showMessageDialog(this,
                "Student ID " + studentID + " does not exist in the system.\n" +
                "Please enter a valid Student ID.",
                "Student Not Found",
                JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return false;
        }
        
        // 3. Validate year level matches student's actual year level
        String selectedYearLevel = yearLevelField.getSelectedItem().toString();
        if (!validateYearLevelMatch(studentID, selectedYearLevel)) {
            yearLevelField.requestFocus();
            return false;
        }
        
        // 4. Validate enrolled courses
        if (!validateCoursesAgainstEnrollment(studentID)) {
            return false;
        }
        
        // 5. Validate all grades
        if (!validateAllGrades()) {
            return false;
        }
        
        // 6. Validate all courses are selected (no empty/default selections)
        for (int i = 0; i < 5; i++) {
            String selectedCourse = courseDropdowns[i].getSelectedItem().toString();
            if (selectedCourse == null || selectedCourse.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please select a course for Course " + (i + 1),
                    "Course Selection Required",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }

    private JPanel createTopSpacer() {
        JPanel top = new JPanel();
        top.setPreferredSize(new Dimension(0, 15));
        top.setBackground(new Color(31, 58, 95));
        return top;
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(31, 58, 95));
        main.add(createLeftPanel(), BorderLayout.WEST);
        main.add(createRightPanel(), BorderLayout.CENTER);
        return main;
    }

    // Custom Button Class
    class StyledButton extends JButton {
        private final Color colorTop = new Color(154, 192, 226);
        private final Color colorBottom = new Color(110, 158, 203);
        private final Color borderColor = new Color(50, 50, 50);

        private boolean isHovered = false;
        private boolean isPressed = false;

        public StyledButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(new Font("Segoe UI", Font.BOLD, 24));
            setForeground(new Color(10, 10, 10));
            setPreferredSize(new Dimension(160, 60));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { isHovered = false; isPressed = false; repaint(); }
                @Override public void mousePressed(MouseEvent e) { isPressed = true; repaint(); }
                @Override public void mouseReleased(MouseEvent e) { isPressed = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color top = isPressed ? colorBottom : (isHovered ? colorTop.brighter() : colorTop);
            Color bottom = isPressed ? colorTop : (isHovered ? colorBottom.brighter() : colorBottom);

            g2.setPaint(new GradientPaint(0, 0, top, 0, h, bottom));
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 12, 12));
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 120), 0, h * 0.6f, new Color(255, 255, 255, 0)));
            g2.fill(new RoundRectangle2D.Double(2, 2, w - 4, h - 4, 10, 10));
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel createLeftPanel() {
        JPanel left = new JPanel();
        left.setBackground(new Color(0, 30, 58));
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        left.setPreferredSize(new Dimension(360, 700));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Color fieldBg = new Color(146, 190, 219);

        studentIdField = addFieldAbove(left, "Student ID", labelFont, fieldBg);
        yearLevelField = addComboAbove(left, "Year Level", yearLevels, labelFont, fieldBg);
        semesterField = addComboAbove(left, "Semester", semesters, labelFont, fieldBg);
        
        // Auto-Fill Button
        JPanel autoFillPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 8));
        autoFillPanel.setBackground(new Color(0, 30, 58));
        autoFillBtn = new JButton("Auto-Fill Subjects");
        autoFillBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        autoFillBtn.setBackground(new Color(100, 180, 100));
        autoFillBtn.setForeground(Color.WHITE);
        autoFillBtn.setPreferredSize(new Dimension(200, 35));
        autoFillBtn.setFocusPainted(false);
        autoFillBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        autoFillBtn.addActionListener(e -> autoFillSubjects());
        autoFillPanel.add(autoFillBtn);
        left.add(autoFillPanel);

        for (int i = 0; i < 5; i++) {
            JPanel row = new JPanel(new GridBagLayout());
            row.setBackground(new Color(0, 30, 58));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 5, 3, 5);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel label = new JLabel("Course " + (i + 1));
            label.setForeground(Color.WHITE);
            label.setFont(labelFont);

            JComboBox<String> box = new JComboBox<>(courseNames.toArray(new String[0]));
            box.setPreferredSize(new Dimension(180, 30));
            box.setBackground(fieldBg);

            JComboBox<String> scoreBox = new JComboBox<>(gradeScale);
            scoreBox.setPreferredSize(new Dimension(80, 30));
            scoreBox.setBackground(fieldBg);
            scoreBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            scoreBox.setSelectedIndex(0); // Default to 1.00

            gbc.gridx = 0; gbc.gridy = 0; row.add(label, gbc);
            gbc.gridx = 0; gbc.gridy = 1; row.add(box, gbc);
            gbc.gridx = 1; gbc.gridy = 1; row.add(scoreBox, gbc);

            courseDropdowns[i] = box;
            courseScores[i] = scoreBox;
            left.add(row);
        }
        return left;
    }

    private JTextField addFieldAbove(JPanel panel, String text, Font font, Color bg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(0, 30, 58));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(font);
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(220, 30));
        tf.setBackground(bg);
        gbc.gridx = 0; gbc.gridy = 0; p.add(l, gbc);
        gbc.gridx = 0; gbc.gridy = 1; p.add(tf, gbc);
        panel.add(p);
        return tf;
    }

    private JComboBox<String> addComboAbove(JPanel panel, String text, String[] options, Font font, Color bg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(0, 30, 58));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(font);
        JComboBox<String> combo = new JComboBox<>(options);
        combo.setPreferredSize(new Dimension(220, 30));
        combo.setBackground(bg);
        gbc.gridx = 0; gbc.gridy = 0; p.add(l, gbc);
        gbc.gridx = 0; gbc.gridy = 1; p.add(combo, gbc);
        panel.add(p);
        return combo;
    }

    private JPanel createRightPanel() {
        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBackground(new Color(31, 58, 95));
        right.add(createSearchPanel(), BorderLayout.NORTH);
        right.add(createTablePanel(), BorderLayout.CENTER);
        right.add(createBottomButtonPanel(), BorderLayout.SOUTH);
        return right;
    }

    private JPanel createSearchPanel() {
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        search.setBackground(new Color(0, 30, 58));
        search.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        searchIdField = makeField(120);
        searchSemField = new JComboBox<>(semesters);
        searchSemField.setPreferredSize(new Dimension(120, 32));
        searchYearField = new JComboBox<>(yearLevels);
        searchYearField.setPreferredSize(new Dimension(100, 32));
        JButton searchBtn = new StyledButton("Search");
        searchBtn.setPreferredSize(new Dimension(110, 38));
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchBtn.addActionListener(e -> searchRecord());
        
        JButton refreshBtn = new StyledButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(110, 38));
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshBtn.addActionListener(e -> loadAllRecordsToTable());
        
        search.add(label("ID:"));
        search.add(searchIdField);
        search.add(label("Sem:"));
        search.add(searchSemField);
        search.add(label("Year:"));
        search.add(searchYearField);
        search.add(searchBtn);
        search.add(refreshBtn);
        return search;
    }

    // ================= TABLE CUSTOMIZATION FOR PHOTO MATCH =================
    private JScrollPane createTablePanel() {
        String[] cols = {
  "ID", "Student ID", "Semester", "Year Level",
  "Course 1", "Score 1", "Course 2", "Score 2",
  "Course 3", "Score 3", "Course 4", "Score 4",
  "Course 5", "Score 5", "GPA"
};

        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // CHANGE 1: Enable Auto-Resize so it fits the frame width
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(224, 227, 232));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false); // Prevents users from dragging columns out of order

        // Custom Renderer for Alternating Row Colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                // Align scores and GPA to center for better fit
                if (column >= 4 && column % 2 == 0 || column == 13) {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        });

        // CHANGE 2: Set "Preferred" vs "Minimum" widths to ensure readability
        // IDs and Scores can be narrow, Courses need to be wider
        int[] colWidths = {
            60,  // ID
            100, // Student ID
            110, // Semester
            90,  // Year Level
            150, 60, // Course 1, Score 1
            150, 60, // Course 2, Score 2
            150, 60, // Course 3, Score 3
            150, 60, // Course 4, Score 4
            150, 60, // Course 5, Score 5
            80       // GPA
        };
        for(int i = 0; i < colWidths.length; i++) {
            if (i < table.getColumnCount()) {
                table.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
            }
        }

        // Add row selection listener to auto-populate form fields
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    populateFieldsFromTableRow(row);
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(new Color(200, 203, 209)); 
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 2));
        
        // CHANGE 3: Ensure the scrollpane doesn't try to be wider than its parent
        scroll.setPreferredSize(new Dimension(800, 400)); 
        
        return scroll;
    }   
    private JPanel createBottomButtonPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 25));
        bottom.setBackground(new Color(0, 30, 58));
        bottom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        saveBtn = new StyledButton("Save");
        clearBtn = new StyledButton("Clear");
        updateBtn = new StyledButton("Update");
        deleteBtn = new StyledButton("Delete");
        droppedBtn = new StyledButton("Mark as Dropped");
        saveBtn.addActionListener(e -> saveRecord());
        clearBtn.addActionListener(e -> clearFields());
        updateBtn.addActionListener(e -> updateRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        droppedBtn.addActionListener(e -> markAsDropped());
        bottom.add(saveBtn);
        bottom.add(clearBtn);
        bottom.add(updateBtn);
        bottom.add(deleteBtn);
        bottom.add(droppedBtn);
        return bottom;
    }

    private JTextField makeField(int width) {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(width, 32));
        t.setBackground(new Color(146, 190, 219));
        return t;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return l;
    }

    private void loadAllRecordsToTable() {
    model.setRowCount(0);
    loadStudentStatusData(); // Refresh status data from student.txt

    String marksheetPath = getMarksheetPath();
    System.out.println("Attempting to load marksheet records from: " + new File(marksheetPath).getAbsolutePath());

    try (BufferedReader br = new BufferedReader(new FileReader(marksheetPath))) {
        String line;

        while ((line = br.readLine()) != null) {
            String[] d = line.split(",");
            if (d.length < 15) continue;

            String studentID = d[1];
            // Skip graduates
            String status = studentStatusMap.get(studentID);
            if ("Graduate".equalsIgnoreCase(status)) {
                System.out.println("Skipping marksheet record for graduate student: " + studentID);
                continue;
            }

            Object[] row = new Object[15];

            row[0] = d[0]; // MRK
            row[1] = d[1]; // Student ID
            row[2] = expandSemester(d[2]); // Semester - expand for display
            row[3] = d[3]; // Year Level

            // Courses & scores start at index 4
            for (int i = 0; i < 5; i++) {
                row[4 + i * 2] = courseMap.getOrDefault(d[4 + i * 2], d[4 + i * 2]);
                row[5 + i * 2] = d[5 + i * 2];
            }

            row[14] = d[14]; // GPA

            model.addRow(row);
        }
    } catch (FileNotFoundException e) {
        System.err.println("ERROR: Marksheet file not found at " + marksheetPath);
        JOptionPane.showMessageDialog(this,
            "Marksheet file not found. Please ensure 'marksheet.txt' exists.",
            "File Not Found",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    // Notify table that data has changed
    model.fireTableDataChanged();
    table.repaint();
}


    private void searchRecord() {
        String id = searchIdField.getText().trim();
        String sem = searchSemField.getSelectedItem().toString();
        String year = searchYearField.getSelectedItem().toString();
        
        // Check if any search criteria is provided
        boolean hasId = !id.isEmpty();
        boolean hasSem = !sem.equals("1st Semester") || searchSemField.getSelectedIndex() != 0;
        boolean hasYear = !year.equals("1st Year") || searchYearField.getSelectedIndex() != 0;
        
        // If no search criteria, show all records
        if (!hasId && !hasSem && !hasYear) {
            loadAllRecordsToTable();
            JOptionPane.showMessageDialog(this,
                "No search criteria entered. Showing all records.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Compress semester for file comparison
        String compressedSem = compressSemester(sem);
        
        model.setRowCount(0);
        loadStudentStatusData(); // Refresh status data

        try(BufferedReader br = new BufferedReader(new FileReader(getMarksheetPath()))) {
            String line;
            int matchCount = 0;
            while((line = br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length < 15) continue;
                
                String studentID = d[1];
                // Skip graduates
                String status = studentStatusMap.get(studentID);
                if ("Graduate".equalsIgnoreCase(status)) {
                    System.out.println("Skipping search result for graduate student: " + studentID);
                    continue;
                }

                // Flexible search: match based on provided criteria only
                boolean matches = true;
                
                // Check ID only if provided
                if (hasId && !d[1].equals(id)) {
                    matches = false;
                }
                
                // Check semester only if selected (not default)
                if (hasSem && !d[2].equals(compressedSem)) {
                    matches = false;
                }
                
                // Check year level only if selected (not default)
                if (hasYear && !d[3].equals(year)) {
                    matches = false;
                }
                
                if(matches){
                    Object[] row = new Object[15];
                    row[0] = d[0]; // MRK ID
                    row[1] = d[1]; // Student ID
                    row[2] = expandSemester(d[2]); // Semester - expand for display
                    row[3] = d[3]; // Year Level
                    
                    for(int i=0;i<5;i++){
                        // d[4+i*2] = Course, d[5+i*2] = Score
                        row[4 + i*2] = courseMap.getOrDefault(d[4 + i*2], d[4 + i*2]);
                        row[5 + i*2] = d[5 + i*2];
                    }
                    row[14] = d[14]; // GPA
                    
                    model.addRow(row);
                    matchCount++;
                    
                    // Auto-populate form fields with the first match
                    if (matchCount == 1) {
                        yearLevelField.setSelectedItem(d[3]);
                        semesterField.setSelectedItem(expandSemester(d[2]));
                        studentIdField.setText(d[1]);
                        
                        for(int i=0;i<5;i++){
                            courseDropdowns[i].setSelectedItem(courseMap.getOrDefault(d[4 + i*2], d[4 + i*2]));
                            courseScores[i].setSelectedItem(d[5 + i*2]);
                        }
                    }
                }
            }
            
            if(matchCount == 0) {
                // Build search criteria message
                StringBuilder searchCriteria = new StringBuilder("No records found for:\n");
                if (hasId) searchCriteria.append("Student ID: ").append(id).append("\n");
                if (hasSem) searchCriteria.append("Semester: ").append(sem).append("\n");
                if (hasYear) searchCriteria.append("Year Level: ").append(year).append("\n");
                
                JOptionPane.showMessageDialog(this,
                    searchCriteria.toString(),
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Found " + matchCount + " matching record(s)");
            }
        } catch(Exception e){ 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error searching records: " + e.getMessage(),
                "Search Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        // Refresh table display
        model.fireTableDataChanged();
        table.repaint();
    }
    
    private void clearFields() {
        studentIdField.setText("");
        semesterField.setSelectedIndex(0);
        yearLevelField.setSelectedIndex(0);
        for (int i = 0; i < 5; i++) {
            courseDropdowns[i].setSelectedIndex(0);
            courseScores[i].setSelectedIndex(0); // Default to 1.00
        }
        searchIdField.setText("");
        searchSemField.setSelectedIndex(0);
        searchYearField.setSelectedIndex(0);
        loadAllRecordsToTable();
    }
    
    /**
     * Populate form fields from a selected table row
     */
    private void populateFieldsFromTableRow(int row) {
        try {
            // Get values from table model
            String studentID = model.getValueAt(row, 1).toString();
            String semester = model.getValueAt(row, 2).toString();
            String yearLevel = model.getValueAt(row, 3).toString();
            
            // Set student ID and combo boxes
            studentIdField.setText(studentID);
            semesterField.setSelectedItem(semester);
            yearLevelField.setSelectedItem(yearLevel);
            
            // Set courses and scores
            for (int i = 0; i < 5; i++) {
                Object courseValue = model.getValueAt(row, 4 + (i * 2));
                Object scoreValue = model.getValueAt(row, 5 + (i * 2));
                
                if (courseValue != null && !courseValue.toString().isEmpty()) {
                    courseDropdowns[i].setSelectedItem(courseValue.toString());
                }
                if (scoreValue != null && !scoreValue.toString().isEmpty()) {
                    courseScores[i].setSelectedItem(scoreValue.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error populating fields from table row: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Auto-fill course dropdowns based on student's enrollment records
     */
    private void autoFillSubjects() {
        String id = studentIdField.getText().trim();
        
        // 1. Validation
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID first.");
            return;
        }
        
        // 2. Fetch enrollment list for specific semester and year level
        String semester = semesterField.getSelectedItem().toString();
        String yearLevel = yearLevelField.getSelectedItem().toString();
        List<String> enrolledCodes = getEnrolledCourses(id, semester, yearLevel);
        
        if (enrolledCodes == null || enrolledCodes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No enrollment records found for " + id + " in " + semester + ", " + yearLevel);
            return;
        }

        // 3. Clear existing selections first
        for (int i = 0; i < 5; i++) {
            courseDropdowns[i].setSelectedIndex(0); 
        }

        // 4. Fill dropdowns with enrolled Course Names
        for (int i = 0; i < enrolledCodes.size() && i < 5; i++) {
            String code = enrolledCodes.get(i);
            String name = courseMap.get(code); // Convert Code (CS101) to Name (Programming 1)
            
            if (name != null) {
                // Try to find the exact match in dropdown
                boolean found = false;
                for (int j = 0; j < courseDropdowns[i].getItemCount(); j++) {
                    String item = courseDropdowns[i].getItemAt(j);
                    if (item != null && item.equals(name)) {
                        courseDropdowns[i].setSelectedIndex(j);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.err.println("Course name not found in dropdown: " + name);
                }
            } else {
                System.err.println("Course code not found in map: " + code);
            }
        }
        
        JOptionPane.showMessageDialog(this, "Loaded " + Math.min(enrolledCodes.size(), 5) + " subjects from enrollment records.");
    }

    private void saveRecord() {
        String id = studentIdField.getText().trim();
        String sem = semesterField.getSelectedItem().toString();
        String year = yearLevelField.getSelectedItem().toString();
        
        // Comprehensive validation of all inputs
        if (!validateAllInputs(id)) {
            return; // Validation failed, error message already shown
        }
        
        // Check for duplicate record
        if(recordExists(id, sem, year)) {
            int choice = JOptionPane.showConfirmDialog(this, 
                "A marksheet record already exists for this student, semester, and year level.\n" +
                "Do you want to update the existing record instead?",
                "Duplicate Record", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if(choice == JOptionPane.YES_OPTION) {
                updateRecord();
                return;
            } else {
                return; // User chose not to proceed
            }
        }

        try(PrintWriter pw = new PrintWriter(new FileWriter(getMarksheetPath(),true))){
            String nextMRK = getNextMRK();
            StringBuilder sb = new StringBuilder();
            String compressedSem = compressSemester(sem);
            sb.append(nextMRK).append(",").append(id).append(",").append(compressedSem).append(",").append(year);

            for(int i=0;i<5;i++){
                sb.append(",").append(getCourseCode(courseDropdowns[i].getSelectedItem().toString()));
                sb.append(",").append(courseScores[i].getSelectedItem().toString());
            }

            double gpa = calculateGPA();
            sb.append(",").append(String.format("%.2f",gpa));
            pw.println(sb.toString());
        } catch(Exception e){ e.printStackTrace(); }
        
        // Reload enrollment data to refresh any changes
        loadEnrollmentData();
        loadAllRecordsToTable();
        
        // Update student's overall GWA in student.txt
        updateStudentGWA(studentIdField.getText().trim());
        
        // Update course statuses based on grades entered
        AcademicUtilities.updateCourseStatuses(id, sem, year);
        
        JOptionPane.showMessageDialog(this,"Record Saved!");
    }

    private void updateRecord() {
        String studentID = studentIdField.getText().trim();
        
        // Comprehensive validation of all inputs
        if (!validateAllInputs(studentID)) {
            return; // Validation failed, error message already shown
        }
        
        try {
            File inputFile = new File(getMarksheetPath());
            File tempFile = new File("temp_marksheet.txt");
            boolean updated = false;

            try(BufferedReader br = new BufferedReader(new FileReader(inputFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

                String line;
                String compressedSem = compressSemester(semesterField.getSelectedItem().toString());
                while((line=br.readLine())!=null){
                    String[] d = line.split(",");
                    if(d.length < 15){
                        pw.println(line);
                        continue;
                    }
                    if(d[1].equals(studentIdField.getText().trim()) &&
                       d[2].equals(compressedSem) &&
                       d[3].equals(yearLevelField.getSelectedItem().toString())) {

                        StringBuilder sb = new StringBuilder();
                        sb.append(d[0]).append(",").append(d[1]).append(",").append(compressedSem);
                        sb.append(",").append(yearLevelField.getSelectedItem().toString());
                        
                        for(int i=0;i<5;i++){
                            sb.append(",").append(getCourseCode(courseDropdowns[i].getSelectedItem().toString()));
                            sb.append(",").append(courseScores[i].getSelectedItem().toString());
                        }
                        double gpa = calculateGPA();
                        sb.append(",").append(String.format("%.2f",gpa));
                        pw.println(sb.toString());
                        updated = true;
                    } else {
                        pw.println(line);
                    }
                }
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
            if(updated) {
                // Update student's overall GWA in student.txt
                updateStudentGWA(studentIdField.getText().trim());
                
                // Update course statuses based on grades entered
                AcademicUtilities.updateCourseStatuses(studentID, 
                    semesterField.getSelectedItem().toString(), 
                    yearLevelField.getSelectedItem().toString());
                
                JOptionPane.showMessageDialog(this,"Record Updated!");
            }
            else JOptionPane.showMessageDialog(this,"Record not found");
            
            // Reload enrollment data to refresh any changes
            loadEnrollmentData();
            loadAllRecordsToTable();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Delete a marksheet record for a student in a specific semester and year level
     */
    private void deleteRecord() {
        String studentID = studentIdField.getText().trim();
        
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a Student ID to delete a record.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return;
        }
        
        String semester = semesterField.getSelectedItem().toString();
        String yearLevel = yearLevelField.getSelectedItem().toString();
        String compressedSemester = compressSemester(semester);
        
        // Confirm deletion
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the marksheet record for:\n\n" +
            "Student ID: " + studentID + "\n" +
            "Semester: " + semester + "\n" +
            "Year Level: " + yearLevel + "\n\n" +
            "This action cannot be undone!",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (choice != JOptionPane.YES_OPTION) {
            return; // User cancelled
        }
        
        try {
            File inputFile = new File(getMarksheetPath());
            File tempFile = new File("temp_marksheet.txt");
            boolean deleted = false;

            try(BufferedReader br = new BufferedReader(new FileReader(inputFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

                String line;
                while((line = br.readLine()) != null) {
                    String[] d = line.split(",");
                    if(d.length < 15) {
                        pw.println(line);
                        continue;
                    }
                    
                    // Skip the line if it matches the record to delete
                    if(d[1].equals(studentID) &&
                       d[2].equals(compressedSemester) &&
                       d[3].equals(yearLevel)) {
                        deleted = true;
                        continue; // Don't write this line to temp file
                    }
                    
                    pw.println(line);
                }
            }
            
            inputFile.delete();
            tempFile.renameTo(inputFile);
            
            if(deleted) {
                // Update student's overall GWA in student.txt after deletion
                updateStudentGWA(studentID);
                
                JOptionPane.showMessageDialog(this,
                    "Record deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields after successful deletion
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No record found for the specified student, semester, and year level.",
                    "Record Not Found",
                    JOptionPane.WARNING_MESSAGE);
            }
            
            loadAllRecordsToTable();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error deleting record: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAsDropped() {
        String studentID = studentIdField.getText().trim();
        
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a Student ID to mark enrollment as dropped.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return;
        }
        
        String semester = semesterField.getSelectedItem().toString();
        String yearLevel = yearLevelField.getSelectedItem().toString();
        String compressedSemester = compressSemester(semester);
        
        // Confirm marking as dropped
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to mark this enrollment as DROPPED?\n\n" +
            "Student ID: " + studentID + "\n" +
            "Semester: " + semester + "\n" +
            "Year Level: " + yearLevel + "\n\n" +
            "This will update the enrollment status to DROPPED.",
            "Confirm Mark as Dropped",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (choice != JOptionPane.YES_OPTION) {
            return; // User cancelled
        }
        
        try {
            // Get enrollment file path
            String[] enrollmentPaths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            String enrollmentPath = FilePathResolver.resolveFilePath(enrollmentPaths);
            
            File inputFile = new File(enrollmentPath);
            File tempFile = new File("temp_enrollment.txt");
            boolean updated = false;
            
            try(BufferedReader br = new BufferedReader(new FileReader(inputFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                
                String line;
                while((line = br.readLine()) != null) {
                    String[] d = line.split(",");
                    if(d.length < 6) {
                        pw.println(line);
                        continue;
                    }
                    
                    // Format: StudentID, CourseList, YearLevel, Semester, Status, SectionList, AcademicYear
                    // Check if this enrollment matches student, semester, and year level
                    if(d[0].equals(studentID) &&
                       d[3].equals(compressedSemester) &&
                       d[2].equals(yearLevel)) {
                        // Update status to DROPPED
                        String academicYear = d.length > 6 ? d[6] : AcademicUtilities.getAcademicYear();
                        pw.println(d[0] + "," + d[1] + "," + d[2] + "," + d[3] + ",DROPPED," + d[5] + "," + academicYear);
                        updated = true;
                    } else {
                        pw.println(line);
                    }
                }
            }
            
            inputFile.delete();
            tempFile.renameTo(inputFile);
            
            if(updated) {
                // Reload enrollment data to reflect changes
                loadEnrollmentData();
                
                JOptionPane.showMessageDialog(this,
                    "Enrollment status updated to DROPPED successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No enrollment record found for the specified student, semester, and year level.",
                    "Record Not Found",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error updating enrollment status: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    private String getNextMRK(){
        int max=0;
        try(BufferedReader br = new BufferedReader(new FileReader(getMarksheetPath()))){
            String line;
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length>0){
                    String mrk = d[0].replace("MRK-","");
                    max = Math.max(max,Integer.parseInt(mrk));
                }
            }
        } catch(Exception ignored){}
        return String.format("MRK-%03d",max+1);
    }
    
    /**
     * Checks if a marksheet record already exists for the given student, semester, and year level.
     * @param studentID The student ID to check
     * @param semester The semester to check
     * @param yearLevel The year level to check
     * @return true if record exists, false otherwise
     */
    private boolean recordExists(String studentID, String semester, String yearLevel) {
        String compressedSem = compressSemester(semester);
        try(BufferedReader br = new BufferedReader(new FileReader(getMarksheetPath()))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if(d.length >= 4 && d[1].equals(studentID) && d[2].equals(compressedSem) && d[3].equals(yearLevel)) {
                    return true;
                }
            }
        } catch(Exception e) { 
            // File might not exist yet, that's okay
        }
        return false;
    }

    private double calculateGPA(){
        double sum=0; int count=0;
        for(JComboBox<String> cb : courseScores){
            String scoreText = cb.getSelectedItem().toString();
            // Skip PENDING scores in calculation
            if (scoreText.equals("PENDING") || scoreText.isEmpty()) {
                continue;
            }
            try { 
                sum+=Double.parseDouble(scoreText); 
                count++;
            } catch(Exception ignored){}
        }
        return count>0 ? sum/count : 0;
    }

    private String getCourseCode(String name) {
        for (Map.Entry<String, String> e : courseMap.entrySet())
            if (e.getValue().equals(name)) return e.getKey();
        return name;
    }

    /**
     * Validate if a grade follows the Filipino grading system (1.0 to 5.0)
     * @param gradeStr the grade string to validate
     * @return true if valid (1.0 to 5.0), false otherwise
     */
    private boolean isValidGrade(String gradeStr) {
        try {
            double grade = Double.parseDouble(gradeStr.trim());
            return grade >= 1.0 && grade <= 5.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate all course scores follow Filipino grading system
     * @return true if all scores are valid, false otherwise
     */
    private boolean validateAllGrades() {
        for (int i = 0; i < courseScores.length; i++) {
            String gradeStr = courseScores[i].getSelectedItem().toString();
            
            // Check for PENDING - this means updating an auto-generated record
            if (gradeStr.equals("PENDING")) {
                JOptionPane.showMessageDialog(this,
                    "Course " + (i + 1) + " still has PENDING grade.\n\n" +
                    "Please select actual grades (1.0 - 5.0) for all courses\n" +
                    "before saving this record.\n\n" +
                    "This record was auto-generated during enrollment.",
                    "Pending Grade Detected", JOptionPane.WARNING_MESSAGE);
                courseScores[i].requestFocus();
                return false;
            }
            
            // Since we're using a dropdown with predefined values,
            // all selected grades are automatically valid
            // No need for additional validation
        }
        return true;
    }

    /**
     * Calculate the overall GWA for a student across all their marksheet records
     * Skips records with 0.00 GPA (auto-generated pending records)
     */
    private double calculateOverallGWA(String studentID) {
        double totalGPA = 0;
        int semesterCount = 0;
        
        try(BufferedReader br = new BufferedReader(new FileReader(getMarksheetPath()))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length >= 14 && parts[1].equals(studentID)) {
                    // Last field is the semester GPA
                    try {
                        double semesterGPA = Double.parseDouble(parts[parts.length - 1]);
                        // Skip auto-generated records with 0.00 GPA (PENDING grades)
                        if (semesterGPA > 0.0) {
                            totalGPA += semesterGPA;
                            semesterCount++;
                        }
                    } catch(NumberFormatException ignored) {}
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return semesterCount > 0 ? totalGPA / semesterCount : 0.0;
    }

    /**
     * Update the student's GWA field in student.txt
     */
    private void updateStudentGWA(String studentID) {
        double overallGWA = calculateOverallGWA(studentID);
        
        String studentPath = FilePathResolver.resolveStudentFilePath();
        
        try {
            File inputFile = new File(studentPath);
            File tempFile = new File("temp_student.txt");
            boolean updated = false;
            
            try(BufferedReader br = new BufferedReader(new FileReader(inputFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
                
                String line;
                while((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    // Student format: ID, Name, Age, DOB, YearLevel, Section, StudentType, SubjectsEnrolled, GWA, Email, PhoneNumber, Gender, Address, FathersName, MothersName, GuardiansPhoneNumber
                    if(parts.length >= 16 && parts[0].equals(studentID)) {
                        // Update the GWA field (index 8)
                        parts[8] = String.format("%.2f", overallGWA);
                        pw.println(String.join(",", parts));
                        updated = true;
                    } else {
                        pw.println(line);
                    }
                }
            }
            
            if (updated) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                System.out.println("Updated GWA for student " + studentID + " to " + String.format("%.2f", overallGWA));
            } else {
                tempFile.delete();
                System.err.println("Student " + studentID + " not found in student.txt");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Set the semester dropdown to the current active semester from academic calendar
     */
    private void setCurrentActiveSemester() {
        try {
            String currentSemester = AcademicUtilities.getCurrentSemester();
            if (currentSemester != null && !currentSemester.isEmpty()) {
                semesterField.setSelectedItem(currentSemester);
                searchSemField.setSelectedItem(currentSemester);
                System.out.println("Set active semester to: " + currentSemester);
            }
        } catch (Exception e) {
            System.err.println("Could not set current semester: " + e.getMessage());
        }
    }
    
    /**
     * Public method to refresh semester selection - called when switching tabs
     */
    public void refreshSemesterSelection() {
        setCurrentActiveSemester();
    }
    
    /**
     * Compress semester for file storage: "1st Semester" -> "1"
     */
    private String compressSemester(String fullSemester) {
        switch (fullSemester) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return fullSemester; // Return as-is if already compressed
        }
    }
    
    /**
     * Expand semester for display: "1" -> "1st Semester"
     */
    private String expandSemester(String compressedSemester) {
        switch (compressedSemester.trim()) {
            case "1": return "1st Semester";
            case "2": return "2nd Semester";
            case "3": return "Summer";
            default: return compressedSemester; // Return as-is if already expanded
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Score Tab");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ScoreTab());
            frame.setVisible(true);
        });
    }
}