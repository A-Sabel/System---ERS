package ers.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class CourseTab extends JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CourseTab.class.getName());
    private ArrayList<Student> students;
    private StudentFileLoader studentFileLoader;
    private StudentFileSaver studentFileSaver;
    private String studentFilePath;
    private CourseSubjectFileLoader courseLoader;
    private EnrollmentFileLoader enrollmentLoader;
    private EnrollmentFileSaver enrollmentFileSaver;
    private ArrayList<Enrollment> enrollments;
    private Map<String, CourseSubject> availableCourses;
    private static final int MAX_SECTION_CAPACITY = 5;
    private static final int MAX_STUDENTS_PER_COURSE = MAX_SECTION_CAPACITY * 3; // 15 students total

    public CourseTab() {
        initComponents();
        students = new ArrayList<>();
        enrollments = new ArrayList<>();
        availableCourses = new HashMap<>();
        courseLoader = new CourseSubjectFileLoader();
        enrollmentLoader = new EnrollmentFileLoader();
        enrollmentFileSaver = new EnrollmentFileSaver();
        studentFileSaver = new StudentFileSaver();
        loadCourseData();
        loadEnrollmentData();
        loadStudentData();
        loadStudentTableData();
        setCurrentActiveSemester();
    }

    public void refreshStudentList() {
        loadStudentData();
        loadStudentTableData();
        logger.info("Student list refreshed");
    }
    
    public void autoPopulateStudent(String studentID) {
        CT_StudentID.setText(studentID);
        logger.info("Auto-populated student: " + studentID);
        CT_LoadCoursesActionPerformed(null);
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
                    studentFilePath = path; // Store for later use in saving
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
            
            // Filter out graduated students from enrollment operations
            students = new ArrayList<>();
            for (Student s : allStudents) {
                if (!"Graduate".equals(s.getStatus())) {
                    students.add(s);
                }
            }
            
            logger.info("Loaded " + students.size() + " active students from file (excluded graduates)");
        } catch (Exception e) {
            logger.severe("Error loading student data: " + e.getMessage());
            students = new ArrayList<>();
        }
    }

    private void loadCourseData() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/courseSubject.txt",
                "src/ers/group/master files/courseSubject.txt",
                "master files/courseSubject.txt",
                "courseSubject.txt"
            };
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    logger.info("Found course data at: " + f.getAbsolutePath());
                    courseLoader.load(f.getAbsolutePath()); // <-- FIX
                    availableCourses = courseLoader.getSubjectMap();
                    logger.info("Loaded " + availableCourses.size() + " courses from file");
                    return;
                }
            }
            logger.warning("Could not find courseSubject.txt");
        } catch (Exception e) {
            logger.severe("Error loading course data: " + e.getMessage());
        }
    }

    private void loadEnrollmentData() {
        try {
            String[] possiblePaths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    logger.info("Found enrollment data at: " + f.getAbsolutePath());
                    enrollmentLoader.load(f.getAbsolutePath()); // <-- FIX
                    enrollments = new ArrayList<>(enrollmentLoader.getAllEnrollments());
                    logger.info("Loaded " + enrollments.size() + " enrollments from file");
                    return;
                }
            }
            enrollments = new ArrayList<>();
        } catch (Exception e) {
            logger.severe("Error loading enrollment data: " + e.getMessage());
            enrollments = new ArrayList<>();
        }
    }

    private void loadStudentTableData() {
        DefaultTableModel model = (DefaultTableModel) CT_Table.getModel();
        model.setRowCount(0);
        // Add student data to table
        for (Student stud : students) {
            model.addRow(new Object[]{
                stud.getStudentID(),
                stud.getStudentName(),
                stud.getDateOfBirth(),
                stud.getGender(),
                stud.getEmail(),
                stud.getPhoneNumber(),
                stud.getFathersName(),
                stud.getMothersName(),
                stud.getGuardiansPhoneNumber(),
                stud.getAddress()
            });
        }
    }

    private void initComponents() {
        CT_LeftPanel = new javax.swing.JPanel();
        CT_id = new javax.swing.JTextField();
        CT_StudentID = new javax.swing.JTextField();
        CT_ID = new javax.swing.JLabel();
        CT_STUDENT_ID = new javax.swing.JLabel();
        CT_SEMESTER = new javax.swing.JLabel();
        CT_COURSE1 = new javax.swing.JLabel();
        CT_COURSE2 = new javax.swing.JLabel();
        CT_COURSE3 = new javax.swing.JLabel();
        CT_COURSE4 = new javax.swing.JLabel();
        CT_COURSE5 = new javax.swing.JLabel();
        CT_Course1 = new javax.swing.JComboBox<>();
        CT_Course2 = new javax.swing.JComboBox<>();
        CT_Course3 = new javax.swing.JComboBox<>();
        CT_Course4 = new javax.swing.JComboBox<>();
        CT_Course5 = new javax.swing.JComboBox<>();
        CT_Semester = new javax.swing.JComboBox<>();
        CT_LoadCourses = new javax.swing.JButton();
        CT_PrereqStatus1 = new javax.swing.JLabel();
        CT_PrereqStatus2 = new javax.swing.JLabel();
        CT_PrereqStatus3 = new javax.swing.JLabel();
        CT_PrereqStatus4 = new javax.swing.JLabel();
        CT_PrereqStatus5 = new javax.swing.JLabel();
        CT_RightPanel = new javax.swing.JPanel();
        CT_SearchStudentPanel = new javax.swing.JPanel();
        CT_SEARCH_STUDENT = new javax.swing.JLabel();
        CT_SearchStudent = new javax.swing.JTextField();
        CT_Search = new javax.swing.JButton();
        CT_Refresh = new javax.swing.JButton();
        CT_TableScrollPane = new javax.swing.JScrollPane();
        CT_Table = new javax.swing.JTable();
        CT_BottomPanel = new javax.swing.JPanel();
        CT_Save = new javax.swing.JButton();
        CT_Update = new javax.swing.JButton();
        CT_Print = new javax.swing.JButton();
        CT_Clear = new javax.swing.JButton();
        CT_Logout = new javax.swing.JButton();

        this.setBackground(new java.awt.Color(31, 58, 95));

        CT_LeftPanel.setBackground(new java.awt.Color(0, 30, 58));
        CT_LeftPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        CT_LeftPanel.setPreferredSize(new java.awt.Dimension(460, 703));

        CT_id.setBackground(new java.awt.Color(146, 190, 219));
        CT_id.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_id.addActionListener(this::CT_idActionPerformed);

        CT_StudentID.setBackground(new java.awt.Color(146, 190, 219));
        CT_StudentID.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_StudentID.addActionListener(this::CT_StudentIDActionPerformed);
        // Auto-populate courses when Student ID is entered
        CT_StudentID.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { autoLoadCoursesIfValid(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { autoLoadCoursesIfValid(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { autoLoadCoursesIfValid(); }
        });

        CT_ID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_ID.setForeground(new java.awt.Color(255, 255, 255));
        CT_ID.setText("ID");

        CT_STUDENT_ID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_STUDENT_ID.setForeground(new java.awt.Color(255, 255, 255));
        CT_STUDENT_ID.setText("Student ID");

        CT_SEMESTER.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_SEMESTER.setForeground(new java.awt.Color(255, 255, 255));
        CT_SEMESTER.setText("Semester");

        CT_COURSE1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_COURSE1.setForeground(new java.awt.Color(255, 255, 255));
        CT_COURSE1.setText("Course 1");

        CT_COURSE2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_COURSE2.setForeground(new java.awt.Color(255, 255, 255));
        CT_COURSE2.setText("Course 2");

        CT_COURSE3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_COURSE3.setForeground(new java.awt.Color(255, 255, 255));
        CT_COURSE3.setText("Course 3");

        CT_COURSE4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_COURSE4.setForeground(new java.awt.Color(255, 255, 255));
        CT_COURSE4.setText("Course 4");

        CT_COURSE5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        CT_COURSE5.setForeground(new java.awt.Color(255, 255, 255));
        CT_COURSE5.setText("Course 5");

        CT_Course1.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Course --" }));
        CT_Course1.setMinimumSize(new java.awt.Dimension(90, 28));
        CT_Course1.addActionListener(e -> {
            updateCourseDropdowns();
            updatePrerequisiteStatus(CT_Course1, CT_PrereqStatus1);
        });

        CT_Course2.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Course --" }));
        CT_Course2.setMinimumSize(new java.awt.Dimension(90, 28));
        CT_Course2.addActionListener(e -> {
            updateCourseDropdowns();
            updatePrerequisiteStatus(CT_Course2, CT_PrereqStatus2);
        });

        CT_Course3.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Course --" }));
        CT_Course3.setMinimumSize(new java.awt.Dimension(90, 28));
        CT_Course3.addActionListener(e -> {
            updateCourseDropdowns();
            updatePrerequisiteStatus(CT_Course3, CT_PrereqStatus3);
        });

        CT_Course4.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Course --" }));
        CT_Course4.setMinimumSize(new java.awt.Dimension(90, 28));
        CT_Course4.addActionListener(e -> {
            updateCourseDropdowns();
            updatePrerequisiteStatus(CT_Course4, CT_PrereqStatus4);
        });

        CT_Course5.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Course --" }));
        CT_Course5.setMinimumSize(new java.awt.Dimension(90, 28));
        CT_Course5.addActionListener(e -> {
            updateCourseDropdowns();
            updatePrerequisiteStatus(CT_Course5, CT_PrereqStatus5);
        });

        CT_Semester.setBackground(new java.awt.Color(146, 190, 219));
        CT_Semester.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Semester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Semester", "2nd Semester", "Summer" }));
        // Auto-populate courses when semester is changed
        CT_Semester.addActionListener(e -> autoLoadCoursesIfValid());

        // Load Courses Button
        CT_LoadCourses.setBackground(new java.awt.Color(73, 118, 159));
        CT_LoadCourses.setFont(new java.awt.Font("Segoe UI", 1, 14));
        CT_LoadCourses.setForeground(new java.awt.Color(255, 255, 255));
        CT_LoadCourses.setText("Load Available Courses");
        CT_LoadCourses.addActionListener(this::CT_LoadCoursesActionPerformed);

        // Prerequisite Status Labels
        CT_PrereqStatus1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        CT_PrereqStatus1.setForeground(new java.awt.Color(189, 216, 233));
        CT_PrereqStatus1.setText("");

        CT_PrereqStatus2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        CT_PrereqStatus2.setForeground(new java.awt.Color(189, 216, 233));
        CT_PrereqStatus2.setText("");

        CT_PrereqStatus3.setFont(new java.awt.Font("Segoe UI", 1, 14));
        CT_PrereqStatus3.setForeground(new java.awt.Color(189, 216, 233));
        CT_PrereqStatus3.setText("");

        CT_PrereqStatus4.setFont(new java.awt.Font("Segoe UI", 1, 14));
        CT_PrereqStatus4.setForeground(new java.awt.Color(189, 216, 233));
        CT_PrereqStatus4.setText("");

        CT_PrereqStatus5.setFont(new java.awt.Font("Segoe UI", 1, 14));
        CT_PrereqStatus5.setForeground(new java.awt.Color(189, 216, 233));
        CT_PrereqStatus5.setText("");

        // Add listeners to update prerequisite status when courses are selected
        CT_Course1.addItemListener(e -> updatePrerequisiteStatus(CT_Course1, CT_PrereqStatus1));
        CT_Course2.addItemListener(e -> updatePrerequisiteStatus(CT_Course2, CT_PrereqStatus2));
        CT_Course3.addItemListener(e -> updatePrerequisiteStatus(CT_Course3, CT_PrereqStatus3));
        CT_Course4.addItemListener(e -> updatePrerequisiteStatus(CT_Course4, CT_PrereqStatus4));
        CT_Course5.addItemListener(e -> updatePrerequisiteStatus(CT_Course5, CT_PrereqStatus5));

        javax.swing.GroupLayout CT_LeftPanelLayout = new javax.swing.GroupLayout(CT_LeftPanel);
        CT_LeftPanel.setLayout(CT_LeftPanelLayout);
        CT_LeftPanelLayout.setHorizontalGroup(
            CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CT_LeftPanelLayout.createSequentialGroup()
                        .addComponent(CT_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CT_id, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CT_LeftPanelLayout.createSequentialGroup()
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CT_COURSE5)
                            .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(CT_COURSE2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CT_COURSE1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(CT_SEMESTER, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CT_STUDENT_ID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(CT_LeftPanelLayout.createSequentialGroup()
                                .addComponent(CT_Course1, 0, 300, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CT_PrereqStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(CT_Semester, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_StudentID, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CT_LeftPanelLayout.createSequentialGroup()
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CT_COURSE3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_COURSE4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CT_Course4, 0, 300, Short.MAX_VALUE)
                            .addComponent(CT_Course3, 0, 300, Short.MAX_VALUE)
                            .addComponent(CT_Course2, 0, 300, Short.MAX_VALUE)
                            .addComponent(CT_Course5, javax.swing.GroupLayout.Alignment.TRAILING, 0, 300, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CT_PrereqStatus2, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(CT_PrereqStatus3, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(CT_PrereqStatus4, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(CT_PrereqStatus5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)))
                    .addComponent(CT_LoadCourses, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        CT_LeftPanelLayout.setVerticalGroup(
            CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGap(32, 32, 32)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(CT_LeftPanelLayout.createSequentialGroup()
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CT_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CT_ID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CT_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CT_STUDENT_ID))
                        .addGap(18, 18, 18)
                        .addComponent(CT_Semester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(CT_SEMESTER))
                .addGap(18, 18, 18)
                .addComponent(CT_LoadCourses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE1)
                    .addComponent(CT_Course1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CT_PrereqStatus1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE2)
                    .addComponent(CT_Course2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CT_PrereqStatus2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE3)
                    .addComponent(CT_Course3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CT_PrereqStatus3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE4)
                    .addComponent(CT_Course4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CT_PrereqStatus4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE5)
                    .addComponent(CT_Course5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CT_PrereqStatus5))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        CT_RightPanel.setBackground(new java.awt.Color(0, 30, 58));
        CT_RightPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        CT_RightPanel.setPreferredSize(new java.awt.Dimension(460, 703));

        CT_SearchStudentPanel.setBackground(new java.awt.Color(0, 30, 58));
        CT_SearchStudentPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(189, 216, 233), 4, true));

        CT_SEARCH_STUDENT.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_SEARCH_STUDENT.setForeground(new java.awt.Color(255, 255, 255));
        CT_SEARCH_STUDENT.setText("Search Student");

        CT_SearchStudent.setBackground(new java.awt.Color(146, 190, 219));
        CT_SearchStudent.addActionListener(this::CT_SearchStudentActionPerformed);

        CT_Search.setBackground(new java.awt.Color(146, 190, 219));
        CT_Search.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Search.setText("Search");
        CT_Search.addActionListener(this::CT_SearchActionPerformed);

        CT_Refresh.setBackground(new java.awt.Color(146, 190, 219));
        CT_Refresh.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Refresh.setText("Refresh");
        CT_Refresh.addActionListener(this::CT_RefreshActionPerformed);

        javax.swing.GroupLayout CT_SearchStudentPanelLayout = new javax.swing.GroupLayout(CT_SearchStudentPanel);
        CT_SearchStudentPanel.setLayout(CT_SearchStudentPanelLayout);
        CT_SearchStudentPanelLayout.setHorizontalGroup(
            CT_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_SearchStudentPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(CT_SEARCH_STUDENT, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CT_SearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CT_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CT_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        CT_SearchStudentPanelLayout.setVerticalGroup(
            CT_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_SearchStudentPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(CT_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CT_SearchStudent)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CT_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CT_Search, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CT_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(CT_SEARCH_STUDENT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        CT_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name", "Mother's Name", "Guardian's Phone No.", "Address"
            }
        ));
        
        // Configure table selection behavior
        CT_Table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        CT_Table.setRowSelectionAllowed(true);
        CT_Table.setColumnSelectionAllowed(false);
        
        // Add selection listener to populate form when row is clicked
        CT_Table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && CT_Table.getSelectedRow() >= 0) {
                populateEnrollmentFormFromTable(CT_Table.getSelectedRow());
            }
        });
        
        CT_TableScrollPane.setViewportView(CT_Table);

        javax.swing.GroupLayout CT_RightPanelLayout = new javax.swing.GroupLayout(CT_RightPanel);
        CT_RightPanel.setLayout(CT_RightPanelLayout);
        CT_RightPanelLayout.setHorizontalGroup(
            CT_RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_RightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CT_RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CT_SearchStudentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CT_TableScrollPane))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CT_RightPanelLayout.setVerticalGroup(
            CT_RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_RightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CT_SearchStudentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CT_TableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addContainerGap())
        );

        CT_BottomPanel.setBackground(new java.awt.Color(0, 30, 58));
        CT_BottomPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));

        CT_Save.setBackground(new java.awt.Color(73, 118, 159));
        CT_Save.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Save.setText("Save");
        CT_Save.addActionListener(this::CT_SaveActionPerformed);

        CT_Update.setBackground(new java.awt.Color(73, 118, 159));
        CT_Update.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Update.setText("Update");

        CT_Print.setBackground(new java.awt.Color(73, 118, 159));
        CT_Print.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Print.setText("Print");

        CT_Clear.setBackground(new java.awt.Color(73, 118, 159));
        CT_Clear.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Clear.setText("Clear");
        CT_Clear.addActionListener(this::CT_ClearActionPerformed);

        CT_Logout.setBackground(new java.awt.Color(73, 118, 159));
        CT_Logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CT_Logout.setText("Logout");
        CT_Logout.addActionListener(this::CT_LogoutActionPerformed); 

        javax.swing.GroupLayout CT_BottomPanelLayout = new javax.swing.GroupLayout(CT_BottomPanel);
        CT_BottomPanel.setLayout(CT_BottomPanelLayout);
        CT_BottomPanelLayout.setHorizontalGroup(
            CT_BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CT_BottomPanelLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(CT_Save)
                .addGap(53, 53, 53)
                .addComponent(CT_Update)
                .addGap(59, 59, 59)
                .addComponent(CT_Print)
                .addGap(68, 68, 68)
                .addComponent(CT_Clear)
                .addGap(59, 59, 59)
                .addComponent(CT_Logout)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CT_BottomPanelLayout.setVerticalGroup(
            CT_BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CT_BottomPanelLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(CT_BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_Save)
                    .addComponent(CT_Update)
                    .addComponent(CT_Print)
                    .addComponent(CT_Clear)
                    .addComponent(CT_Logout))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout CourseTabLayout = new javax.swing.GroupLayout(this);
        this.setLayout(CourseTabLayout);
        CourseTabLayout.setHorizontalGroup(
            CourseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CourseTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CT_LeftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CourseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CT_RightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 990, Short.MAX_VALUE)
                    .addComponent(CT_BottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(335, 335, 335))
        );
        CourseTabLayout.setVerticalGroup(
            CourseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CourseTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CourseTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CT_LeftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addGroup(CourseTabLayout.createSequentialGroup()
                        .addComponent(CT_RightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CT_BottomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }                                        

    private void CT_idActionPerformed(java.awt.event.ActionEvent evt) {
        // Event handler for CT_id field (currently unused)
    }

    private void CT_StudentIDActionPerformed(java.awt.event.ActionEvent evt) {
        // Event handler for CT_StudentID field (currently unused)
    }
    
    private void resetToPending(javax.swing.JComboBox<String> combo, javax.swing.JLabel label) {
        combo.setSelectedIndex(0); // Reset to "-- Select Course --"
        label.setText(""); // Clear status
    }

    private void CT_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        // 1. Basic Student and Semester Validation
        String studentID = CT_StudentID.getText().trim();
        String semesterStr = (String) CT_Semester.getSelectedItem();
        if (studentID.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter a Student ID!", "Validation Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }
        
        if (student == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Student ID not found!", "Validation Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Graduate".equalsIgnoreCase(student.getStatus())) {
            javax.swing.JOptionPane.showMessageDialog(this, "Graduate students are not allowed to enroll.", "Enrollment Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Prepare Collection for Valid Courses
        String[] selectedCourseNames = {
            (String) CT_Course1.getSelectedItem(), (String) CT_Course2.getSelectedItem(),
            (String) CT_Course3.getSelectedItem(), (String) CT_Course4.getSelectedItem(),
            (String) CT_Course5.getSelectedItem()
        };

        ArrayList<CourseSubject> coursesToEnroll = new ArrayList<>();
        StringBuilder validationErrors = new StringBuilder();
        java.util.HashSet<String> duplicateCheckSet = new java.util.HashSet<>();
        String yearLevel = student.getYearLevel();

        // 3. THE GATEKEEPER LOOP
        for (String courseName : selectedCourseNames) {
            if (courseName == null || courseName.isEmpty() || courseName.startsWith("-- Select")) continue;

            if (!duplicateCheckSet.add(courseName)) {
                validationErrors.append("- ").append(courseName).append(": Duplicate selection.\n");
                continue;
            }

            CourseSubject course = findCourseByName(courseName);
            if (course == null) continue;

            AcademicUtilities.loadPrerequisites();
            java.util.List<String> missingPrereqs = AcademicUtilities.getMissingPrerequisites(studentID, course.getCourseSubjectID());
            
            if (!missingPrereqs.isEmpty()) {
                validationErrors.append("- ").append(courseName).append(": Missing prereqs (").append(String.join(", ", missingPrereqs)).append(")\n");
                continue;
            }

            if ("Summer".equalsIgnoreCase(semesterStr)) {
                boolean isValidSummerSubject = false;
                for (Enrollment e : enrollments) {
                    if (e.getStudentID().equals(studentID)) {
                        String status = e.getCourseStatus(course.getCourseSubjectID());
                        if ("FAILED".equalsIgnoreCase(status) || "DROPPED".equalsIgnoreCase(status) || "INC".equalsIgnoreCase(status)) {
                            isValidSummerSubject = true;
                            break;
                        }
                    }
                }
                if (!isValidSummerSubject) {
                    validationErrors.append("- ").append(courseName).append(": Only failed/dropped/incomplete courses allowed in Summer.\n");
                    continue;
                }
                
                // RETAKE LIMIT CHECK: Maximum 3 attempts per course
                if (!AcademicUtilities.canRetakeCourse(studentID, course.getCourseSubjectID())) {
                    int retakeCount = AcademicUtilities.getRetakeCount(studentID, course.getCourseSubjectID());
                    validationErrors.append("- ").append(courseName).append(": Maximum 3 retakes reached (").append(retakeCount).append(" attempts).\n");
                    continue;
                }
            }
            coursesToEnroll.add(course);
        }

        // 4. COURSE CAPACITY VALIDATION (15 students maximum per course)
        for (CourseSubject course : new ArrayList<>(coursesToEnroll)) {
            int enrolledCount = 0;
            String courseID = course.getCourseSubjectID();
            for (Enrollment e : enrollments) {
                if (e.getCourseID().equals(courseID) && "ENROLLED".equals(e.getStatus())) {
                    enrolledCount++;
                }
            }
            if (enrolledCount >= MAX_STUDENTS_PER_COURSE) {
                validationErrors.append("- ").append(course.getCourseSubjectName())
                    .append(": Course is at maximum capacity (").append(enrolledCount)
                    .append("/").append(MAX_STUDENTS_PER_COURSE).append(" students).\n");
                coursesToEnroll.remove(course);
            }
        }

        // 5. SUMMER UNIT CAP VALIDATION (6-15 units hard limit)
        if ("Summer".equalsIgnoreCase(semesterStr)) {
            int totalUnits = 0;
            for (CourseSubject course : coursesToEnroll) {
                totalUnits += course.getUnits();
            }
            if (totalUnits < 3) {
                validationErrors.append("- Summer enrollment requires at least 6 units (currently: ").append(totalUnits).append(" units).\n");
            } else if (totalUnits > 15) {
                validationErrors.append("- Summer enrollment cannot exceed 15 units (currently: ").append(totalUnits).append(" units).\n");
            }
        }

        if (coursesToEnroll.isEmpty() || validationErrors.length() > 0) {
            String msg = validationErrors.length() > 0 ? "Blocked:\n" + validationErrors.toString() : "Select a course.";
            javax.swing.JOptionPane.showMessageDialog(this, msg, "Enrollment Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 6. THE ENROLLMENT PHASE (Unified Record)
        int successCount = 0;
        StringBuilder courseIDsBuilder = new StringBuilder();
        StringBuilder sectionsBuilder = new StringBuilder();
        StringBuilder summary = new StringBuilder("Enrollment Summary for " + studentID + ":\n\n"); // FIXED: Added summary

        for (CourseSubject course : coursesToEnroll) {
            try {
                String sectionID = Schedule.assignSection(course.getCourseSubjectID(), studentID, MAX_SECTION_CAPACITY, course.isLabRoom(), course.getUnits(), semesterStr);
                Schedule.cacheStudentSectionAssignment(studentID, sectionID);
                
                if (courseIDsBuilder.length() > 0) {
                    courseIDsBuilder.append(";");
                    sectionsBuilder.append(";");
                }
                courseIDsBuilder.append(course.getCourseSubjectID());
                sectionsBuilder.append(sectionID);
                summary.append(course.getCourseSubjectID()).append(" (").append(sectionID).append(")\n");
                
                successCount++;
            } catch (Schedule.SectionFullException e) {
                // Specific handling for course at capacity
                String errorMsg = course.getCourseSubjectName() + ": Course is full (" + MAX_STUDENTS_PER_COURSE + " students enrolled)";
                validationErrors.append("- ").append(errorMsg).append("\n");
                System.err.println("Section full: " + errorMsg);
            } catch (Exception e) {
                String errorMsg = course.getCourseSubjectID() + ": " + e.getMessage();
                validationErrors.append("- ").append(errorMsg).append("\n");
                System.err.println("Schedule generation error: " + errorMsg);
                e.printStackTrace(); // Print full stack trace for debugging
            }
        }

        // 7. FINAL SAVE AND REFRESH
        if (successCount > 0) {
            try {
                Enrollment newEnrollment = new Enrollment(Enrollment.generateEnrollmentID(), studentID, courseIDsBuilder.toString(), yearLevel, semesterStr, "ENROLLED");
                newEnrollment.setSectionID(sectionsBuilder.toString());
                enrollments.add(newEnrollment);

                // Save to Student File
                student.getSubjectsEnrolled().addAll(coursesToEnroll.stream().map(CourseSubject::getCourseSubjectID).collect(java.util.stream.Collectors.toList()));
                studentFileSaver.save(studentFilePath, students);
                
                // Save to Enrollment File (use same paths as loading for consistency)
                String savePath = FilePathResolver.resolveWritablePath(new String[]{
                    "ERS-group/src/ers/group/master files/enrollment.txt",
                    "src/ers/group/master files/enrollment.txt",
                    "master files/enrollment.txt",
                    "enrollment.txt"
                });
                enrollmentFileSaver.saveEnrollmentsByStudent(savePath, enrollments);
                
                // Auto-generate marksheet record for grading
                java.util.List<String> enrolledCourseIDs = coursesToEnroll.stream()
                    .map(CourseSubject::getCourseSubjectID)
                    .collect(java.util.stream.Collectors.toList());
                autoGenerateMarksheetRecord(studentID, semesterStr, yearLevel, enrolledCourseIDs);
                
                // Show success message, but include schedule errors if any occurred
                String finalMessage = summary.toString() + "\nSuccessfully Enrolled!";
                if (validationErrors.length() > 0) {
                    finalMessage += "\n\n⚠ Schedule Generation Errors:\n" + validationErrors.toString();
                }
                javax.swing.JOptionPane.showMessageDialog(this, finalMessage, "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
                loadStudentData();
                loadStudentTableData();
                clearCourseForm();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Save Error: " + e.getMessage());
            }
        } // End successCount
    } // End Method
        
    /**
     * Format course display name with status prefix and capacity info.
     * Ensures consistent formatting across all dropdown operations.
     */
    private String formatCourseDisplayName(CourseSubject course, String studentID) {
        String courseID = course.getCourseSubjectID();
        // 1. Determine Status Prefix
        String statusPrefix = "[ ] ";
        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }
        if (student != null) {
            // Check if course is actually PASSED (not just enrolled)
            boolean isPassed = false;
            for (Enrollment e : enrollments) {
                if (e.getStudentID().equals(studentID)) {
                    String status = e.getCourseStatus(courseID);
                    if ("PASSED".equalsIgnoreCase(status)) {
                        isPassed = true;
                        break;
                    }
                }
            }
            
            if (isPassed) {
                statusPrefix = "[/] "; // Already Passed
            } else if (!checkPrerequisites(studentID, course.getPrerequisites())) {
                statusPrefix = "[X] "; // Blocked by Prerequisites
            }
        }
        // 2. Calculate Capacity
        int enrolledCount = 0;
        for (Enrollment e : enrollments) {
            if (e.getCourseID().equals(courseID) && e.getStatus().equals("ENROLLED")) {
                enrolledCount++;
            }
        }
        int maxCapacity = MAX_SECTION_CAPACITY * 3; // Total for 3 sections
        // 3. Return Formatted String with both ID and Name
        String displayText = courseID + " - " + course.getCourseSubjectName();
        if (enrolledCount >= maxCapacity) {
            return statusPrefix + displayText + " [FULL - " + enrolledCount + "/" + maxCapacity + "]";
        } else {
            return statusPrefix + displayText + " (" + enrolledCount + "/" + maxCapacity + ")";
        }
    }
    
    /**
     * Update course dropdowns to filter out already selected courses.
     * Uses formatCourseDisplayName to ensure consistent formatting during filtering.
     */
    private void updateCourseDropdowns() {
        javax.swing.JComboBox<String>[] dropdowns = new javax.swing.JComboBox[]{
            CT_Course1, CT_Course2, CT_Course3, CT_Course4, CT_Course5
        };
        String studentID = CT_StudentID.getText().trim();
        if (studentID.isEmpty()) return;
        // 1. Collect all current selections to hide them from other boxes
        java.util.Set<String> allCurrentSelections = new java.util.HashSet<>();
        for (javax.swing.JComboBox<String> cb : dropdowns) {
            String sel = (String) cb.getSelectedItem();
            if (sel != null && !sel.equals("-- Select Course --")) {
                allCurrentSelections.add(sel);
            }
        }
        // Get student info for year level
        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }
        if (student == null) return;
        String semesterStr = (String) CT_Semester.getSelectedItem();
        boolean isSummer = "Summer".equalsIgnoreCase(semesterStr);
        int semester = semesterStr.equals("1st Semester") ? 1 : 2;
        int year = student.getYearLevel().equals("1st Year") ? 1 : 2;
        
        // For Summer, get courses that need retaking (latest status is FAILED/DROPPED/INC)
        java.util.Set<String> failedCourses = new java.util.HashSet<>();
        if (isSummer) {
            // Track latest status for each course across all enrollments
            java.util.Map<String, String> latestCourseStatus = new java.util.HashMap<>();
            
            for (Enrollment e : enrollments) {
                if (e.getStudentID().equals(studentID)) {
                    java.util.Map<String, String> statuses = e.getCourseStatuses();
                    if (statuses != null) {
                        for (java.util.Map.Entry<String, String> entry : statuses.entrySet()) {
                            String courseID = entry.getKey().toUpperCase();
                            String status = entry.getValue();
                            // Update with latest status (later enrollments override earlier ones)
                            latestCourseStatus.put(courseID, status);
                        }
                    }
                }
            }
            
            // Only show courses where LATEST status is FAILED/DROPPED/INC
            for (java.util.Map.Entry<String, String> entry : latestCourseStatus.entrySet()) {
                String status = entry.getValue();
                if ("FAILED".equalsIgnoreCase(status) || "DROPPED".equalsIgnoreCase(status) || "INC".equalsIgnoreCase(status)) {
                    failedCourses.add(entry.getKey());
                }
            }
        }

        // 2. Refresh each dropdown
        for (javax.swing.JComboBox<String> targetCB : dropdowns) {
            String currentSelection = (String) targetCB.getSelectedItem();
            java.awt.event.ActionListener[] listeners = targetCB.getActionListeners();
            for (java.awt.event.ActionListener l : listeners) targetCB.removeActionListener(l);
            java.util.List<String> newItems = new java.util.ArrayList<>();
            newItems.add("-- Select Course --");
            for (CourseSubject course : availableCourses.values()) {
                boolean shouldShow = false;
                if (isSummer) {
                    shouldShow = failedCourses.contains(course.getCourseSubjectID().toUpperCase());
                } else {
                    shouldShow = (course.getYearLevel() == year && course.getSemester() == semester);
                }
                if (shouldShow) {
                    String formatted = formatCourseDisplayName(course, studentID);
                    if (formatted.equals(currentSelection) || !allCurrentSelections.contains(formatted)) {
                        newItems.add(formatted);
                    }
                }
            }
            targetCB.setModel(new javax.swing.DefaultComboBoxModel<>(newItems.toArray(new String[0])));
            targetCB.setSelectedItem(currentSelection);
            for (java.awt.event.ActionListener l : listeners) targetCB.addActionListener(l);
        }
    }
    
    private CourseSubject findCourseByName(String courseName) {
        // Strip status prefix and capacity indicator from course name
        String cleanName = courseName.trim();
        if (cleanName.startsWith("[") && cleanName.contains("]")) {
            int closeBracket = cleanName.indexOf("]");
            cleanName = cleanName.substring(closeBracket + 1).trim();
        }
        if (cleanName.contains(" [")) {
            cleanName = cleanName.substring(0, cleanName.indexOf(" [")).trim();
        } else if (cleanName.contains(" (")) {
            cleanName = cleanName.substring(0, cleanName.indexOf(" (")).trim();
        }
        String courseID = cleanName;
        if (cleanName.contains(" - ")) {
            courseID = cleanName.substring(0, cleanName.indexOf(" - ")).trim();
        }
        for (CourseSubject course : availableCourses.values()) {
            if (course.getCourseSubjectID().trim().equals(courseID)) {
                return course;
            }
        }
        for (CourseSubject course : availableCourses.values()) {
            if (course.getCourseSubjectName().trim().equals(cleanName)) {
                return course;
            }
        }
        for (CourseSubject course : availableCourses.values()) {
            if (course.getCourseSubjectName().trim().equalsIgnoreCase(cleanName)) {
                return course;
            }
        }
        return null;
    }
    
    private boolean checkPrerequisites(String studentID, ArrayList<CourseSubject> prereqs) {
        for (CourseSubject prereq : prereqs) {
            boolean hasPassed = false;
            for (Enrollment e : enrollments) {
                // Check if student has PASSED this specific course using courseStatuses
                if (e.getStudentID().equals(studentID) && 
                    e.getCourseID().equals(prereq.getCourseSubjectID())) {
                    // Use the new course status tracking
                    String courseStatus = e.getCourseStatus(prereq.getCourseSubjectID());
                    if ("PASSED".equals(courseStatus)) {
                        hasPassed = true;
                        break;
                    }
                }
            }
            if (!hasPassed) {
                return false;
            }
        }
        return true;
    }

    private void CT_SearchActionPerformed(java.awt.event.ActionEvent evt) {                                                
        CT_SearchStudentActionPerformed(evt);
    }

    private void CT_SearchStudentActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String searchName = CT_SearchStudent.getText().trim();
        DefaultTableModel model = (DefaultTableModel) CT_Table.getModel();
        model.setRowCount(0); // Clear the table first
        if (searchName.isEmpty()) {
            // If search field is empty, reload all students
            loadStudentTableData();
            return;
        }
        boolean found = false;
        // Search by ID or Name
        for (Student student : students) {
            if (student.getStudentID().equalsIgnoreCase(searchName) ||
                student.getStudentName().toLowerCase().contains(searchName.toLowerCase())) {
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.getDateOfBirth(),
                    student.getGender(),
                    student.getEmail(),
                    student.getPhoneNumber(),
                    student.getFathersName(),
                    student.getMothersName(),
                    student.getGuardiansPhoneNumber(),
                    student.getAddress()
                });
                found = true;
            }
        }
        if (!found) {
            javax.swing.JOptionPane.showMessageDialog(this, "Student not found!", "Search Result", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void CT_RefreshActionPerformed(java.awt.event.ActionEvent evt) {                                           
        CT_SearchStudent.setText("");
        loadStudentTableData(); // reload all students
    }

    private void CT_LogoutActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            javax.swing.JOptionPane.showMessageDialog(this, "Logged out successfully!", "Logout", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            // Close the parent frame safely
            javax.swing.JFrame frame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    private void CT_ClearActionPerformed(java.awt.event.ActionEvent evt) {
        clearCourseForm();
    }

    private void clearCourseForm() {
        CT_id.setText("");
        CT_StudentID.setText("");
        CT_Semester.setSelectedIndex(0);
        CT_Course1.setSelectedIndex(0);
        CT_Course2.setSelectedIndex(0);
        CT_Course3.setSelectedIndex(0);
        CT_Course4.setSelectedIndex(0);
        CT_Course5.setSelectedIndex(0);
        CT_PrereqStatus1.setText("");
        CT_PrereqStatus2.setText("");
        CT_PrereqStatus3.setText("");
        CT_PrereqStatus4.setText("");
        CT_PrereqStatus5.setText("");
        CT_Table.clearSelection();
    }
    
    private void populateEnrollmentFormFromTable(int selectedRow) {
        try {
            DefaultTableModel model = (DefaultTableModel) CT_Table.getModel();
            // Get student ID from table
            String studentID = model.getValueAt(selectedRow, 0).toString();
            // Find the student
            Student student = null;
            for (Student s : students) {
                if (s.getStudentID().equals(studentID)) {
                    student = s;
                    break;
                }
            }
            if (student == null) return;
            // Populate Student ID
            CT_StudentID.setText(student.getStudentID());
            // Load enrolled courses for this student
            ArrayList<Enrollment> studentEnrollments = new ArrayList<>();
            for (Enrollment e : enrollments) {
                if (e.getStudentID().equals(studentID) && e.getStatus().equals("ENROLLED")) {
                    studentEnrollments.add(e);
                }
            }
            // If student has enrollments, populate the form
            if (!studentEnrollments.isEmpty()) {
                // Get semester from first enrollment (assume all are same semester)
                Enrollment firstEnrollment = studentEnrollments.get(0);
                // Set semester based on student's current enrollment
                CT_Semester.setSelectedIndex(0); // Default to 1st Semester
                // Reset all course dropdowns first
                CT_Course1.setSelectedIndex(0);
                CT_Course2.setSelectedIndex(0);
                CT_Course3.setSelectedIndex(0);
                CT_Course4.setSelectedIndex(0);
                CT_Course5.setSelectedIndex(0);
                
                // Load courses for this student's year/semester
                CT_LoadCoursesActionPerformed(null);
                
                // Populate course dropdowns with enrolled courses
                javax.swing.JComboBox<String>[] courseDropdowns = new javax.swing.JComboBox[]{
                    CT_Course1, CT_Course2, CT_Course3, CT_Course4, CT_Course5
                };
                for (int i = 0; i < Math.min(studentEnrollments.size(), 5); i++) {
                    Enrollment enrollment = studentEnrollments.get(i);
                    CourseSubject course = availableCourses.get(enrollment.getCourseID());
                    if (course != null) {
                        String courseName = course.getCourseSubjectName();
                        // Find and select this course in the dropdown
                        for (int j = 0; j < courseDropdowns[i].getItemCount(); j++) {
                            if (courseDropdowns[i].getItemAt(j).equals(courseName)) {
                                courseDropdowns[i].setSelectedIndex(j);
                                break;
                            }
                        }
                    }
                }
            } else {
                CT_LoadCoursesActionPerformed(null);
            }
        } catch (Exception e) {
            logger.severe("Error populating form from table: " + e.getMessage());
        }
    }

    /**
     * Auto-load courses without showing dialog messages.
     * Called automatically when Student ID or semester changes.
     */
    private void autoLoadCoursesIfValid() {
        String studentID = CT_StudentID.getText().trim();
        String currentSemester = (String) CT_Semester.getSelectedItem(); // This holds "Summer", "1st", etc.

        if (studentID.isEmpty() || studentID.length() < 3) {
            clearCourseDropdowns();
            return;
        }

        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }

        if (student == null) {
            clearCourseDropdowns();
            return;
        }

        // RULE 4.1: Block Graduate Students
        if ("Graduate".equalsIgnoreCase(student.getStatus())) {
            clearCourseDropdowns();
            javax.swing.JOptionPane.showMessageDialog(this, "Graduate students cannot enroll.");
            return;
        }

        // Load courses for the student
        loadCoursesForStudent(student, false); 
    }

    private void loadFilteredCourses(Student student, boolean isSummer) {
        clearCourseDropdowns();
        
        // 1. Refresh prerequisites from file to ensure data is current
        AcademicUtilities.loadPrerequisites();

        // SUMMER AUTO-SUGGESTIONS: Show recommended failed/INC courses
        if (isSummer && student != null) {
            showSummerCourseSuggestions(student.getStudentID());
        }

        for (CourseSubject course : availableCourses.values()) {
            if (isSummer) {
                // SUMMER RULES: Only show courses where LATEST status is FAILED/DROPPED/INC
                // Track latest status across all enrollments
                String latestStatus = null;
                for (Enrollment e : enrollments) {
                    if (e.getStudentID().equals(student.getStudentID())) {
                        String status = e.getCourseStatus(course.getCourseSubjectID());
                        if (status != null && !status.isEmpty()) {
                            // Later enrollments override earlier ones
                            latestStatus = status;
                        }
                    }
                }
                
                // Only add if latest status is FAILED/DROPPED/INC (not PASSED or PENDING)
                if (latestStatus != null && 
                    ("FAILED".equalsIgnoreCase(latestStatus) || 
                    "DROPPED".equalsIgnoreCase(latestStatus) || 
                    "INC".equalsIgnoreCase(latestStatus))) {
                    addItemToAllDropdowns(course.getCourseSubjectName()); // Use Name for the ComboBox
                }

            } else {
                // REGULAR SEMESTER: Show all courses for their current Year Level
                // We show everything so they can see what they are missing!
                int studentYearNum = Integer.parseInt(student.getYearLevel().replaceAll("[^0-9]", ""));
                
                if (course.getYearLevel() == studentYearNum) {
                    addItemToAllDropdowns(course.getCourseSubjectName());
                }
            }
        }
    }

    /**
     * Clear all course dropdowns to default state.
     */
    private void clearCourseDropdowns() {
        String[] empty = new String[] { "-- Select Course --" };
        CT_Course1.setModel(new javax.swing.DefaultComboBoxModel<>(empty));
        CT_Course2.setModel(new javax.swing.DefaultComboBoxModel<>(empty));
        CT_Course3.setModel(new javax.swing.DefaultComboBoxModel<>(empty));
        CT_Course4.setModel(new javax.swing.DefaultComboBoxModel<>(empty));
        CT_Course5.setModel(new javax.swing.DefaultComboBoxModel<>(empty));
        CT_PrereqStatus1.setText("");
        CT_PrereqStatus2.setText("");
        CT_PrereqStatus3.setText("");
        CT_PrereqStatus4.setText("");
        CT_PrereqStatus5.setText("");
    }
    
    /**
     * Shows auto-suggestions for summer remedial courses based on failed/INC status.
     * Displays dialog with course names, IDs, units, and status tags ([F]/[I]/[D]).
     */
    private void showSummerCourseSuggestions(String studentID) {
        java.util.List<String> recommendations = AcademicUtilities.getRecommendedSummerCourses(studentID);
        
        if (recommendations.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No failed or incomplete courses found.\nStudent has no remedial requirements for summer.",
                "Summer Suggestions", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder message = new StringBuilder("Recommended Summer Courses (Failed/Incomplete):\n\n");
        
        for (String courseID : recommendations) {
            CourseSubject course = availableCourses.get(courseID);
            if (course != null) {
                // Determine status tag
                String tag = "[F]"; // Default to Failed
                for (Enrollment e : enrollments) {
                    if (e.getStudentID().equals(studentID)) {
                        String status = e.getCourseStatus(courseID);
                        if ("INC".equalsIgnoreCase(status)) {
                            tag = "[I]";
                        } else if ("DROPPED".equalsIgnoreCase(status)) {
                            tag = "[D]";
                        }
                    }
                }
                message.append(tag).append(" ")
                        .append(course.getCourseSubjectName())
                        .append(" (").append(courseID).append(") - ")
                        .append(course.getUnits()).append(" units\n");
            } else {
                message.append("[?] ").append(courseID).append(" (course data not found)\n");
            }
        }
        
        message.append("\n[F]=Failed | [I]=Incomplete | [D]=Dropped");
        
        javax.swing.JOptionPane.showMessageDialog(this, 
            message.toString(),
            "Summer Course Suggestions", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void CT_LoadCoursesActionPerformed(java.awt.event.ActionEvent evt) {
        String studentID = CT_StudentID.getText().trim();
        String semesterStr = (String) CT_Semester.getSelectedItem();
        
        if (studentID.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please enter a Student ID first!");
            return;
        }

        // 1. REFRESH COURSE DATA FROM FILE
        // This ensures that any newly added courses are available in dropdowns
        loadCourseData();

        // 2. REFRESH ENROLLMENT LIST FROM FILE
        // This ensures that any FAILED or PASSED statuses just saved are actually seen
        try {
            String savePath = FilePathResolver.resolveWritablePath(new String[]{
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            });
            // Use existing loader to update the global 'enrollments' list
            EnrollmentFileLoader loader = new EnrollmentFileLoader();
            java.io.File ef = new java.io.File(savePath);
            loader.load(ef.getAbsolutePath());
            this.enrollments = new ArrayList<>(loader.getAllEnrollments());
        } catch (Exception e) {
            System.err.println("Note: Could not refresh enrollment list: " + e.getMessage());
        }

        // 3. Find the student
        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }

        if (student == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Student ID not found!");
            return;
        }

        // 4. Load logic (now with fresh data)
        loadCoursesForStudent(student, true);
    }
    
    /**
     * Core method to load courses for a student.
     * @param student The student object
     * @param showDialog Whether to show confirmation dialog after loading
     */
        private void loadCoursesForStudent(Student student, boolean showDialog) {
            System.out.println("DEBUG: availableCourses size = " + availableCourses.size());
            System.out.println("DEBUG: Course IDs loaded: " + availableCourses.keySet());
            String studentID = student.getStudentID().trim();
            String semesterStr = (String) CT_Semester.getSelectedItem();
            boolean isSummer = "Summer".equalsIgnoreCase(semesterStr);
            
            int studentYear = Integer.parseInt(student.getYearLevel().replaceAll("[^0-9]", ""));

            java.util.List<String> courseNames = new java.util.ArrayList<>();
            courseNames.add("-- Select Course --");
            
            // Track passed and failed courses
            java.util.Set<String> allPassed = new java.util.HashSet<>();
            java.util.Set<String> currentlyFailed = new java.util.HashSet<>();

            // 1. SCAN TOTAL HISTORY (Added .trim() and .toUpperCase() for safety)
            for (Enrollment e : enrollments) {
                if (e.getStudentID().trim().equalsIgnoreCase(studentID)) {
                    String detailedStatus = e.getDetailedCourseStatuses(); 
                    if (detailedStatus != null && !detailedStatus.isEmpty()) {
                        String[] entries = detailedStatus.split(";");
                        for (String entry : entries) {
                            if (entry.contains(":")) {
                                String[] pair = entry.split(":");
                                // Use trim and uppercase to prevent "cs101" vs "CS101" bugs
                                String cID = pair[0].trim().toUpperCase();
                                String status = pair[1].trim().toUpperCase();
                                
                                if ("PASSED".equals(status)) {
                                    allPassed.add(cID);
                                    currentlyFailed.remove(cID); 
                                } else if ("FAILED".equals(status) || "DROPPED".equals(status)) {
                                    if (!allPassed.contains(cID)) {
                                        currentlyFailed.add(cID);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. APPLY RULES
            for (CourseSubject course : availableCourses.values()) {
                boolean canShow = false;
                String currentCourseID = course.getCourseSubjectID().trim().toUpperCase();

                if (isSummer) {
                    // STRICT SUMMER: If it's not in the failed set, don't show it. Period.
                    if (currentlyFailed.contains(currentCourseID)) {
                canShow = true;
                    }
                } else {
                    // REGULAR SEMESTER: Use Year and Mapped Semester (1 or 2)
                    int mappedSem = semesterStr.contains("1st") ? 1 : 2;
                    
                    if (course.getYearLevel() == studentYear && course.getSemester() == mappedSem) {
                        // Don't show if already passed
                        if (!allPassed.contains(course.getCourseSubjectID())) {
                            // Check Prerequisites
                            if (AcademicUtilities.getMissingPrerequisites(studentID, course.getCourseSubjectID()).isEmpty()) {
                                canShow = true;
                            }
                        }
                    }
                }

                // 3. POPULATE LIST
                if (canShow) {
                    int enrolledCount = 0;
                    for (Enrollment e : enrollments) {
                        if (e.getCourseID().contains(course.getCourseSubjectID()) && "ENROLLED".equals(e.getStatus())) {
                            enrolledCount++;
                        }
                    }
                    int maxCap = MAX_SECTION_CAPACITY * 3;
                    courseNames.add(course.getCourseSubjectID() + " (" + enrolledCount + "/" + maxCap + ")");
                }
            }

            // 4. UPDATE UI
            javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>(courseNames.toArray(new String[0]));
            CT_Course1.setModel(model);
            CT_Course2.setModel(new javax.swing.DefaultComboBoxModel<>(courseNames.toArray(new String[0])));
            CT_Course3.setModel(new javax.swing.DefaultComboBoxModel<>(courseNames.toArray(new String[0])));
            CT_Course4.setModel(new javax.swing.DefaultComboBoxModel<>(courseNames.toArray(new String[0])));
            CT_Course5.setModel(new javax.swing.DefaultComboBoxModel<>(courseNames.toArray(new String[0])));
            
            if (showDialog && courseNames.size() <= 1) {
                String msg = isSummer ? "No failed subjects found to retake." : "No available courses for " + student.getYearLevel() + " " + semesterStr;
                javax.swing.JOptionPane.showMessageDialog(this, msg);
            }
        }


    private void addItemToAllDropdowns(String courseID) {
        CT_Course1.addItem(courseID);
        CT_Course2.addItem(courseID);
        CT_Course3.addItem(courseID);
        CT_Course4.addItem(courseID);
        CT_Course5.addItem(courseID);
    }
    private void updatePrerequisiteStatus(javax.swing.JComboBox<String> courseCombo, javax.swing.JLabel statusLabel) {
        String selectedItem = (String) courseCombo.getSelectedItem();
        
        // 1. Reset if empty
        if (selectedItem == null || selectedItem.equals("-- Select Course --")) {
            statusLabel.setText("");
            return;
        }

        // 2. Extract ID (Takes "CS101" from "CS101 (0/120)")
        String courseID = selectedItem.split(" ")[0];
        String studentID = CT_StudentID.getText().trim();

        if (studentID.isEmpty()) {
            statusLabel.setText("ID?");
            return;
        }

        // 3. Check for duplicates in other slots
        javax.swing.JComboBox[] allCombos = {CT_Course1, CT_Course2, CT_Course3, CT_Course4, CT_Course5};
        for (javax.swing.JComboBox combo : allCombos) {
            if (combo != courseCombo && selectedItem.equals(combo.getSelectedItem())) {
                javax.swing.JOptionPane.showMessageDialog(this, "Subject already selected.");
                resetToPending(courseCombo, statusLabel);
                return;
            }
        }

        // 4. THE FIX: Check history against requirements
        java.util.List<String> missing = AcademicUtilities.getMissingPrerequisites(studentID, courseID);

        if (missing.isEmpty()) {
            // Green Checkmark: Prereqs are "None" OR are found in the PASSED set
            statusLabel.setText("✔");
            statusLabel.setForeground(new java.awt.Color(0, 153, 0));
        } else {
            // Red X: Prereqs were specifically found as FAILED in Index 7
            statusLabel.setText("X");
            statusLabel.setForeground(java.awt.Color.RED);
            
            String error = "You must pass the following first:\n• " + String.join("\n• ", missing);
            javax.swing.JOptionPane.showMessageDialog(this, error, "Prerequisite Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            
            resetToPending(courseCombo, statusLabel);
        }
    }
        
    private javax.swing.JPanel CT_BottomPanel;
    private javax.swing.JLabel CT_COURSE1;
    private javax.swing.JLabel CT_COURSE2;
    private javax.swing.JLabel CT_COURSE3;
    private javax.swing.JLabel CT_COURSE4;
    private javax.swing.JLabel CT_COURSE5;
    private javax.swing.JButton CT_Clear;
    private javax.swing.JComboBox<String> CT_Course1;
    private javax.swing.JComboBox<String> CT_Course2;
    private javax.swing.JComboBox<String> CT_Course3;
    private javax.swing.JComboBox<String> CT_Course4;
    private javax.swing.JComboBox<String> CT_Course5;
    private javax.swing.JLabel CT_ID;
    private javax.swing.JPanel CT_LeftPanel;
    private javax.swing.JButton CT_Logout;
    private javax.swing.JButton CT_Print;
    private javax.swing.JButton CT_Refresh;
    private javax.swing.JPanel CT_RightPanel;
    private javax.swing.JLabel CT_SEARCH_STUDENT;
    private javax.swing.JLabel CT_SEMESTER;
    private javax.swing.JLabel CT_STUDENT_ID;
    private javax.swing.JButton CT_Save;
    private javax.swing.JButton CT_Search;
    private javax.swing.JTextField CT_SearchStudent;
    private javax.swing.JPanel CT_SearchStudentPanel;
    private javax.swing.JComboBox<String> CT_Semester;
    private javax.swing.JTextField CT_StudentID;
    private javax.swing.JTable CT_Table;
    private javax.swing.JScrollPane CT_TableScrollPane;
    private javax.swing.JButton CT_Update;
    private javax.swing.JTextField CT_id;
    private javax.swing.JButton CT_LoadCourses;
    private javax.swing.JLabel CT_PrereqStatus1;
    private javax.swing.JLabel CT_PrereqStatus2;
    private javax.swing.JLabel CT_PrereqStatus3;
    private javax.swing.JLabel CT_PrereqStatus4;
    private javax.swing.JLabel CT_PrereqStatus5;

    /**
     * Populate the enrollment form from a selected table row.
     * Called when user clicks on a row in the student table.
     */
    public void populateFormFromTable(int rowIndex) {
        try {
            DefaultTableModel model = (DefaultTableModel) CT_Table.getModel();
            if (rowIndex < 0 || rowIndex >= model.getRowCount()) {
                return;
            }
            String studentID = model.getValueAt(rowIndex, 0).toString();
            CT_StudentID.setText(studentID);
            CT_LoadCoursesActionPerformed(null);
            logger.info("Auto-populated form for student: " + studentID);
        } catch (Exception e) {
            logger.warning("Error populating form from table: " + e.getMessage());
        }
    }

    /**
     * Auto-generates a marksheet record with pending grades when a student enrolls.
     * Creates a placeholder record with all enrolled courses and "PENDING" scores.
     */
    private void autoGenerateMarksheetRecord(String studentID, String semester, String yearLevel, java.util.List<String> enrolledCourses) {
        try {
            String filePath = FilePathResolver.resolveMarksheetFilePath();
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            // Ensure parent directory exists
            if (!java.nio.file.Files.exists(path.getParent())) {
                java.nio.file.Files.createDirectories(path.getParent());
            }
            // Create file if it doesn't exist
            if (!java.nio.file.Files.exists(path)) {
                java.nio.file.Files.createFile(path);
            }
            // Read existing records
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
            
            // Compress semester for file storage
            String compressedSemester = compressSemester(semester);
            
            // Check if record already exists for this student/semester/year combination
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4 && 
                    parts[1].equals(studentID) && 
                    parts[2].equals(compressedSemester) && 
                    parts[3].equals(yearLevel)) {
                    logger.info("Marksheet record already exists for " + studentID + " - " + semester + " - " + yearLevel);
                    return; // Record already exists, don't duplicate
                }
            }
            // Get next MRK ID
            String mrkID = getNextMRK(lines);
            // Ensure we have exactly 5 courses (pad with "NONE" if necessary)
            java.util.List<String> courseList = new java.util.ArrayList<>(enrolledCourses);
            while (courseList.size() < 5) {
                courseList.add("NONE");
            }
            // Truncate if more than 5
            if (courseList.size() > 5) {
                courseList = courseList.subList(0, 5);
            }
            // Create record: MRK-ID,StudentID,Semester,YearLevel,C1,S1,C2,S2,C3,S3,C4,S4,C5,S5,GPA
            StringBuilder record = new StringBuilder(mrkID)
                .append(",").append(studentID)
                .append(",").append(compressedSemester)
                .append(",").append(yearLevel);
            for (String course : courseList) {
                record.append(",").append(course)
                      .append(",").append("PENDING"); // Placeholder score
            }
            record.append(",0.00"); // GPA placeholder
            // Append to file
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                    new java.io.FileWriter(filePath, true))) {
                writer.write(record.toString());
                writer.newLine();
            }
            logger.info("Auto-generated marksheet record: " + mrkID + " for student " + studentID + 
                    " (" + semester + ", " + yearLevel + ")");
        } catch (Exception e) {
            logger.warning("Failed to auto-generate marksheet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Convert full semester format to abbreviated format for file storage
     * 1st Semester -> 1, 2nd Semester -> 2, Summer -> 3
     */
    private String compressSemester(String full) {
        switch (full) {
            case "1st Semester": return "1";
            case "2nd Semester": return "2";
            case "Summer": return "3";
            default: return full; // Already abbreviated or unknown
        }
    }

    /**
     * Generates the next MRK ID by finding the highest existing number.
     */
    private String getNextMRK(java.util.List<String> existingLines) {
        int maxNum = 0;
        for (String line : existingLines) {
            if (line.trim().isEmpty()) continue;
            if (line.startsWith("MRK-")) {
                String[] parts = line.split(",");
                try {
                    int num = Integer.parseInt(parts[0].substring(4));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("MRK-%03d", maxNum + 1);
    }

    /**
     * Gets all enrolled courses for a student in a specific semester and year level.
     */
    private java.util.List<String> getEnrolledCoursesForStudent(String studentID, String semesterStr, String yearLevel) {
        java.util.List<String> courses = new java.util.ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentID().equals(studentID) && 
                enrollment.getSemester().equals(semesterStr) &&
                enrollment.getYearLevel().equals(yearLevel) &&  // NOW checks year level too!
                "ENROLLED".equals(enrollment.getStatus())) {
                courses.add(enrollment.getCourseID());
            }
        }
        
        return courses;
    }

    
    /**
     * Set the semester dropdown to the current active semester from academic calendar
     */
    private void setCurrentActiveSemester() {
        try {
            String currentSemester = AcademicUtilities.getCurrentSemester();
            if (currentSemester != null && !currentSemester.isEmpty()) {
                CT_Semester.setSelectedItem(currentSemester);
                logger.info("Set active semester to: " + currentSemester);
            }
        } catch (Exception e) {
            logger.warning("Could not set current semester: " + e.getMessage());
        }
    }
    
    /**
     * Public method to refresh semester selection to current active semester.
     * Called when switching tabs or after advancing semester.
     */
    public void refreshSemesterSelection() {
        setCurrentActiveSemester();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Course Tab Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800); // optional, you can use pack() instead
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new CourseTab());
            frame.setVisible(true);
        });
    }
}