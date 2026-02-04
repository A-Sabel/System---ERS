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
    }

    /**
     * Public method to refresh the student list from file.
     * Called when switching to Course tab after adding new students.
     */
    public void refreshStudentList() {
        loadStudentData();
        loadStudentTableData();
        logger.info("Student list refreshed");
    }
    
    /**
     * Auto-populate student ID and trigger course loading.
     * Called from SessionManager when coming from Save & Enroll.
     */
    public void autoPopulateStudent(String studentID) {
        CT_StudentID.setText(studentID);
        logger.info("Auto-populated student: " + studentID);
        
        // Auto-trigger Load Courses
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
            students = new ArrayList<>(allStudents);
            logger.info("Loaded " + students.size() + " students from file");
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
            
            String filePath = null;
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    filePath = path;
                    break;
                }
            }
            
            if (filePath != null) {
                courseLoader.load(filePath);
                availableCourses = courseLoader.getSubjectMap();
                logger.info("Loaded " + availableCourses.size() + " courses from file");
            }
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
            
            String filePath = null;
            for (String path : possiblePaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    filePath = path;
                    break;
                }
            }
            
            if (filePath != null) {
                enrollmentLoader.load(filePath);
                Collection<Enrollment> allEnrollments = enrollmentLoader.getAllEnrollments();
                enrollments = new ArrayList<>(allEnrollments);
                logger.info("Loaded " + enrollments.size() + " enrollments from file");
            } else {
                enrollments = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.severe("Error loading enrollment data: " + e.getMessage());
            enrollments = new ArrayList<>();
        }
    }

    private void loadStudentTableData() {
        // Get the model from CT_Table directly
        DefaultTableModel model = (DefaultTableModel) CT_Table.getModel();
        
        // Clear existing rows
        model.setRowCount(0);
        
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
        CT_Semester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Semester", "2nd Semester" }));

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
        // TODO add your handling code here:
    }                                     

    private void CT_StudentIDActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }

    private void CT_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        // Validate required fields
        String studentID = CT_StudentID.getText().trim();
        String semesterStr = (String) CT_Semester.getSelectedItem();
        
        if (studentID.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please enter a Student ID!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Find the student
        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }
        
        if (student == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student ID not found!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Parse semester
        int semester = semesterStr.equals("1st Semester") ? 1 : 2;
        String yearLevel = student.getYearLevel();
        int year = yearLevel.equals("1st Year") ? 1 : 2;
        
        // Collect selected courses
        String[] selectedCourseNames = {
            (String) CT_Course1.getSelectedItem(),
            (String) CT_Course2.getSelectedItem(),
            (String) CT_Course3.getSelectedItem(),
            (String) CT_Course4.getSelectedItem(),
            (String) CT_Course5.getSelectedItem()
        };
        
        ArrayList<CourseSubject> coursesToEnroll = new ArrayList<>();
        StringBuilder validationErrors = new StringBuilder();
        StringBuilder enrollmentSummary = new StringBuilder();
        enrollmentSummary.append("Enrollment Summary:\n\n");
        
        // Validate and collect courses
        for (String courseName : selectedCourseNames) {
            if (courseName == null || courseName.isEmpty()) continue;
            
            // Find course by name
            CourseSubject course = findCourseByName(courseName);
            if (course != null) {
                // Check if already enrolled
                boolean alreadyEnrolled = false;
                for (Enrollment e : enrollments) {
                    if (e.getStudentID().equals(studentID) && 
                        e.getCourseID().equals(course.getCourseSubjectID()) &&
                        e.getStatus().equals("ENROLLED")) {
                        alreadyEnrolled = true;
                        break;
                    }
                }
                
                if (alreadyEnrolled) {
                    validationErrors.append("- ").append(courseName).append(": Already enrolled\n");
                    continue;
                }
                
                // Check prerequisites
                ArrayList<CourseSubject> prereqs = course.getPrerequisites();
                if (!prereqs.isEmpty()) {
                    boolean hasAllPrereqs = checkPrerequisites(studentID, prereqs);
                    if (!hasAllPrereqs) {
                        validationErrors.append("- ").append(courseName).append(": Missing prerequisites\n");
                        continue;
                    }
                }
                
                coursesToEnroll.add(course);
            }
        }
        
        if (validationErrors.length() > 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Enrollment validation errors:\n\n" + validationErrors.toString(), 
                "Validation Errors", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            
            if (coursesToEnroll.isEmpty()) {
                return;
            }
        }
        
        // Enroll in courses
        int successCount = 0;
        for (CourseSubject course : coursesToEnroll) {
            try {
                // Create enrollment
                String enrollmentID = Enrollment.generateEnrollmentID();
                // Assign section
                String sectionID = Schedule.assignSection(course.getCourseSubjectID(), studentID, MAX_SECTION_CAPACITY);
                // Assign schedule to section if not already assigned
                Schedule.assignScheduleToSection(sectionID, course.getCourseSubjectID(), course.isLabRoom());
                // Create enrollment record
                Enrollment newEnrollment = new Enrollment(
                    enrollmentID, 
                    studentID, 
                    course.getCourseSubjectID(), 
                    yearLevel, 
                    String.valueOf(semester), 
                    "ENROLLED"
                );
                newEnrollment.setSectionID(sectionID);
                
                enrollments.add(newEnrollment);
                
                enrollmentSummary.append(String.format("%s - %s\n  Section: %s\n  Status: ENROLLED\n\n", 
                    course.getCourseSubjectID(), 
                    course.getCourseSubjectName(), 
                    sectionID));
                
                successCount++;
                
            } catch (Schedule.SectionFullException e) {
                validationErrors.append("- ").append(course.getCourseSubjectName())
                    .append(": All sections are full (max 3 sections)\n");
            } catch (Exception e) {
                validationErrors.append("- ").append(course.getCourseSubjectName())
                    .append(": ").append(e.getMessage()).append("\n");
            }
        }
        
        // Update student's subjectsEnrolled list
        if (successCount > 0) {
            java.util.ArrayList<String> currentEnrollments = student.getSubjectsEnrolled();
            if (currentEnrollments == null) {
                currentEnrollments = new java.util.ArrayList<>();
            }
            
            for (CourseSubject course : coursesToEnroll) {
                String courseID = course.getCourseSubjectID();
                if (!currentEnrollments.contains(courseID)) {
                    currentEnrollments.add(courseID);
                }
            }
            
            // Save updated student data
            try {
                studentFileSaver.save(studentFilePath, students);
                logger.info("Updated student enrollments for " + studentID);
            } catch (Exception e) {
                logger.severe("Error updating student enrollments: " + e.getMessage());
            }
        }
        
        // Save enrollments
        try {
            String[] paths = {
                "ERS-group/src/ers/group/master files/enrollment.txt",
                "src/ers/group/master files/enrollment.txt",
                "master files/enrollment.txt",
                "enrollment.txt"
            };
            
            String savePath = null;
            for (String path : paths) {
                java.io.File dir = new java.io.File(path).getParentFile();
                if (dir != null && (dir.exists() || dir.mkdirs())) {
                    savePath = path;
                    break;
                }
            }
            
            if (savePath != null) {
                enrollmentFileSaver.save(savePath, enrollments);
            }
        } catch (Exception e) {
            logger.severe("Error saving enrollments: " + e.getMessage());
        }
        
        // Show result
        if (successCount > 0) {
            enrollmentSummary.append("Successfully enrolled in " + successCount + " course(s)!");
            loadEnrollmentData();
            loadStudentData();
            loadStudentTableData();
            CT_LoadCoursesActionPerformed(null);
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                enrollmentSummary.toString(), 
                "Enrollment Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            clearCourseForm();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No courses were enrolled.\n\n" + validationErrors.toString(), 
                "Enrollment Failed", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
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
            ArrayList<String> enrolled = student.getSubjectsEnrolled();
            if (enrolled != null && enrolled.contains(courseID)) {
                statusPrefix = "[✓] "; // Already Enrolled/Passed
            } else if (!checkPrerequisites(studentID, course.getPrerequisites())) {
                statusPrefix = "[✗] "; // Blocked by Prerequisites
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

        // 3. Return Formatted String
        if (enrolledCount >= maxCapacity) {
            return statusPrefix + course.getCourseSubjectName() + " [FULL - " + enrolledCount + "/" + maxCapacity + "]";
        } else {
            return statusPrefix + course.getCourseSubjectName() + " (" + enrolledCount + "/" + maxCapacity + ")";
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
        int semester = semesterStr.equals("1st Semester") ? 1 : 2;
        int year = student.getYearLevel().equals("1st Year") ? 1 : 2;

        // 2. Refresh each dropdown
        for (javax.swing.JComboBox<String> targetCB : dropdowns) {
            String currentSelection = (String) targetCB.getSelectedItem();
            
            // Remove listeners to prevent infinite loop during model update
            java.awt.event.ActionListener[] listeners = targetCB.getActionListeners();
            for (java.awt.event.ActionListener l : listeners) targetCB.removeActionListener(l);

            java.util.List<String> newItems = new java.util.ArrayList<>();
            newItems.add("-- Select Course --");

            // Rebuild list
            for (CourseSubject course : availableCourses.values()) {
                if (course.getYearLevel() == year && course.getSemester() == semester) {
                    String formatted = formatCourseDisplayName(course, studentID);
                    
                    // Add to list if it's the current selection of THIS box OR not selected anywhere else
                    if (formatted.equals(currentSelection) || !allCurrentSelections.contains(formatted)) {
                        newItems.add(formatted);
                    }
                }
            }

            targetCB.setModel(new javax.swing.DefaultComboBoxModel<>(newItems.toArray(new String[0])));
            targetCB.setSelectedItem(currentSelection);

            // Re-add listeners
            for (java.awt.event.ActionListener l : listeners) targetCB.addActionListener(l);
        }
    }
    
    private CourseSubject findCourseByName(String courseName) {
        // Strip status prefix and capacity indicator from course name
        // e.g., "[✓] Course Name (5/15)" -> "Course Name"
        String cleanName = courseName;
        
        // Remove status prefix ([✓], [✗], [ ])
        if (cleanName.startsWith("[✓] ")) {
            cleanName = cleanName.substring(4);
        } else if (cleanName.startsWith("[✗] ")) {
            cleanName = cleanName.substring(4);
        } else if (cleanName.startsWith("[ ] ")) {
            cleanName = cleanName.substring(4);
        }
        
        // Strip capacity indicator (e.g., "Course Name [FULL - 15/15]" or "Course Name (5/15)")
        if (cleanName.contains(" [")) {
            cleanName = cleanName.substring(0, cleanName.indexOf(" ["));
        } else if (cleanName.contains(" (")) {
            cleanName = cleanName.substring(0, cleanName.indexOf(" ("));
        }
        
        for (CourseSubject course : availableCourses.values()) {
            if (course.getCourseSubjectName().equals(cleanName)) {
                return course;
            }
        }
        return null;
    }
    
    private boolean checkPrerequisites(String studentID, ArrayList<CourseSubject> prereqs) {
        for (CourseSubject prereq : prereqs) {
            boolean hasPassed = false;
            for (Enrollment e : enrollments) {
                if (e.getStudentID().equals(studentID) && 
                    e.getCourseID().equals(prereq.getCourseSubjectID()) &&
                    (e.getStatus().equals("ENROLLED") || e.getStatus().equals("PASSED"))) {
                    hasPassed = true;
                    break;
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
                    student.getAge(),
                    student.getDateOfBirth(),
                    student.getYearLevel(),
                    student.getStudentType(),
                    student.getGwa(),
                    student.getEmail(),
                    student.getPhoneNumber()
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
                // You can determine this from the course or student's year level
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
                // No enrollments yet, just load available courses
                CT_LoadCoursesActionPerformed(null);
            }
            
        } catch (Exception e) {
            logger.severe("Error populating form from table: " + e.getMessage());
        }
    }

    private void CT_LoadCoursesActionPerformed(java.awt.event.ActionEvent evt) {
        String studentID = CT_StudentID.getText().trim();
        String semesterStr = (String) CT_Semester.getSelectedItem();
        
        if (studentID.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please enter a Student ID first!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Find the student
        Student student = null;
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                student = s;
                break;
            }
        }
        
        if (student == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student ID not found!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Parse semester and year level
        int semester = semesterStr.equals("1st Semester") ? 1 : 2;
        String yearLevel = student.getYearLevel();
        int year = yearLevel.equals("1st Year") ? 1 : 2;
        
        // Load courses for the student's year and semester with capacity and status info
        java.util.List<String> courseNames = new java.util.ArrayList<>();
        courseNames.add("-- Select Course --");
        
        java.util.ArrayList<String> enrolledSubjects = student.getSubjectsEnrolled();
        
        for (CourseSubject course : availableCourses.values()) {
            if (course.getYearLevel() == year && course.getSemester() == semester) {
                String courseID = course.getCourseSubjectID();
                
                // Determine status
                String statusPrefix = "[A] ";
                if (enrolledSubjects != null && enrolledSubjects.contains(courseID)) {
                    statusPrefix = "[/] ";
                } else {
                    // Check prerequisites
                    java.util.ArrayList<CourseSubject> prereqs = course.getPrerequisites();
                    if (!prereqs.isEmpty()) {
                        boolean hasAllPrereqs = checkPrerequisites(studentID, prereqs);
                        if (!hasAllPrereqs) {
                            statusPrefix = "[X] ";
                        }
                    }
                }
                
                // Calculate current enrollment for this course
                int enrolledCount = 0;
                for (Enrollment e : enrollments) {
                    if (e.getCourseID().equals(courseID) && 
                        e.getStatus().equals("ENROLLED")) {
                        enrolledCount++;
                    }
                }
                
                // Max capacity: 3 sections * 5 students = 15
                int maxCapacity = MAX_SECTION_CAPACITY * 3;
                
                // Format course name with status and capacity indicator
                String displayName;
                if (enrolledCount >= maxCapacity) {
                    displayName = statusPrefix + course.getCourseSubjectName() + " [FULL - " + enrolledCount + "/" + maxCapacity + "]";
                } else {
                    displayName = statusPrefix + course.getCourseSubjectName() + " (" + enrolledCount + "/" + maxCapacity + ")";
                }
                
                courseNames.add(displayName);
            }
        }
        
        // Update all course dropdowns
        String[] coursesArray = courseNames.toArray(new String[0]);
        CT_Course1.setModel(new javax.swing.DefaultComboBoxModel<>(coursesArray));
        CT_Course2.setModel(new javax.swing.DefaultComboBoxModel<>(coursesArray));
        CT_Course3.setModel(new javax.swing.DefaultComboBoxModel<>(coursesArray));
        CT_Course4.setModel(new javax.swing.DefaultComboBoxModel<>(coursesArray));
        CT_Course5.setModel(new javax.swing.DefaultComboBoxModel<>(coursesArray));
        
        // Clear prerequisite status
        CT_PrereqStatus1.setText("");
        CT_PrereqStatus2.setText("");
        CT_PrereqStatus3.setText("");
        CT_PrereqStatus4.setText("");
        CT_PrereqStatus5.setText("");
        
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Loaded " + (courseNames.size() - 1) + " available courses for " + yearLevel + ", Semester " + semester, 
            "Courses Loaded", 
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void updatePrerequisiteStatus(javax.swing.JComboBox<String> courseCombo, javax.swing.JLabel statusLabel) {
        String selectedCourse = (String) courseCombo.getSelectedItem();
        
        if (selectedCourse == null || selectedCourse.equals("-- Select Course --")) {
            statusLabel.setText("");
            return;
        }
        
        // Find course
        CourseSubject course = findCourseByName(selectedCourse);
        if (course == null) {
            statusLabel.setText("");
            return;
        }
        
        // Check prerequisites
        ArrayList<CourseSubject> prereqs = course.getPrerequisites();
        if (prereqs.isEmpty()) {
            statusLabel.setText("✓");
            statusLabel.setForeground(new java.awt.Color(0, 255, 0));
            statusLabel.setToolTipText("No prerequisites required");
            return;
        }
        
        // Check if student has met prerequisites
        String studentID = CT_StudentID.getText().trim();
        if (studentID.isEmpty()) {
            statusLabel.setText("?");
            statusLabel.setForeground(new java.awt.Color(255, 255, 0));
            statusLabel.setToolTipText("Enter Student ID to check");
            return;
        }
        
        boolean hasMet = checkPrerequisites(studentID, prereqs);
        if (hasMet) {
            statusLabel.setText("✓");
            statusLabel.setForeground(new java.awt.Color(0, 255, 0));
            statusLabel.setToolTipText("Prerequisites met");
        } else {
            statusLabel.setText("✗");
            statusLabel.setForeground(new java.awt.Color(255, 0, 0));
            StringBuilder prereqNames = new StringBuilder("Missing: ");
            for (int i = 0; i < prereqs.size(); i++) {
                prereqNames.append(prereqs.get(i).getCourseSubjectID());
                if (i < prereqs.size() - 1) prereqNames.append(", ");
            }
            statusLabel.setToolTipText(prereqNames.toString());
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
            
            // Get student ID from the first column
            String studentID = model.getValueAt(rowIndex, 0).toString();
            
            // Auto-populate the student ID field
            CT_StudentID.setText(studentID);
            
            // Auto-trigger Load Courses
            CT_LoadCoursesActionPerformed(null);
            
            logger.info("Auto-populated form for student: " + studentID);
            
        } catch (Exception e) {
            logger.warning("Error populating form from table: " + e.getMessage());
        }
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