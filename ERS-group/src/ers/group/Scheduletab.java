package ers.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fedoc
 */
public class Scheduletab extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Scheduletab.class.getName());
    private ArrayList<Student> students;
    private StudentFileLoader studentFileLoader;
    private final ArrayList<Schedule> schedules;
    private final Map<String, ArrayList<String>> studentCourses;  // Maps studentID to list of courseIDs

    /**
     * Creates new form studenthub2
     */
    public Scheduletab() {
        initComponents();
        
        // Initialize data
        students = new ArrayList<>();
        schedules = new ArrayList<>();
        studentCourses = new HashMap<>();
        loadStudentData();
        loadScheduleData();
        loadStudentCourseMappings();

        // Fix Year spinner
        yearSpinner.setModel(new javax.swing.SpinnerNumberModel(
            2026, // default
            2000, // min
            2100, // max
            1     // step
        ));

        // Set correct months
        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        }));

        // Initial label update
        updateMonthYearLabel();

        // Update label when year changes
        yearSpinner.addChangeListener(e -> updateMonthYearLabel());
        
        // Set window properties
        setTitle("Student Management System - ERS");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null); // Center window on screen
        setResizable(true);
        
        // Load initial data into tables
        loadStudentTableData();
    }
    
    private void loadStudentData() {
        try {
            // Try multiple possible paths
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/student.txt",
                "src/ers/group/master files/student.txt",
                "master files/student.txt",
                "student.txt",
                "ERS-group/student.txt",
                "../student.txt",
                new java.io.File(".").getAbsolutePath() + "/ERS-group/src/ers/group/master files/student.txt"
            };
            
            String filePath = null;
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    filePath = path;
                    logger.info("Found student data at: " + f.getAbsolutePath());
                    break;
                }
            }
            
            if (filePath == null) {
                logger.warning("Could not find student.txt in any expected location");
                students = new ArrayList<>();
                return;
            }
            
            studentFileLoader = new StudentFileLoader();
            studentFileLoader.load(filePath);
            Collection<Student> allStudents = studentFileLoader.getAllStudents();
            students = new ArrayList<>(allStudents);
            logger.info("Loaded " + students.size() + " students from file");
        } catch (Exception e) {
            logger.severe("Error loading student data: " + e.getMessage());
            students = new ArrayList<>();
        }
    }
    
    private void loadStudentTableData() {
        DefaultTableModel model = null;
        javax.swing.JTable table = null;
        
        // Try to get existing model from table if it exists
        if (studentTablePanel.getComponentCount() > 0 && studentTablePanel.getComponent(0) instanceof javax.swing.JScrollPane) {
            javax.swing.JScrollPane pane = (javax.swing.JScrollPane) studentTablePanel.getComponent(0);
            if (pane.getViewport().getView() instanceof javax.swing.JTable) {
                table = (javax.swing.JTable) pane.getViewport().getView();
                if (table.getModel() instanceof DefaultTableModel) {
                    model = (DefaultTableModel) table.getModel();
                }
            }
        }
        
        // Create a proper table if it doesn't exist
        if (model == null) {
            model = new DefaultTableModel(
                new String[]{"Student ID", "Name", "Age", "DOB", "Year Level", "Type", "GWA", "Email", "Phone"},
                0
            );
            table = new javax.swing.JTable(model);
            table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
            
            // Clear panel and add the new table
            studentTablePanel.removeAll();
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
            studentTablePanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        } else {
            model.setRowCount(0);
        }
        
        // Add student data to table
        for (Student stud : students) {
            model.addRow(new Object[]{
                stud.getStudentID(),
                stud.getStudentName(),
                stud.getAge(),
                stud.getDateOfBirth(),
                stud.getYearLevel(),
                stud.getStudentType(),
                stud.getGwa(),
                stud.getEmail(),
                stud.getPhoneNumber()
            });
        }
        
        // Refresh display
        if (studentTablePanel.getParent() != null) {
            studentTablePanel.revalidate();
            studentTablePanel.repaint();
        }
    }
    
    private void loadScheduleData() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/Schedule.txt",
                "src/ers/group/master files/Schedule.txt",
                "ERS-group/src/ers/group/master files/schedule.txt",
                "src/ers/group/master files/schedule.txt",
                "Schedule.txt",
                "schedule.txt",
                new java.io.File(".").getAbsolutePath() + "/ERS-group/src/ers/group/master files/Schedule.txt"
            };
            
            String filePath = null;
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    filePath = path;
                    break;
                }
            }
            
            if (filePath == null) {
                logger.warning("Could not find schedule.txt");
                return;
            }
            
            try (java.util.Scanner scanner = new java.util.Scanner(new java.io.File(filePath))) {
                scanner.nextLine(); // skip header
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length < 7) continue;
                    
                    Schedule schedule = new Schedule(
                        parts[0].trim(),    // scheduleID
                        parts[1].trim(),    // courseID
                        parts[2].trim(),    // room
                        parts[3].trim(),    // day
                        parts[4].trim(),    // startTime
                        parts[5].trim(),    // endTime
                        parts[6].trim()     // teacherName
                    );
                    schedules.add(schedule);
                }
                logger.info("Loaded " + schedules.size() + " schedules");
            }
        } catch (Exception e) {
            logger.warning("Error loading schedule data: " + e.getMessage());
        }
    }
    
    private void loadStudentCourseMappings() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/studentcourse.txt",
                "src/ers/group/master files/studentcourse.txt",
                "studentcourse.txt",
                new java.io.File(".").getAbsolutePath() + "/ERS-group/src/ers/group/master files/studentcourse.txt"
            };
            
            String filePath = null;
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    filePath = path;
                    break;
                }
            }
            
            if (filePath == null) {
                logger.warning("Could not find studentcourse.txt");
                return;
            }
            
            try (java.util.Scanner scanner = new java.util.Scanner(new java.io.File(filePath))) {
                scanner.nextLine(); // skip header
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split(",");
                    if (parts.length < 2) continue;
                    
                    String studentID = parts[0].trim();
                    String courseID = parts[1].trim();
                    
                    studentCourses.computeIfAbsent(studentID, k -> new ArrayList<>()).add(courseID);
                }
                logger.info("Loaded student-course mappings");
            }
        } catch (Exception e) {
            logger.warning("Error loading student-course mappings: " + e.getMessage());
        }
    }
    
    private void displayStudentSchedule(String studentID) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"},
            0
        );
        Scheduletable.setBackground(new java.awt.Color(230, 230, 240));
        
        ArrayList<String> courses = studentCourses.getOrDefault(studentID, new ArrayList<>());
        logger.info("Found " + courses.size() + " courses for student " + studentID + ": " + courses);
        logger.info("Total schedules available: " + schedules.size());
        
        // Initialize time slots (15-minute intervals for better granularity)
        String[] timeSlots = {
            "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00"
        };
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        
        // Create a 2D grid for the schedule - using CourseSlot objects to store course info and color
        Map<String, Map<String, CourseSlot>> scheduleGrid = new HashMap<>();
        for (String timeSlot : timeSlots) {
            Map<String, CourseSlot> dayMap = new HashMap<>();
            for (String day : days) {
                dayMap.put(day, null);
            }
            scheduleGrid.put(timeSlot, dayMap);
        }
        
        // Colors for courses - modern professional palette
        java.awt.Color[] colors = {
            new java.awt.Color(112, 161, 255),  // Soft Steel Blue
            new java.awt.Color(46, 213, 115),   // Emerald Mint
            new java.awt.Color(255, 165, 94),   // Soft Orange
            new java.awt.Color(255, 107, 129),  // Watermelon Red
            new java.awt.Color(179, 136, 255)   // Lavender Purple
        };
        
        // Assign colors to courses based on their ID (consistent mapping)
        Map<String, java.awt.Color> courseColorMap = new HashMap<>();
        for (int i = 0; i < courses.size(); i++) {
            courseColorMap.put(courses.get(i), colors[i % colors.length]);
            logger.info("Assigned color to course " + courses.get(i));
        }
        
        // Fill in the schedule
        for (String courseID : courses) {
            boolean found = false;
            java.awt.Color courseColor = courseColorMap.get(courseID);
            
            for (Schedule schedule : schedules) {
                if (schedule.getCourseID().equals(courseID)) {
                    String startTime = schedule.getStartTime().trim();
                    String endTime = schedule.getEndTime().trim();
                    String timeKey = parseTimeTo24Hour(startTime);
                    String endTimeKey = parseTimeTo24Hour(endTime);
                    
                    // Convert times to minutes for accurate comparison
                    int startMinutes = timeToMinutes(timeKey);
                    int endMinutes = timeToMinutes(endTimeKey);
                    
                    logger.info("Matched " + courseID + " from " + timeKey + " (" + startMinutes + "m) to " + endTimeKey + " (" + endMinutes + "m) on " + schedule.getDay());
                    
                    CourseSlot slot = new CourseSlot(courseID, schedule.getRoom(), courseColor);
                    
                    // Fill all time slots from start to end
                    for (String slot_time : timeSlots) {
                        int slotMinutes = timeToMinutes(slot_time);
                        // Fill if this slot is within the course time range (start inclusive, end exclusive)
                        if (slotMinutes >= startMinutes && slotMinutes < endMinutes) {
                            if (scheduleGrid.containsKey(slot_time)) {
                                Map<String, CourseSlot> daySlots = scheduleGrid.get(slot_time);
                                if (daySlots.get(schedule.getDay()) == null) {
                                    daySlots.put(schedule.getDay(), slot);
                                }
                            }
                        }
                    }
                    found = true;
                }
            }
            if (!found) {
                logger.warning("No schedule found for course " + courseID);
            }
        }
        
        // Populate table with visual representation
        for (String timeSlot : timeSlots) {
            Map<String, CourseSlot> dayMap = scheduleGrid.get(timeSlot);
            if (dayMap != null) {
                model.addRow(new Object[]{
                    timeSlot,
                    dayMap.getOrDefault("Monday", new CourseSlot("", "", null)),
                    dayMap.getOrDefault("Tuesday", new CourseSlot("", "", null)),
                    dayMap.getOrDefault("Wednesday", new CourseSlot("", "", null)),
                    dayMap.getOrDefault("Thursday", new CourseSlot("", "", null)),
                    dayMap.getOrDefault("Friday", new CourseSlot("", "", null)),
                    dayMap.getOrDefault("Saturday", new CourseSlot("", "", null))
                });
            }
        }
        
        Scheduletable.setModel(model);
        
        // Modernize the table
        Scheduletable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        Scheduletable.setRowHeight(30);
        Scheduletable.setShowGrid(false);
        Scheduletable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        Scheduletable.setCellSelectionEnabled(false);
        Scheduletable.setFocusable(false);
        
        // Header styling - match dark theme
        Scheduletable.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        Scheduletable.getTableHeader().setBackground(new java.awt.Color(0, 30, 58));
        Scheduletable.getTableHeader().setForeground(java.awt.Color.WHITE);
        Scheduletable.getTableHeader().setPreferredSize(new java.awt.Dimension(100, 40));
        ((javax.swing.table.DefaultTableCellRenderer)Scheduletable.getTableHeader().getDefaultRenderer())
            .setHorizontalAlignment(javax.swing.JLabel.CENTER);
        
        // Time column styling with zebra striping
        javax.swing.table.DefaultTableCellRenderer timeRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Zebra striping - alternating subtle background colors
                if (row % 2 == 0) {
                    setBackground(new java.awt.Color(255, 255, 255));
                } else {
                    setBackground(new java.awt.Color(248, 249, 252));
                }
                setHorizontalAlignment(javax.swing.JLabel.CENTER);
                setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
                setForeground(new java.awt.Color(0, 30, 58));
                return this;
            }
        };
        Scheduletable.getColumnModel().getColumn(0).setCellRenderer(timeRenderer);
        Scheduletable.getColumnModel().getColumn(0).setPreferredWidth(80);
        
        // Set custom cell renderer for colored schedule cells
        for (int i = 1; i < Scheduletable.getColumnCount(); i++) {
            Scheduletable.getColumnModel().getColumn(i).setCellRenderer(new ScheduleCellRenderer());
            Scheduletable.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
        
        Scheduletable.repaint();
        jScrollPane2.repaint();
    }
    
    // Inner class to store course information with color
    class CourseSlot {
        String courseID;
        String room;
        java.awt.Color color;
        
        CourseSlot(String courseID, String room, java.awt.Color color) {
            this.courseID = courseID;
            this.room = room;
            this.color = color;
        }
        
        @Override
        public String toString() {
            if (courseID.isEmpty()) return "";
            return courseID + "\n(" + room + ")";
        }
    }
    
    // Custom cell renderer for colored schedule cells with continuous block effect
    class ScheduleCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof CourseSlot) {
                CourseSlot slot = (CourseSlot) value;
                if (!slot.courseID.isEmpty()) {
                    setBackground(slot.color);
                    setForeground(new java.awt.Color(30, 30, 30));
                    setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
                    setHorizontalAlignment(javax.swing.JLabel.CENTER);
                    setVerticalAlignment(javax.swing.JLabel.CENTER);
                    
                    // Check if same course above/below for continuous block effect
                    boolean sameAbove = false;
                    boolean sameBelow = false;
                    
                    if (row > 0) {
                        Object aboveValue = table.getValueAt(row - 1, column);
                        if (aboveValue instanceof CourseSlot) {
                            sameAbove = ((CourseSlot) aboveValue).courseID.equals(slot.courseID);
                        }
                    }
                    
                    if (row < table.getRowCount() - 1) {
                        Object belowValue = table.getValueAt(row + 1, column);
                        if (belowValue instanceof CourseSlot) {
                            sameBelow = ((CourseSlot) belowValue).courseID.equals(slot.courseID);
                        }
                    }
                    
                    // Hide duplicate text - only show in top cell of block
                    if (sameAbove) {
                        setText("");
                    } else {
                        setText(slot.toString());
                    }
                    
                    // Light guide borders - faint lines for internal blocks, visible for edges
                    java.awt.Color lightLine = new java.awt.Color(255, 255, 255, 50);  // Very faint guide
                    java.awt.Color darkLine = new java.awt.Color(255, 255, 255, 120);  // Visible edge
                    
                    int topWidth = sameAbove ? 1 : 2;  // Thinner for internal, thicker for block start
                    int bottomWidth = sameBelow ? 1 : 2;  // Thinner for internal, thicker for block end               
                    java.awt.Color topColor = sameAbove ? lightLine : darkLine;
                    
                    // Create compound border: MatteBorder for guides + EmptyBorder for text padding
                    javax.swing.border.Border matteBorder = javax.swing.BorderFactory.createMatteBorder(
                        topWidth, 1, bottomWidth, 1, topColor
                    );
                    javax.swing.border.Border paddingBorder = javax.swing.BorderFactory.createEmptyBorder(
                        4, 6, 4, 6  // top, left, bottom, right padding for text
                    );
                    setBorder(javax.swing.BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
                } else {
                    // Empty cells with zebra striping effect
                    if (row % 2 == 0) {
                        setBackground(new java.awt.Color(255, 255, 255));
                    } else {
                        setBackground(new java.awt.Color(248, 249, 252));
                    }
                    setForeground(new java.awt.Color(100, 100, 100));
                    setText("");
                    setBorder(javax.swing.BorderFactory.createMatteBorder(
                        0, 0, 1, 1, new java.awt.Color(230, 230, 230)
                    ));
                }
            } else {
                // Null cells with zebra striping
                if (row % 2 == 0) {
                    setBackground(new java.awt.Color(255, 255, 255));
                } else {
                    setBackground(new java.awt.Color(248, 249, 252));
                }
                setForeground(new java.awt.Color(100, 100, 100));
                setText("");
                setBorder(javax.swing.BorderFactory.createMatteBorder(
                    0, 0, 1, 1, new java.awt.Color(230, 230, 230)
                ));
            }
            
            return this;
        }
    }
    
    private String parseTimeTo24Hour(String time) {
        // Convert "8:00 AM" or "1:00 PM" to "08:00" or "13:00"
        time = time.trim().toUpperCase();
        
        if (time.matches("\\d{1,2}:\\d{2}\\s*(AM|PM)")) {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0].trim());
            String minute = parts[1].split(" ")[0].trim();
            
            if (time.contains("PM") && hour != 12) {
                hour += 12;
            } else if (time.contains("AM") && hour == 12) {
                hour = 0;
            }
            
            return String.format("%02d:%s", hour, minute);
        }
        
        // If already in HH:MM format
        if (time.matches("\\d{1,2}:\\d{2}")) {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            return String.format("%02d:%s", hour, parts[1]);
        }
        
        return time;
    }
    
    private int timeToMinutes(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            try {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                return hours * 60 + minutes;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    private void updateMonthYearLabel() {
    String month = monthComboBox.getSelectedItem().toString();
    int year = (int) yearSpinner.getValue();
    monthYearDisplayLabel.setText(month + " " + year);
}

    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        alltabs = new javax.swing.JTabbedPane();
        student = new javax.swing.JPanel();
        studentFormPanel = new javax.swing.JPanel();
        studentTablePanel = new javax.swing.JPanel();
        course = new javax.swing.JPanel();
        courseFormPanel = new javax.swing.JPanel();
        courseTablePanel = new javax.swing.JPanel();
        score = new javax.swing.JPanel();
        scoreFormPanel = new javax.swing.JPanel();
        scoreTablePanel = new javax.swing.JPanel();
        Marksheet = new javax.swing.JPanel();
        marksheetFormPanel = new javax.swing.JPanel();
        marksheetTablePanel = new javax.swing.JPanel();
        footerPanel = new javax.swing.JPanel();
        Schedule = new javax.swing.JPanel();
        scheduleSearchPanel = new javax.swing.JPanel();
        scheduleFiltersPanel = new javax.swing.JPanel();
        semesterIDLabel = new javax.swing.JLabel();
        studentSearchField = new javax.swing.JTextField();
        searchStudentButton = new javax.swing.JButton();
        semesterSearchField = new javax.swing.JTextField();
        studentIDLabel = new javax.swing.JLabel();
        searchSemesterButton = new javax.swing.JButton();
        scheduleTablePanel = new javax.swing.JPanel();
        gwaLabel = new javax.swing.JLabel();
        monthYearPanel = new javax.swing.JPanel();
        monthComboBox = new javax.swing.JComboBox<>();
        yearSpinner = new javax.swing.JSpinner();
        monthYearDisplayLabel = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Scheduletable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(31, 58, 95));

        headerPanel.setBackground(new java.awt.Color(0, 30, 58));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Student Management System");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        alltabs.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N

        student.setBackground(new java.awt.Color(31, 58, 95));

        studentFormPanel.setBackground(new java.awt.Color(0, 30, 58));
        studentFormPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        javax.swing.GroupLayout studentFormPanelLayout = new javax.swing.GroupLayout(studentFormPanel);
        studentFormPanel.setLayout(studentFormPanelLayout);
        studentFormPanelLayout.setHorizontalGroup(
            studentFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );
        studentFormPanelLayout.setVerticalGroup(
            studentFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 581, Short.MAX_VALUE)
        );

        studentTablePanel.setBackground(new java.awt.Color(0, 30, 58));
        studentTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        studentTablePanel.setLayout(new java.awt.BorderLayout());
        
        // Create table for students
        javax.swing.JTable studentTable = new javax.swing.JTable(new DefaultTableModel(
            new String[]{"Student ID", "Name", "Age", "DOB", "Year Level", "Type", "GWA", "Email", "Phone"},
            0
        ));
        studentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        javax.swing.JScrollPane studentScrollPane = new javax.swing.JScrollPane(studentTable);
        studentTablePanel.add(studentScrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout studentLayout = new javax.swing.GroupLayout(student);
        student.setLayout(studentLayout);
        studentLayout.setHorizontalGroup(
            studentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studentFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(studentTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        studentLayout.setVerticalGroup(
            studentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(studentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(studentTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studentFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        alltabs.addTab("Student", student);

        course.setBackground(new java.awt.Color(31, 58, 95));

        courseFormPanel.setBackground(new java.awt.Color(0, 30, 58));
        courseFormPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        javax.swing.GroupLayout courseFormPanelLayout = new javax.swing.GroupLayout(courseFormPanel);
        courseFormPanel.setLayout(courseFormPanelLayout);
        courseFormPanelLayout.setHorizontalGroup(
            courseFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );
        courseFormPanelLayout.setVerticalGroup(
            courseFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 581, Short.MAX_VALUE)
        );

        courseTablePanel.setBackground(new java.awt.Color(0, 30, 58));
        courseTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        javax.swing.GroupLayout courseTablePanelLayout = new javax.swing.GroupLayout(courseTablePanel);
        courseTablePanel.setLayout(courseTablePanelLayout);
        courseTablePanelLayout.setHorizontalGroup(
            courseTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1314, Short.MAX_VALUE)
        );
        courseTablePanelLayout.setVerticalGroup(
            courseTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout courseLayout = new javax.swing.GroupLayout(course);
        course.setLayout(courseLayout);
        courseLayout.setHorizontalGroup(
            courseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(courseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(courseFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(courseTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        courseLayout.setVerticalGroup(
            courseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, courseLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(courseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(courseTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(courseFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        alltabs.addTab("Course", course);

        score.setBackground(new java.awt.Color(31, 58, 95));

        scoreFormPanel.setBackground(new java.awt.Color(0, 30, 58));
        scoreFormPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        javax.swing.GroupLayout scoreFormPanelLayout = new javax.swing.GroupLayout(scoreFormPanel);
        scoreFormPanel.setLayout(scoreFormPanelLayout);
        scoreFormPanelLayout.setHorizontalGroup(
            scoreFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );
        scoreFormPanelLayout.setVerticalGroup(
            scoreFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 581, Short.MAX_VALUE)
        );

        scoreTablePanel.setBackground(new java.awt.Color(0, 30, 58));
        scoreTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        javax.swing.GroupLayout scoreTablePanelLayout = new javax.swing.GroupLayout(scoreTablePanel);
        scoreTablePanel.setLayout(scoreTablePanelLayout);
        scoreTablePanelLayout.setHorizontalGroup(
            scoreTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1314, Short.MAX_VALUE)
        );
        scoreTablePanelLayout.setVerticalGroup(
            scoreTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout scoreLayout = new javax.swing.GroupLayout(score);
        score.setLayout(scoreLayout);
        scoreLayout.setHorizontalGroup(
            scoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scoreTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        scoreLayout.setVerticalGroup(
            scoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scoreLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(scoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scoreTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        alltabs.addTab("Score", score);

        Marksheet.setBackground(new java.awt.Color(31, 58, 95));

        marksheetFormPanel.setBackground(new java.awt.Color(0, 30, 58));
        marksheetFormPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        javax.swing.GroupLayout marksheetFormPanelLayout = new javax.swing.GroupLayout(marksheetFormPanel);
        marksheetFormPanel.setLayout(marksheetFormPanelLayout);
        marksheetFormPanelLayout.setHorizontalGroup(
            marksheetFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );
        marksheetFormPanelLayout.setVerticalGroup(
            marksheetFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 581, Short.MAX_VALUE)
        );

        marksheetTablePanel.setBackground(new java.awt.Color(0, 30, 58));
        marksheetTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        footerPanel.setBackground(new java.awt.Color(0, 30, 58));

        javax.swing.GroupLayout footerPanelLayout = new javax.swing.GroupLayout(footerPanel);
        footerPanel.setLayout(footerPanelLayout);
        footerPanelLayout.setHorizontalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1302, Short.MAX_VALUE)
        );
        footerPanelLayout.setVerticalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout marksheetTablePanelLayout = new javax.swing.GroupLayout(marksheetTablePanel);
        marksheetTablePanel.setLayout(marksheetTablePanelLayout);
        marksheetTablePanelLayout.setHorizontalGroup(
            marksheetTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(marksheetTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(footerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        marksheetTablePanelLayout.setVerticalGroup(
            marksheetTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(marksheetTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(footerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout MarksheetLayout = new javax.swing.GroupLayout(Marksheet);
        Marksheet.setLayout(MarksheetLayout);
        MarksheetLayout.setHorizontalGroup(
            MarksheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MarksheetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(marksheetFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(marksheetTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        MarksheetLayout.setVerticalGroup(
            MarksheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MarksheetLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(MarksheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(marksheetTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(marksheetFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        alltabs.addTab("Marksheet", Marksheet);

        Schedule.setBackground(new java.awt.Color(31, 58, 95));

        scheduleSearchPanel.setBackground(new java.awt.Color(0, 30, 58));
        scheduleSearchPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        scheduleFiltersPanel.setBackground(new java.awt.Color(0, 30, 58));
        scheduleFiltersPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        semesterIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        semesterIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        semesterIDLabel.setText("Semester's ID");

        studentSearchField.setBackground(new java.awt.Color(146, 190, 219));

        searchStudentButton.setBackground(new java.awt.Color(189, 216, 233));
        searchStudentButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        searchStudentButton.setText("Search");
        searchStudentButton.addActionListener(this::searchStudentButtonActionPerformed);

        semesterSearchField.setBackground(new java.awt.Color(146, 190, 219));

        studentIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        studentIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        studentIDLabel.setText("Student ID");

        searchSemesterButton.setBackground(new java.awt.Color(189, 216, 233));
        searchSemesterButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        searchSemesterButton.setText("Search");
        searchSemesterButton.addActionListener(this::searchSemesterButtonActionPerformed);

        javax.swing.GroupLayout scheduleFiltersPanelLayout = new javax.swing.GroupLayout(scheduleFiltersPanel);
        scheduleFiltersPanel.setLayout(scheduleFiltersPanelLayout);
        scheduleFiltersPanelLayout.setHorizontalGroup(
            scheduleFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleFiltersPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(scheduleFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(semesterIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(scheduleFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(scheduleFiltersPanelLayout.createSequentialGroup()
                            .addComponent(semesterSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchSemesterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, scheduleFiltersPanelLayout.createSequentialGroup()
                            .addComponent(studentSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31)
                            .addComponent(searchStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        scheduleFiltersPanelLayout.setVerticalGroup(
            scheduleFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleFiltersPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(studentIDLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(scheduleFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(studentSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStudentButton))
                .addGap(18, 18, 18)
                .addComponent(semesterIDLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(scheduleFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(semesterSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchSemesterButton))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        scheduleTablePanel.setBackground(new java.awt.Color(0, 30, 58));
        scheduleTablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        gwaLabel.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        gwaLabel.setForeground(new java.awt.Color(255, 255, 255));
        gwaLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gwaLabel.setText("GWA. 3.53");

        javax.swing.GroupLayout scheduleTablePanelLayout = new javax.swing.GroupLayout(scheduleTablePanel);
        scheduleTablePanel.setLayout(scheduleTablePanelLayout);
        scheduleTablePanelLayout.setHorizontalGroup(
            scheduleTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gwaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        scheduleTablePanelLayout.setVerticalGroup(
            scheduleTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gwaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout scheduleSearchPanelLayout = new javax.swing.GroupLayout(scheduleSearchPanel);
        scheduleSearchPanel.setLayout(scheduleSearchPanelLayout);
        scheduleSearchPanelLayout.setHorizontalGroup(
            scheduleSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleSearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scheduleSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scheduleFiltersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scheduleTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        scheduleSearchPanelLayout.setVerticalGroup(
            scheduleSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleSearchPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(scheduleFiltersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scheduleTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        monthYearPanel.setBackground(new java.awt.Color(0, 30, 58));
        monthYearPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        monthComboBox.addActionListener(this::monthComboBoxActionPerformed);

        monthYearDisplayLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        monthYearDisplayLabel.setForeground(new java.awt.Color(255, 255, 255));
        monthYearDisplayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        monthYearDisplayLabel.setText("January 2026");

        refreshButton.setBackground(new java.awt.Color(189, 216, 233));
        refreshButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(this::refreshButtonActionPerformed);

        Scheduletable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"08:00", "", "", "", "", "", ""},
                {"09:00", "", "", "", "", "", ""},
                {"10:00", "", "", "", "", "", ""},
                {"11:00", "", "", "", "", "", ""},
                {"12:00", "", "", "", "", "", ""},
                {"13:00", "", "", "", "", "", ""},
                {"14:00", "", "", "", "", "", ""},
                {"15:00", "", "", "", "", "", ""},
                {"16:00", "", "", "", "", "", ""},
                {"17:00", "", "", "", "", "", ""}
            },
            new String [] {
                "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            }
        ));
        Scheduletable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(Scheduletable);

        javax.swing.GroupLayout monthYearPanelLayout = new javax.swing.GroupLayout(monthYearPanel);
        monthYearPanel.setLayout(monthYearPanelLayout);
        monthYearPanelLayout.setHorizontalGroup(
            monthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monthYearPanelLayout.createSequentialGroup()
                .addGroup(monthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(monthYearPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(monthYearDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 627, Short.MAX_VALUE))
                    .addGroup(monthYearPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        monthYearPanelLayout.setVerticalGroup(
            monthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monthYearPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(monthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthYearDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ScheduleLayout = new javax.swing.GroupLayout(Schedule);
        Schedule.setLayout(ScheduleLayout);
        ScheduleLayout.setHorizontalGroup(
            ScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScheduleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scheduleSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(monthYearPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ScheduleLayout.setVerticalGroup(
            ScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ScheduleLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(ScheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(monthYearPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scheduleSearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        alltabs.addTab("Schedule", Schedule);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alltabs))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(alltabs)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }                                                       

private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateMonthYearLabel();
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        studentSearchField.setText("");
        gwaLabel.setText("GWA. --");
        // Clear the schedule table
        DefaultTableModel emptyModel = new DefaultTableModel(
            new String[]{"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"},
            0
        );
        Scheduletable.setModel(emptyModel);
        loadStudentTableData();
    }
    
    private void searchStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
        String searchID = studentSearchField.getText().trim();
        if (searchID.isEmpty()) {
            loadStudentTableData();
            gwaLabel.setText("GWA. --");
            return;
        }
        
        // Find student by ID
        for (Student student : students) {
            if (student.getStudentID().equals(searchID)) {
                // Display GWA
                gwaLabel.setText("GWA. " + student.getGwa());
                
                // Display student's schedule
                displayStudentSchedule(searchID);
                
                // Update table to show only this student
                DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Student ID", "Name", "Age", "DOB", "Year Level", "Type", "GWA", "Email", "Phone"},
                    0
                );
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.getAge(),
                    student.getDateOfBirth(),
                    student.getYearLevel(),
                    student.getStudentType(),
                    student.getGwa(),
                    student.getEmail(),
                    student.getPhoneNumber()
                });
                
                // Create and set table to display in schedule tab
                if (studentTablePanel.getComponentCount() > 0) {
                    studentTablePanel.remove(0);
                }
                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(new javax.swing.JTable(model));
                studentTablePanel.add(scrollPane);
                studentTablePanel.revalidate();
                studentTablePanel.repaint();
                return;
            }
        }
        
        gwaLabel.setText("GWA. Not found");
    }
    
    private void searchSemesterButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO: Implement semester search
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
        java.awt.EventQueue.invokeLater(() -> new Scheduletab().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel gwaLabel;
    private javax.swing.JPanel Marksheet;
    private javax.swing.JComboBox<String> monthComboBox;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel Schedule;
    private javax.swing.JTable Scheduletable;
    private javax.swing.JButton searchSemesterButton;
    private javax.swing.JTextField studentSearchField;
    private javax.swing.JTextField semesterSearchField;
    private javax.swing.JButton searchStudentButton;
    private javax.swing.JLabel semesterIDLabel;
    private javax.swing.JLabel studentIDLabel;
    private javax.swing.JSpinner yearSpinner;
    private javax.swing.JTabbedPane alltabs;
    private javax.swing.JPanel course;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel monthYearDisplayLabel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel scoreFormPanel;
    private javax.swing.JPanel scoreTablePanel;
    private javax.swing.JPanel marksheetFormPanel;
    private javax.swing.JPanel marksheetTablePanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel scheduleSearchPanel;
    private javax.swing.JPanel scheduleFiltersPanel;
    private javax.swing.JPanel scheduleTablePanel;
    private javax.swing.JPanel monthYearPanel;
    private javax.swing.JPanel studentFormPanel;
    private javax.swing.JPanel studentTablePanel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel courseFormPanel;
    private javax.swing.JPanel courseTablePanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel score;
    private javax.swing.JPanel student;
    // End of variables declaration                   
}
