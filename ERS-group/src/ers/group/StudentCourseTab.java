package ers.group;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Component;

/**
 *
 * @author Eli
 */
public class StudentCourseTab extends javax.swing.JFrame {
    
    // Session Manager - holds current student for auto-population in Course Tab
    public static class SessionManager {
        private static String currentStudentID = null;
        private static String currentStudentName = null;
        
        public static void setCurrentStudent(String studentID, String studentName) {
            currentStudentID = studentID;
            currentStudentName = studentName;
        }
        
        public static String getCurrentStudentID() {
            return currentStudentID;
        }
        
        public static String getCurrentStudentName() {
            return currentStudentName;
        }
        
        public static void clearSession() {
            currentStudentID = null;
            currentStudentName = null;
        }
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentCourseTab.class.getName());
    private ArrayList<Student> students;
    private StudentFileLoader studentFileLoader;
    private StudentFileSaver studentFileSaver;

    private String studentFilePath; // Store the actual file path found during loading
    private CourseSubjectFileLoader courseLoader; // For loading suggested courses
    private CourseTab courseTab; // Reference to CourseTab for refreshing
    private boolean isInitialized = false; // Flag to prevent premature tab change events

    
    // Schedule Data
    private ArrayList<Schedule> schedules;
    private Map<String, ArrayList<String>> studentCourses;
    
    // Schedule Tab Components
    private javax.swing.JPanel SCH_SearchPanel;
    private javax.swing.JPanel SCH_FiltersPanel;
    private javax.swing.JLabel SCH_SemesterIDLabel;
    private javax.swing.JTextField SCH_StudentSearchField;
    private javax.swing.JButton SCH_SearchStudentButton;
    private javax.swing.JTextField SCH_SemesterSearchField;
    private javax.swing.JLabel SCH_StudentIDLabel;
    private javax.swing.JButton SCH_SearchSemesterButton;
    private javax.swing.JPanel SCH_GWAPanel;
    private javax.swing.JLabel SCH_GWALabel;
    private javax.swing.JPanel SCH_MonthYearPanel;
    private javax.swing.JComboBox<String> SCH_MonthComboBox;
    private javax.swing.JSpinner SCH_YearSpinner;
    private javax.swing.JLabel SCH_MonthYearDisplayLabel;
    private javax.swing.JButton SCH_RefreshButton;
    private javax.swing.JScrollPane SCH_TableScrollPane;
    private javax.swing.JTable SCH_Table;
    
    // File paths
    private static final String STUDENT_FILE = "src/ers/group/master files/student.txt";


    /**
     * Creates new form Student
     */
    public StudentCourseTab() {
        initComponents();

        students = new ArrayList<>();
        studentFileSaver = new StudentFileSaver();
        courseLoader = new CourseSubjectFileLoader();
        loadCourseData();
        loadStudentData();
        loadStudentTableData();
    }
    
    /**
     * Helper method to find the student file path.
     * Returns the resolved path if found, or the first possible path if not found.
     */
    private String getStudentFilePath() {
        if (studentFilePath != null) {
            return studentFilePath;
        }
        
        // Try to find the file using the same logic as loadStudentData
        String[] possiblePaths = {
            "ERS-group/src/ers/group/master files/student.txt",
            "src/ers/group/master files/student.txt",
            "master files/student.txt",
            "student.txt",
            "ERS-group/student.txt",
            "../student.txt"
        };
        
        for (String path : possiblePaths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                return path;
            }
        }
        
        // If not found, return the most likely path (first option)
        return possiblePaths[0];
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
        // Get the model from ST_Table directly
        DefaultTableModel model = (DefaultTableModel) ST_Table.getModel();
        
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
                stud.getSection(),
                stud.getStudentType(),
                stud.getGwa(),
                stud.getEmail(),
                stud.getPhoneNumber()
            });
        }
        
        // Refresh display
        if (ST_TableScrollPane.getParent() != null) {
            ST_TableScrollPane.revalidate();
            ST_TableScrollPane.repaint();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        MainScrollPane = new javax.swing.JScrollPane();
        MainPanel = new javax.swing.JPanel();
        SMSPanel = new javax.swing.JPanel();
        SMS = new javax.swing.JLabel();
        MainTabPanel = new javax.swing.JTabbedPane();
        StudentTab = new javax.swing.JPanel();
        ST_LeftPanel = new javax.swing.JPanel();
        ST_StudentID = new javax.swing.JTextField();
        ST_StudentName = new javax.swing.JTextField();
        ST_Email = new javax.swing.JTextField();
        ST_PhoneNumber = new javax.swing.JTextField();
        ST_FathersName = new javax.swing.JTextField();
        ST_MothersName = new javax.swing.JTextField();
        ST_GuardiansPhoneNumber = new javax.swing.JTextField();
        ST_Address = new javax.swing.JTextField();
        ST_Gender = new javax.swing.JComboBox<>();
        ST_YearLevel = new javax.swing.JComboBox<>();
        ST_CurrentSemester = new javax.swing.JComboBox<>();
        ST_StudentType = new javax.swing.JComboBox<>();
        ST_Section = new javax.swing.JTextField();
        ST_GWA = new javax.swing.JTextField();
        ST_SuggestedCoursesPanel = new javax.swing.JPanel();
        ST_SuggestedCoursesScrollPane = new javax.swing.JScrollPane();
        ST_SuggestedCoursesList = new javax.swing.JList<>();
        ST_SaveAndEnroll = new javax.swing.JButton();
        ST_STUDENT_ID = new javax.swing.JLabel();
        ST_STUDENT_NAME = new javax.swing.JLabel();
        ST_BIRTHDAY = new javax.swing.JLabel();
        ST_GENDER = new javax.swing.JLabel();
        ST_EMAIL = new javax.swing.JLabel();
        ST_PHONE_NUM = new javax.swing.JLabel();
        ST_FATHERS_NAME = new javax.swing.JLabel();
        ST_MOTHERS_NAME = new javax.swing.JLabel();
        ST_GUARDIANS_PHONE_NUM = new javax.swing.JLabel();
        ST_ADDRESS = new javax.swing.JLabel();
        ST_YEAR_LEVEL = new javax.swing.JLabel();
        ST_CURRENT_SEMESTER = new javax.swing.JLabel();
        ST_STUDENT_TYPE = new javax.swing.JLabel();
        ST_SECTION = new javax.swing.JLabel();
        ST_GWA_LABEL = new javax.swing.JLabel();
        ST_ACADEMIC_INFO = new javax.swing.JLabel();

        ST_DateOfBirth = new javax.swing.JSpinner(
            new javax.swing.SpinnerDateModel(
                new java.util.Date(),
                null,
                null,
                java.util.Calendar.DAY_OF_MONTH
            )
        );

        javax.swing.JSpinner.DateEditor dobEditor =
            new javax.swing.JSpinner.DateEditor(ST_DateOfBirth, "yyyy-MM-dd");
        ST_DateOfBirth.setEditor(dobEditor);
        
        ST_RightPanel = new javax.swing.JPanel();
        ST_SearchStudentPanel = new javax.swing.JPanel();
        ST_SEARCH_STUDENT = new javax.swing.JLabel();
        ST_Search = new javax.swing.JButton();
        ST_Refresh = new javax.swing.JButton();
        ST_SearchStudent = new javax.swing.JTextField();
        ST_TableScrollPane = new javax.swing.JScrollPane();
        ST_Table = new javax.swing.JTable();
        ST_BottomPanel = new javax.swing.JPanel();
        ST_AddNew = new javax.swing.JButton();
        ST_Update = new javax.swing.JButton();
        ST_Delete = new javax.swing.JButton();
        ST_Clear = new javax.swing.JButton();
        ST_Logout = new javax.swing.JButton();
        CourseTab = new javax.swing.JPanel();
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
        ScoreTab = new javax.swing.JPanel();
        MarkSheetTab = new javax.swing.JPanel();
        ScheduleTab = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MainPanel.setBackground(new java.awt.Color(31, 58, 95));
        MainPanel.setPreferredSize(new java.awt.Dimension(1350, 650));

        SMSPanel.setBackground(new java.awt.Color(0, 30, 58));

        SMS.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        SMS.setForeground(new java.awt.Color(255, 255, 255));
        SMS.setText("Student Management System");

        javax.swing.GroupLayout SMSPanelLayout = new javax.swing.GroupLayout(SMSPanel);
        SMSPanel.setLayout(SMSPanelLayout);
        SMSPanelLayout.setHorizontalGroup(
            SMSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SMSPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(SMS, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SMSPanelLayout.setVerticalGroup(
            SMSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SMSPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SMS, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        MainTabPanel.setBackground(new java.awt.Color(73, 118, 159));
        MainTabPanel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N

        StudentTab.setBackground(new java.awt.Color(31, 58, 95));

        ST_LeftPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_LeftPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        ST_LeftPanel.setPreferredSize(new java.awt.Dimension(460, 480));

        ST_StudentID.setBackground(new java.awt.Color(240, 240, 240));
        ST_StudentID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_StudentID.setEditable(false);
        ST_StudentID.addActionListener(this::ST_StudentIDActionPerformed);

        ST_StudentName.setBackground(new java.awt.Color(146, 190, 219));
        ST_StudentName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_StudentName.addActionListener(this::ST_StudentNameActionPerformed);

        ST_Email.setBackground(new java.awt.Color(146, 190, 219));
        ST_Email.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ST_PhoneNumber.setBackground(new java.awt.Color(146, 190, 219));
        ST_PhoneNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ST_FathersName.setBackground(new java.awt.Color(146, 190, 219));
        ST_FathersName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ST_MothersName.setBackground(new java.awt.Color(146, 190, 219));
        ST_MothersName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ST_GuardiansPhoneNumber.setBackground(new java.awt.Color(146, 190, 219));
        ST_GuardiansPhoneNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ST_Address.setBackground(new java.awt.Color(146, 190, 219));
        ST_Address.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ST_Gender.setBackground(new java.awt.Color(146, 190, 219));
        ST_Gender.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_Gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));

        // Academic Information Fields
        ST_YearLevel.setBackground(new java.awt.Color(146, 190, 219));
        ST_YearLevel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_YearLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Year", "2nd Year" }));

        ST_CurrentSemester.setBackground(new java.awt.Color(146, 190, 219));
        ST_CurrentSemester.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_CurrentSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        ST_StudentType.setBackground(new java.awt.Color(146, 190, 219));
        ST_StudentType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_StudentType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Regular", "Irregular" }));

        ST_Section.setBackground(new java.awt.Color(240, 240, 240));
        ST_Section.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_Section.setEditable(false);
        ST_Section.setText("(Auto-assigned)");

        ST_GWA.setBackground(new java.awt.Color(240, 240, 240));
        ST_GWA.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ST_GWA.setEditable(false);
        ST_GWA.setText("0.00");

        // Suggested Courses Preview Panel
        ST_SuggestedCoursesPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_SuggestedCoursesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 2),
            "Suggested Courses",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new java.awt.Font("Segoe UI", 1, 14),
            new java.awt.Color(189, 216, 233)
        ));

        ST_SuggestedCoursesList.setBackground(new java.awt.Color(146, 190, 219));
        ST_SuggestedCoursesList.setFont(new java.awt.Font("Segoe UI", 0, 12));
        ST_SuggestedCoursesList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Select Year Level and Semester to view suggested courses" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ST_SuggestedCoursesScrollPane.setViewportView(ST_SuggestedCoursesList);

        ST_SaveAndEnroll.setBackground(new java.awt.Color(73, 118, 159));
        ST_SaveAndEnroll.setFont(new java.awt.Font("Segoe UI", 1, 16));
        ST_SaveAndEnroll.setForeground(new java.awt.Color(255, 255, 255));
        ST_SaveAndEnroll.setText("Save & Enroll →");
        ST_SaveAndEnroll.addActionListener(this::ST_SaveAndEnrollActionPerformed);

        javax.swing.GroupLayout ST_SuggestedCoursesPanelLayout = new javax.swing.GroupLayout(ST_SuggestedCoursesPanel);
        ST_SuggestedCoursesPanel.setLayout(ST_SuggestedCoursesPanelLayout);
        ST_SuggestedCoursesPanelLayout.setHorizontalGroup(
            ST_SuggestedCoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_SuggestedCoursesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ST_SuggestedCoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ST_SuggestedCoursesScrollPane)
                    .addComponent(ST_SaveAndEnroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ST_SuggestedCoursesPanelLayout.setVerticalGroup(
            ST_SuggestedCoursesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_SuggestedCoursesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ST_SuggestedCoursesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ST_SaveAndEnroll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // Add listeners to Year Level and Semester dropdowns
        ST_YearLevel.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                loadSuggestedCourses();
            }
        });
        
        ST_CurrentSemester.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                loadSuggestedCourses();
            }
        });

        ST_STUDENT_ID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_STUDENT_ID.setForeground(new java.awt.Color(255, 255, 255));
        ST_STUDENT_ID.setText("Student ID");

        ST_STUDENT_NAME.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_STUDENT_NAME.setForeground(new java.awt.Color(255, 255, 255));
        ST_STUDENT_NAME.setText("Student Name");

        ST_BIRTHDAY.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_BIRTHDAY.setForeground(new java.awt.Color(255, 255, 255));
        ST_BIRTHDAY.setText("Date of Birth");

        ST_GENDER.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_GENDER.setForeground(new java.awt.Color(255, 255, 255));
        ST_GENDER.setText("Gender");

        ST_EMAIL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_EMAIL.setForeground(new java.awt.Color(255, 255, 255));
        ST_EMAIL.setText("Email");

        ST_PHONE_NUM.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_PHONE_NUM.setForeground(new java.awt.Color(255, 255, 255));
        ST_PHONE_NUM.setText("Phone Number");

        ST_FATHERS_NAME.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_FATHERS_NAME.setForeground(new java.awt.Color(255, 255, 255));
        ST_FATHERS_NAME.setText("Father's Name");

        ST_MOTHERS_NAME.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_MOTHERS_NAME.setForeground(new java.awt.Color(255, 255, 255));
        ST_MOTHERS_NAME.setText("Mother's Name");

        ST_GUARDIANS_PHONE_NUM.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_GUARDIANS_PHONE_NUM.setForeground(new java.awt.Color(255, 255, 255));
        ST_GUARDIANS_PHONE_NUM.setText("Guardian's Phone No.");

        ST_ADDRESS.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_ADDRESS.setForeground(new java.awt.Color(255, 255, 255));
        ST_ADDRESS.setText("Address");

        // Academic Information Labels
        ST_ACADEMIC_INFO.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_ACADEMIC_INFO.setForeground(new java.awt.Color(189, 216, 233));
        ST_ACADEMIC_INFO.setText("Academic Information");

        ST_YEAR_LEVEL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_YEAR_LEVEL.setForeground(new java.awt.Color(255, 255, 255));
        ST_YEAR_LEVEL.setText("Year Level");

        ST_CURRENT_SEMESTER.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_CURRENT_SEMESTER.setForeground(new java.awt.Color(255, 255, 255));
        ST_CURRENT_SEMESTER.setText("Current Semester");

        ST_STUDENT_TYPE.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_STUDENT_TYPE.setForeground(new java.awt.Color(255, 255, 255));
        ST_STUDENT_TYPE.setText("Student Type");

        ST_SECTION.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_SECTION.setForeground(new java.awt.Color(255, 255, 255));
        ST_SECTION.setText("Section");

        ST_GWA_LABEL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ST_GWA_LABEL.setForeground(new java.awt.Color(255, 255, 255));
        ST_GWA_LABEL.setText("GWA");

        javax.swing.GroupLayout ST_LeftPanelLayout = new javax.swing.GroupLayout(ST_LeftPanel);
        ST_LeftPanel.setLayout(ST_LeftPanelLayout);
        ST_LeftPanelLayout.setHorizontalGroup(
            ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ST_LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_STUDENT_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ST_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_PHONE_NUM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ST_PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_FATHERS_NAME, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(ST_FathersName, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_MOTHERS_NAME)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ST_MothersName, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ST_BIRTHDAY, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ST_STUDENT_NAME, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ST_StudentName)
                            .addComponent(ST_DateOfBirth, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)))
                    .addGroup(ST_LeftPanelLayout.createSequentialGroup()
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(ST_EMAIL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ST_GENDER, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ST_Gender, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ST_Email, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ST_LeftPanelLayout.createSequentialGroup()
                                .addComponent(ST_ADDRESS, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(ST_GUARDIANS_PHONE_NUM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ST_GuardiansPhoneNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(ST_Address)))
                    .addComponent(ST_ACADEMIC_INFO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_YEAR_LEVEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ST_YearLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_CURRENT_SEMESTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ST_CurrentSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_STUDENT_TYPE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ST_StudentType, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_SECTION, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ST_Section, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ST_LeftPanelLayout.createSequentialGroup()
                        .addComponent(ST_GWA_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ST_GWA, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ST_SuggestedCoursesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ST_LeftPanelLayout.setVerticalGroup(
            ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                // Personal Info Section
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_STUDENT_ID)
                    .addComponent(ST_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2) // Tightest possible gap
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_STUDENT_NAME)
                    .addComponent(ST_StudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_BIRTHDAY)
                    .addComponent(ST_DateOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_GENDER)
                    .addComponent(ST_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_EMAIL)
                    .addComponent(ST_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_PHONE_NUM)
                    .addComponent(ST_PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_FATHERS_NAME)
                    .addComponent(ST_FathersName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_MOTHERS_NAME)
                    .addComponent(ST_MothersName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_GUARDIANS_PHONE_NUM)
                    .addComponent(ST_GuardiansPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_ADDRESS)
                    .addComponent(ST_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    
                // Header
                .addGap(8, 8, 8)
                .addComponent(ST_ACADEMIC_INFO)
                .addGap(4, 4, 4)
                
                // Academic Info Section
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_YEAR_LEVEL)
                    .addComponent(ST_YearLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_CURRENT_SEMESTER)
                    .addComponent(ST_CurrentSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_STUDENT_TYPE)
                    .addComponent(ST_StudentType, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_SECTION)
                    .addComponent(ST_Section, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_GWA_LABEL)
                    .addComponent(ST_GWA, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    
                // Bottom Panel
                .addGap(8, 8, 8)
                .addComponent(ST_SuggestedCoursesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ST_RightPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_RightPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        ST_RightPanel.setPreferredSize(new java.awt.Dimension(620, 480));

        ST_SearchStudentPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_SearchStudentPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(189, 216, 233), 4, true));
        ST_SearchStudentPanel.setPreferredSize(new java.awt.Dimension(620, 119));

        ST_SEARCH_STUDENT.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_SEARCH_STUDENT.setForeground(new java.awt.Color(255, 255, 255));
        ST_SEARCH_STUDENT.setText("Search Student");

        ST_Search.setBackground(new java.awt.Color(146, 190, 219));
        ST_Search.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_Search.setText("Search");
        ST_Search.addActionListener(this::ST_SearchActionPerformed);

        ST_Refresh.setBackground(new java.awt.Color(146, 190, 219));
        ST_Refresh.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_Refresh.setText("Refresh");
        ST_Refresh.addActionListener(this::ST_RefreshActionPerformed);

        ST_SearchStudent.setBackground(new java.awt.Color(146, 190, 219));
        ST_SearchStudent.addActionListener(this::ST_SearchStudentActionPerformed);

        javax.swing.GroupLayout ST_SearchStudentPanelLayout = new javax.swing.GroupLayout(ST_SearchStudentPanel);
        ST_SearchStudentPanel.setLayout(ST_SearchStudentPanelLayout);
        ST_SearchStudentPanelLayout.setHorizontalGroup(
            ST_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_SearchStudentPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(ST_SEARCH_STUDENT, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ST_SearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ST_Search)
                .addGap(18, 18, 18)
                .addComponent(ST_Refresh)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        ST_SearchStudentPanelLayout.setVerticalGroup(
            ST_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_SearchStudentPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(ST_SearchStudentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ST_SEARCH_STUDENT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ST_SearchStudent, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ST_Search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(ST_Refresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        ST_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", "Student Name", "Age", "Date of Birth", "Year Level", "Section", "Student Status", "GWA", "Email", "Phone No.",
            }
        ));
        ST_TableScrollPane.setViewportView(ST_Table);

        javax.swing.GroupLayout ST_RightPanelLayout = new javax.swing.GroupLayout(ST_RightPanel);
        ST_RightPanel.setLayout(ST_RightPanelLayout);
        ST_RightPanelLayout.setHorizontalGroup(
            ST_RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_RightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ST_RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ST_SearchStudentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_TableScrollPane))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ST_RightPanelLayout.setVerticalGroup(
            ST_RightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_RightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ST_SearchStudentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ST_TableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        ST_BottomPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_BottomPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));

        ST_AddNew.setBackground(new java.awt.Color(73, 118, 159));
        ST_AddNew.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_AddNew.setText("Add New");
        ST_AddNew.addActionListener(this::ST_AddNewActionPerformed);

        ST_Update.setBackground(new java.awt.Color(73, 118, 159));
        ST_Update.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_Update.setText("Update");
        ST_Update.addActionListener(this::ST_UpdateActionPerformed);

        ST_Delete.setBackground(new java.awt.Color(73, 118, 159));
        ST_Delete.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_Delete.setText("Delete");
        ST_Delete.addActionListener(this::ST_DeleteActionPerformed);

        ST_Clear.setBackground(new java.awt.Color(73, 118, 159));
        ST_Clear.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_Clear.setText("Clear");
        ST_Clear.addActionListener(this::ST_ClearActionPerformed);

        ST_Logout.setBackground(new java.awt.Color(73, 118, 159));
        ST_Logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ST_Logout.setText("Logout");
        ST_Logout.addActionListener(this::ST_LogoutActionPerformed); 

        javax.swing.GroupLayout ST_BottomPanelLayout = new javax.swing.GroupLayout(ST_BottomPanel);
        ST_BottomPanel.setLayout(ST_BottomPanelLayout);
        ST_BottomPanelLayout.setHorizontalGroup(
            ST_BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_BottomPanelLayout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(ST_AddNew)
                .addGap(53, 53, 53)
                .addComponent(ST_Update)
                .addGap(59, 59, 59)
                .addComponent(ST_Delete)
                .addGap(68, 68, 68)
                .addComponent(ST_Clear)
                .addGap(59, 59, 59)
                .addComponent(ST_Logout)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ST_BottomPanelLayout.setVerticalGroup(
            ST_BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_BottomPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(ST_BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_AddNew)
                    .addComponent(ST_Update)
                    .addComponent(ST_Delete)
                    .addComponent(ST_Clear)
                    .addComponent(ST_Logout))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout StudentTabLayout = new javax.swing.GroupLayout(StudentTab);
        StudentTab.setLayout(StudentTabLayout);
        StudentTabLayout.setHorizontalGroup(
            StudentTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ST_LeftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(StudentTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ST_RightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
                    .addComponent(ST_BottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(335, 335, 335))
        );
        StudentTabLayout.setVerticalGroup(
            StudentTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StudentTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ST_LeftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addGroup(StudentTabLayout.createSequentialGroup()
                        .addComponent(ST_RightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ST_BottomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        MainTabPanel.addTab("Student", StudentTab);

        courseTab = new CourseTab();
        MainTabPanel.addTab("Course", courseTab);
        
        // Add tab change listener to refresh Course tab when selected
        MainTabPanel.addChangeListener(e -> {
            // Only process tab changes after initial setup is complete
            if (!isInitialized) return;
            
            int selectedTab = MainTabPanel.getSelectedIndex();
            
            if (selectedTab == 0) { // Student tab
                // Refresh student table and suggested courses when returning
                loadStudentData();
                loadStudentTableData();
                
                // Refresh suggested courses for currently selected student
                String currentStudentID = ST_StudentID.getText().trim();
                if (!currentStudentID.isEmpty()) {
                    loadSuggestedCourses();
                }
            } 
            else if (selectedTab == 1) { // Course tab
                courseTab.refreshStudentList();
                
                // Auto-populate student ID if coming from Save & Enroll
                String sessionStudentID = SessionManager.getCurrentStudentID();
                if (sessionStudentID != null && !sessionStudentID.isEmpty()) {
                    courseTab.autoPopulateStudent(sessionStudentID);
                    SessionManager.clearSession(); // Clear after use
                }
            }
        });

        CourseTab.setBackground(new java.awt.Color(31, 58, 95));

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
        CT_Course1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems", "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies", "Linear Algebra for Computing", "Human-Computer Interaction", "Data Structures and Algorithms", "Design and Analysis of Algorithms", "Database Management Systems", "Computer Networks", "Advanced Programming Concepts", "Operating Systems", "Compiler Design", "Information Security", "Distributed Systems", "Systems Integration Project" }));
        CT_Course1.setMinimumSize(new java.awt.Dimension(90, 28));

        CT_Course2.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems", "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies", "Linear Algebra for Computing", "Human-Computer Interaction", "Data Structures and Algorithms", "Design and Analysis of Algorithms", "Database Management Systems", "Computer Networks", "Advanced Programming Concepts", "Operating Systems", "Compiler Design", "Information Security", "Distributed Systems", "Systems Integration Project" }));
        CT_Course2.setMinimumSize(new java.awt.Dimension(90, 28));

        CT_Course3.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems", "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies", "Linear Algebra for Computing", "Human-Computer Interaction", "Data Structures and Algorithms", "Design and Analysis of Algorithms", "Database Management Systems", "Computer Networks", "Advanced Programming Concepts", "Operating Systems", "Compiler Design", "Information Security", "Distributed Systems", "Systems Integration Project" }));
        CT_Course3.setMinimumSize(new java.awt.Dimension(90, 28));

        CT_Course4.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems", "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies", "Linear Algebra for Computing", "Human-Computer Interaction", "Data Structures and Algorithms", "Design and Analysis of Algorithms", "Database Management Systems", "Computer Networks", "Advanced Programming Concepts", "Operating Systems", "Compiler Design", "Information Security", "Distributed Systems", "Systems Integration Project" }));
        CT_Course4.setMinimumSize(new java.awt.Dimension(90, 28));

        CT_Course5.setBackground(new java.awt.Color(146, 190, 219));
        CT_Course5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Course5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems", "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies", "Linear Algebra for Computing", "Human-Computer Interaction", "Data Structures and Algorithms", "Design and Analysis of Algorithms", "Database Management Systems", "Computer Networks", "Advanced Programming Concepts", "Operating Systems", "Compiler Design", "Information Security", "Distributed Systems", "Systems Integration Project" }));
        CT_Course5.setMinimumSize(new java.awt.Dimension(90, 28));

        CT_Semester.setBackground(new java.awt.Color(146, 190, 219));
        CT_Semester.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        CT_Semester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Semester", "2nd Semester" }));

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
                            .addComponent(CT_Course1, 0, 363, Short.MAX_VALUE)
                            .addComponent(CT_Semester, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_StudentID, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CT_LeftPanelLayout.createSequentialGroup()
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CT_COURSE3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_COURSE4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CT_Course4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_Course3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_Course2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CT_Course5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CT_COURSE1)
                    .addComponent(CT_Course1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE2)
                    .addComponent(CT_Course2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE3)
                    .addComponent(CT_Course3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE4)
                    .addComponent(CT_Course4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CT_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CT_COURSE5)
                    .addComponent(CT_Course5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout CourseTabLayout = new javax.swing.GroupLayout(CourseTab);
        CourseTab.setLayout(CourseTabLayout);
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

        ScoreTab = new javax.swing.JPanel();
        ScoreTab.setBackground(new java.awt.Color(31, 58, 95));

        javax.swing.GroupLayout ScoreTabLayout = new javax.swing.GroupLayout(ScoreTab);
        ScoreTab.setLayout(ScoreTabLayout);
        ScoreTabLayout.setHorizontalGroup(
            ScoreTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1520, Short.MAX_VALUE)
        );
        ScoreTabLayout.setVerticalGroup(
            ScoreTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );

        MainTabPanel.addTab("Score",new ScoreTab());

        MainTabPanel.addTab("Mark Sheet", new Marksheettab());

        ScheduleTab.setBackground(new java.awt.Color(31, 58, 95));
        
        SCH_SearchPanel = new javax.swing.JPanel();
        SCH_FiltersPanel = new javax.swing.JPanel();
        SCH_SemesterIDLabel = new javax.swing.JLabel();
        SCH_StudentSearchField = new javax.swing.JTextField();
        SCH_SearchStudentButton = new javax.swing.JButton();
        SCH_SemesterSearchField = new javax.swing.JTextField();
        SCH_StudentIDLabel = new javax.swing.JLabel();
        SCH_SearchSemesterButton = new javax.swing.JButton();
        SCH_GWAPanel = new javax.swing.JPanel();
        SCH_GWALabel = new javax.swing.JLabel();
        SCH_MonthYearPanel = new javax.swing.JPanel();
        SCH_MonthComboBox = new javax.swing.JComboBox<>();
        SCH_YearSpinner = new javax.swing.JSpinner();
        SCH_MonthYearDisplayLabel = new javax.swing.JLabel();
        SCH_RefreshButton = new javax.swing.JButton();
        SCH_TableScrollPane = new javax.swing.JScrollPane();
        SCH_Table = new javax.swing.JTable();

        SCH_SearchPanel.setBackground(new java.awt.Color(0, 30, 58));
        SCH_SearchPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        SCH_FiltersPanel.setBackground(new java.awt.Color(0, 30, 58));
        SCH_FiltersPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        SCH_SemesterIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        SCH_SemesterIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        SCH_SemesterIDLabel.setText("Semester's ID");

        SCH_StudentSearchField.setBackground(new java.awt.Color(146, 190, 219));

        SCH_SearchStudentButton.setBackground(new java.awt.Color(189, 216, 233));
        SCH_SearchStudentButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        SCH_SearchStudentButton.setText("Search");
        SCH_SearchStudentButton.addActionListener(this::SCH_SearchStudentButtonActionPerformed);

        SCH_SemesterSearchField.setBackground(new java.awt.Color(146, 190, 219));

        SCH_StudentIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        SCH_StudentIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        SCH_StudentIDLabel.setText("Student ID");

        SCH_SearchSemesterButton.setBackground(new java.awt.Color(189, 216, 233));
        SCH_SearchSemesterButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        SCH_SearchSemesterButton.setText("Search");

        javax.swing.GroupLayout SCH_FiltersPanelLayout = new javax.swing.GroupLayout(SCH_FiltersPanel);
        SCH_FiltersPanel.setLayout(SCH_FiltersPanelLayout);
        SCH_FiltersPanelLayout.setHorizontalGroup(
            SCH_FiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_FiltersPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(SCH_FiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SCH_StudentIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SCH_SemesterIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SCH_FiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(SCH_FiltersPanelLayout.createSequentialGroup()
                            .addComponent(SCH_SemesterSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SCH_SearchSemesterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SCH_FiltersPanelLayout.createSequentialGroup()
                            .addComponent(SCH_StudentSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31)
                            .addComponent(SCH_SearchStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        SCH_FiltersPanelLayout.setVerticalGroup(
            SCH_FiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_FiltersPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(SCH_StudentIDLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SCH_FiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SCH_StudentSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SCH_SearchStudentButton))
                .addGap(18, 18, 18)
                .addComponent(SCH_SemesterIDLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SCH_FiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SCH_SemesterSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SCH_SearchSemesterButton))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        SCH_GWAPanel.setBackground(new java.awt.Color(0, 30, 58));
        SCH_GWAPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        SCH_GWALabel.setFont(new java.awt.Font("Segoe UI", 1, 40)); 
        SCH_GWALabel.setForeground(new java.awt.Color(255, 255, 255));
        SCH_GWALabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SCH_GWALabel.setText("GWA. --");

        javax.swing.GroupLayout SCH_GWAPanelLayout = new javax.swing.GroupLayout(SCH_GWAPanel);
        SCH_GWAPanel.setLayout(SCH_GWAPanelLayout);
        SCH_GWAPanelLayout.setHorizontalGroup(
            SCH_GWAPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_GWAPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SCH_GWALabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        SCH_GWAPanelLayout.setVerticalGroup(
            SCH_GWAPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_GWAPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SCH_GWALabel, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout SCH_SearchPanelLayout = new javax.swing.GroupLayout(SCH_SearchPanel);
        SCH_SearchPanel.setLayout(SCH_SearchPanelLayout);
        SCH_SearchPanelLayout.setHorizontalGroup(
            SCH_SearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_SearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SCH_SearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SCH_FiltersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SCH_GWAPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        SCH_SearchPanelLayout.setVerticalGroup(
            SCH_SearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_SearchPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(SCH_FiltersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SCH_GWAPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        SCH_MonthYearPanel.setBackground(new java.awt.Color(0, 30, 58));
        SCH_MonthYearPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        SCH_MonthComboBox.addActionListener(this::SCH_MonthComboBoxActionPerformed);

        SCH_MonthYearDisplayLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        SCH_MonthYearDisplayLabel.setForeground(new java.awt.Color(255, 255, 255));
        SCH_MonthYearDisplayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SCH_MonthYearDisplayLabel.setText("January 2026");

        SCH_RefreshButton.setBackground(new java.awt.Color(189, 216, 233));
        SCH_RefreshButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        SCH_RefreshButton.setText("Refresh");
        SCH_RefreshButton.addActionListener(this::SCH_RefreshButtonActionPerformed);

        SCH_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            }
        ));
        SCH_TableScrollPane.setViewportView(SCH_Table);

        javax.swing.GroupLayout SCH_MonthYearPanelLayout = new javax.swing.GroupLayout(SCH_MonthYearPanel);
        SCH_MonthYearPanel.setLayout(SCH_MonthYearPanelLayout);
        SCH_MonthYearPanelLayout.setHorizontalGroup(
            SCH_MonthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_MonthYearPanelLayout.createSequentialGroup()
                .addGroup(SCH_MonthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SCH_MonthYearPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(SCH_MonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(SCH_YearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(SCH_MonthYearDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SCH_RefreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(SCH_MonthYearPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(SCH_TableScrollPane)))
                .addContainerGap())
        );
        SCH_MonthYearPanelLayout.setVerticalGroup(
            SCH_MonthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SCH_MonthYearPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SCH_MonthYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SCH_MonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SCH_YearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SCH_MonthYearDisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SCH_RefreshButton))
                .addGap(18, 18, 18)
                .addComponent(SCH_TableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ScheduleTabLayout = new javax.swing.GroupLayout(ScheduleTab);
        ScheduleTab.setLayout(ScheduleTabLayout);
        ScheduleTabLayout.setHorizontalGroup(
            ScheduleTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScheduleTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SCH_SearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SCH_MonthYearPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ScheduleTabLayout.setVerticalGroup(
            ScheduleTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ScheduleTabLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(ScheduleTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SCH_MonthYearPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SCH_SearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        MainTabPanel.addTab("Schedule", ScheduleTab);

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(MainTabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1520, Short.MAX_VALUE)
                    .addComponent(SMSPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SMSPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MainTabPanel)
                .addContainerGap())
        );

        MainScrollPane.setViewportView(MainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1540, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
        );

        setSize(1350, 680);
        setMinimumSize(new java.awt.Dimension(1200, 600));
        setLocationRelativeTo(null);
        
        // Add table click listeners for auto-populate
        ST_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = ST_Table.getSelectedRow();
                if (selectedRow >= 0) {
                    populateStudentFormFromTable(selectedRow);
                }
            }
        });
        
        CT_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = CT_Table.getSelectedRow();
                if (selectedRow >= 0) {
                    courseTab.populateFormFromTable(selectedRow);
                }
            }
        });
        
        // Mark initialization as complete
        isInitialized = true;
    }// </editor-fold>                        

    private void ST_StudentNameActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void ST_StudentIDActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void CT_SearchActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void CT_idActionPerformed(java.awt.event.ActionEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void CT_StudentIDActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                                                                                                                         

    private void ST_SearchActionPerformed(java.awt.event.ActionEvent evt) {                                                
        ST_SearchStudentActionPerformed(evt);
    }                                         

    private void ST_RefreshActionPerformed(java.awt.event.ActionEvent evt) {                                           
        ST_SearchStudent.setText("");
        loadStudentTableData();
    }                                          

    private void ST_SearchStudentActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String searchName = ST_SearchStudent.getText().trim();
        if (searchName.isEmpty()) {
            loadStudentTableData();
            return;
        }
        //Find student by ID
        for (Student student : students) {
            if (student.getStudentID().equalsIgnoreCase(searchName)) {
                // Update table to show only this student
                DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Student ID", "Name", "Age", "DOB", "Year Level", "Section","Type", "GWA", "Email", "Phone"},
                    0
                );
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.getAge(),
                    student.getDateOfBirth(),
                    student.getYearLevel(),
                    student.getSection(),
                    student.getStudentType(),
                    student.getGwa(),
                    student.getEmail(),
                    student.getPhoneNumber()
                });

                logger.info("Found student: " + student.getStudentID());
                
                // Create and set table
                javax.swing.JTable table = new javax.swing.JTable(model);
                ST_TableScrollPane.setViewportView(table);
                ST_TableScrollPane.revalidate();
                ST_TableScrollPane.repaint();
                return;
            }
        }
        
        // Find student by name
        for (Student student : students) {
            if (student.getStudentName().toLowerCase().contains(searchName.toLowerCase())) {
                // Update table to show only this student
                DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Student ID", "Name", "Age", "DOB", "Year Level", "Section", "Type", "GWA", "Email", "Phone"},
                    0
                );
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getStudentName(),
                    student.getAge(),
                    student.getDateOfBirth(),
                    student.getYearLevel(),
                    student.getSection(),
                    student.getStudentType(),
                    student.getGwa(),
                    student.getEmail(),
                    student.getPhoneNumber()
                });

                logger.info("Found student: " + student.getStudentName());
                
                // Create and set table
                javax.swing.JTable table = new javax.swing.JTable(model);
                ST_TableScrollPane.setViewportView(table);
                ST_TableScrollPane.revalidate();
                ST_TableScrollPane.repaint();
                return;
            }
        }
        
        javax.swing.JOptionPane.showMessageDialog(this, "Student not found!", "Search Result", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void CT_SearchStudentActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String searchName = CT_SearchStudent.getText().trim();
        if (searchName.isEmpty()) {
            loadStudentTableData();
            return;
        }
        //Find student by ID
        for (Student student : students) {
            if (student.getStudentID().equalsIgnoreCase(searchName)) {
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

                logger.info("Found student: " + student.getStudentID());
                
                // Create and set table
                javax.swing.JTable table = new javax.swing.JTable(model);
                CT_TableScrollPane.setViewportView(table);
                CT_TableScrollPane.revalidate();
                CT_TableScrollPane.repaint();
                return;
            }
        }
        
        // Find student by name
        for (Student student : students) {
            if (student.getStudentName().toLowerCase().contains(searchName.toLowerCase())) {
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

                logger.info("Found student: " + student.getStudentName());
                
                // Create and set table
                javax.swing.JTable table = new javax.swing.JTable(model);
                CT_TableScrollPane.setViewportView(table);
                CT_TableScrollPane.revalidate();
                CT_TableScrollPane.repaint();
                return;
            }
        }
        
        javax.swing.JOptionPane.showMessageDialog(this, "Student not found!", "Search Result", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void CT_RefreshActionPerformed(java.awt.event.ActionEvent evt) {                                           
        CT_SearchStudent.setText("");
        loadStudentTableData();
    }
    
    private void ST_DeleteActionPerformed(java.awt.event.ActionEvent evt) {                                         
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) ST_Table.getModel();
        int selectedRow = ST_Table.getSelectedRow();

        if (selectedRow >= 0) {
            // Get student ID from the selected row
            String studentID = model.getValueAt(selectedRow, 0).toString();
            
            // Remove from table
            model.removeRow(selectedRow);
            
            // Remove from ArrayList
            students.removeIf(s -> s.getStudentID().equals(studentID));
            
            // Save to file
            try {
                studentFileSaver.save(getStudentFilePath(), students);
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Student deleted and saved successfully!", 
                    "Success", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Error saving to file: " + e.getMessage(), 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No student selected to delete.", 
                "Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    private void ST_AddNewActionPerformed(java.awt.event.ActionEvent evt) {
        // Validate required fields
        if (ST_StudentName.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student Name are required!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Syncing student ID
        int maxId = 0;
        for (Student s : students) {
            int idNum = Integer.parseInt(s.getStudentID().replace("STU-", ""));
            if (idNum > maxId) maxId = idNum;
        }
        Student.setNextIdNum(maxId); 
        

        try {
            // Collect data from form fields
            String name = ST_StudentName.getText().trim();
            String gender = (String) ST_Gender.getSelectedItem();
            String email = ST_Email.getText().trim();
            String phoneNumber = ST_PhoneNumber.getText().trim();
            String address = ST_Address.getText().trim();
            String fathersName = ST_FathersName.getText().trim();
            String mothersName = ST_MothersName.getText().trim();
            String guardiansPhone = ST_GuardiansPhoneNumber.getText().trim();
            
            // Get date of birth from spinner
            java.util.Date dobDate = (java.util.Date) ST_DateOfBirth.getValue();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf.format(dobDate);
            
            // Calculate age from DOB
            java.util.Calendar dobCal = java.util.Calendar.getInstance();
            dobCal.setTime(dobDate);
            java.util.Calendar today = java.util.Calendar.getInstance();
            int age = today.get(java.util.Calendar.YEAR) - dobCal.get(java.util.Calendar.YEAR);
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < dobCal.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--;
            }
            
            // Get academic information from form
            String yearLevel = (String) ST_YearLevel.getSelectedItem();
            String studentType = (String) ST_StudentType.getSelectedItem();
            String section = ST_Section.getText().equals("(Auto-assigned)") ? "" : ST_Section.getText();
            java.util.ArrayList<String> subjectsEnrolled = new java.util.ArrayList<>();
            double gwa = 0.0;
            try {
                String gwaText = ST_GWA.getText().trim();
                if (!gwaText.isEmpty() && !gwaText.equals("0.00")) {
                    gwa = Double.parseDouble(gwaText);
                }
            } catch (NumberFormatException e) {
                gwa = 0.0; // Default to 0.0 if parsing fails
            }
            
            // Create new Student object (constructor without ID will auto-generate)
            Student newStudent = new Student(
                name, age, dob, yearLevel, section, studentType,
                subjectsEnrolled, gwa, email, phoneNumber, gender,
                address, fathersName, mothersName, guardiansPhone
            );
            
            // Add to ArrayList
            students.add(newStudent);
            
            // Save to file
            studentFileSaver.save(getStudentFilePath(), students);
            
            // Refresh table
            loadStudentTableData();
            
            // Show success message with generated ID
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student added successfully!\nGenerated ID: " + newStudent.getStudentID(), 
                "Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form and show next available ID
            clearStudentForm();
                
        } catch (Exception e) {
            logger.severe("Error in Add New: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error adding student: " + e.getMessage(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ST_UpdateActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = ST_Table.getSelectedRow();
        
        if (selectedRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Please select a student to update.", 
                "Selection Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate required fields
        if (ST_StudentID.getText().trim().isEmpty() || 
            ST_StudentName.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student ID and Name are required!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) ST_Table.getModel();
            String originalID = model.getValueAt(selectedRow, 0).toString();
            String newID = ST_StudentID.getText().trim();
            
            // Check if ID changed and if new ID already exists
            if (!originalID.equals(newID)) {
                for (Student s : students) {
                    if (s.getStudentID().equals(newID)) {
                        javax.swing.JOptionPane.showMessageDialog(this, 
                            "Student ID already exists!", 
                            "Duplicate Error", 
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }
            
            // Collect data from form fields
            String name = ST_StudentName.getText().trim();
            String gender = (String) ST_Gender.getSelectedItem();
            String email = ST_Email.getText().trim();
            String phoneNumber = ST_PhoneNumber.getText().trim();
            String address = ST_Address.getText().trim();
            String fathersName = ST_FathersName.getText().trim();
            String mothersName = ST_MothersName.getText().trim();
            String guardiansPhone = ST_GuardiansPhoneNumber.getText().trim();
            
            // Get date of birth from spinner
            java.util.Date dobDate = (java.util.Date) ST_DateOfBirth.getValue();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf.format(dobDate);
            
            // Calculate age from DOB
            java.util.Calendar dobCal = java.util.Calendar.getInstance();
            dobCal.setTime(dobDate);
            java.util.Calendar today = java.util.Calendar.getInstance();
            int age = today.get(java.util.Calendar.YEAR) - dobCal.get(java.util.Calendar.YEAR);
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < dobCal.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--;
            }
            
            // Get academic information from form
            String yearLevel = (String) ST_YearLevel.getSelectedItem();
            String studentType = (String) ST_StudentType.getSelectedItem();
            String section = ST_Section.getText().equals("(Auto-assigned)") ? "" : ST_Section.getText();
            
            // Preserve subjects enrolled from existing student
            java.util.ArrayList<String> subjectsEnrolled = new java.util.ArrayList<>();
            double gwa = 0.0;
            
            for (Student s : students) {
                if (s.getStudentID().equals(originalID)) {
                    subjectsEnrolled = s.getSubjectsEnrolled();
                    break;
                }
            }
            
            // Parse GWA from field
            try {
                String gwaText = ST_GWA.getText().trim();
                if (!gwaText.isEmpty() && !gwaText.equals("0.00")) {
                    gwa = Double.parseDouble(gwaText);
                }
            } catch (NumberFormatException e) {
                gwa = 0.0; // Default to 0.0 if parsing fails
            }
            
            // Create updated Student object
            Student updatedStudent = new Student(
                newID, name, age, dob, yearLevel, section, studentType,
                subjectsEnrolled, gwa, email, phoneNumber, gender,
                address, fathersName, mothersName, guardiansPhone
            );
            
            // Update in ArrayList
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getStudentID().equals(originalID)) {
                    students.set(i, updatedStudent);
                    break;
                }
            }
            
            // Save to file
            studentFileSaver.save(getStudentFilePath(), students);
            
            // Refresh table
            loadStudentTableData();
            
            // Clear form
            clearStudentForm();
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student updated successfully!", 
                "Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            logger.severe("Error in Update: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error updating student: " + e.getMessage(), 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ST_ClearActionPerformed(java.awt.event.ActionEvent evt) {
        clearStudentForm();
    }

    private void clearStudentForm() {
        // Generate and display next Student ID
        int maxId = 0;
        for (Student s : students) {
            String idStr = s.getStudentID().replace("STU-", "");
            try {
                int idNum = Integer.parseInt(idStr);
                if (idNum > maxId) maxId = idNum;
            } catch (NumberFormatException e) {
                // Skip invalid IDs
            }
        }
        String nextID = String.format("STU-%03d", maxId + 1);
        ST_StudentID.setText(nextID);
        ST_StudentID.setEditable(false); // Make read-only for new students
        ST_StudentID.setBackground(new java.awt.Color(240, 240, 240)); // Gray background
        
        ST_StudentName.setText("");
        ST_Email.setText("");
        ST_PhoneNumber.setText("");
        ST_Address.setText("");
        ST_FathersName.setText("");
        ST_MothersName.setText("");
        ST_GuardiansPhoneNumber.setText("");
        ST_Gender.setSelectedIndex(0);
        ST_DateOfBirth.setValue(new java.util.Date());
        
        // Clear academic fields
        ST_YearLevel.setSelectedIndex(0);
        ST_CurrentSemester.setSelectedIndex(0);
        ST_StudentType.setSelectedIndex(0);
        ST_Section.setText("(Auto-assigned)");
        ST_GWA.setText("0.00");
        
        ST_Table.clearSelection();
    }

    private void populateStudentFormFromTable(int rowIndex) {
        try {
            // Enable Student ID field for updates (keep read-only to prevent ID changes)
            ST_StudentID.setEditable(false); // Keep read-only even for updates to prevent ID conflicts
            ST_StudentID.setBackground(new java.awt.Color(240, 240, 240));
            
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) ST_Table.getModel();
            
            // Get student ID from table and find full student object
            String studentID = model.getValueAt(rowIndex, 0).toString();
            Student student = null;
            
            for (Student s : students) {
                if (s.getStudentID().equals(studentID)) {
                    student = s;
                    break;
                }
            }
            
            if (student != null) {
                // Populate form fields
                ST_StudentID.setText(student.getStudentID());
                ST_StudentName.setText(student.getStudentName());
                ST_Email.setText(student.getEmail() != null ? student.getEmail() : "");
                ST_PhoneNumber.setText(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");
                ST_Address.setText(student.getAddress() != null ? student.getAddress() : "");
                ST_FathersName.setText(student.getFathersName() != null ? student.getFathersName() : "");
                ST_MothersName.setText(student.getMothersName() != null ? student.getMothersName() : "");
                ST_GuardiansPhoneNumber.setText(student.getGuardiansPhoneNumber() != null ? student.getGuardiansPhoneNumber() : "");
                
                // Set gender
                String gender = student.getGender();
                if ("Male".equalsIgnoreCase(gender)) {
                    ST_Gender.setSelectedIndex(0);
                } else if ("Female".equalsIgnoreCase(gender)) {
                    ST_Gender.setSelectedIndex(1);
                }
                
                // Set date of birth
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dob = sdf.parse(student.getDateOfBirth());
                    ST_DateOfBirth.setValue(dob);
                } catch (java.text.ParseException e) {
                    logger.warning("Error parsing date: " + e.getMessage());
                    ST_DateOfBirth.setValue(new java.util.Date());
                }
                
                // Populate academic fields
                String yearLevel = student.getYearLevel();
                if ("1st Year".equals(yearLevel)) {
                    ST_YearLevel.setSelectedIndex(0);
                } else if ("2nd Year".equals(yearLevel)) {
                    ST_YearLevel.setSelectedIndex(1);
                } else {
                    ST_YearLevel.setSelectedIndex(0); // Default to 1st Year
                }
                
                String studentType = student.getStudentType();
                if ("Regular".equals(studentType)) {
                    ST_StudentType.setSelectedIndex(0);
                } else if ("Irregular".equals(studentType)) {
                    ST_StudentType.setSelectedIndex(1);
                } else {
                    ST_StudentType.setSelectedIndex(0); // Default to Regular
                }
                
                ST_Section.setText(student.getSection() != null && !student.getSection().isEmpty() ? 
                    student.getSection() : "(Auto-assigned)");
                ST_GWA.setText(String.format("%.2f", student.getGwa()));
                
                // Refresh suggested courses with student's enrollment status
                loadSuggestedCourses();
            }
        } catch (Exception e) {
            logger.warning("Error populating form from table: " + e.getMessage());
        }
    }

    private void ST_LogoutActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", 
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            javax.swing.JOptionPane.showMessageDialog(this, "Logged out successfully!", "Logout", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            // Optionally, close the window
            this.dispose();
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
                    logger.info("Found course data at: " + f.getAbsolutePath());
                    break;
                }
            }
            
            if (filePath != null) {
                courseLoader.load(filePath);
                logger.info("Loaded " + courseLoader.getAllSubjects().size() + " courses from file");
            } else {
                logger.warning("Could not find courseSubject.txt");
            }
        } catch (Exception e) {
            logger.severe("Error loading course data: " + e.getMessage());
        }
    }

    private void loadSuggestedCourses() {
        try {
            String selectedYearLevel = (String) ST_YearLevel.getSelectedItem();
            String selectedSemester = (String) ST_CurrentSemester.getSelectedItem();
            
            if (selectedYearLevel == null || selectedSemester == null) {
                return;
            }
            
            // Parse year level (1st Year -> 1, 2nd Year -> 2)
            int yearLevel = selectedYearLevel.equals("1st Year") ? 1 : 2;
            int semester = Integer.parseInt(selectedSemester);
            
            // Get current student's ID from form
            String currentStudentID = ST_StudentID.getText().trim();
            Student currentStudent = null;
            
            // Find the student object
            if (!currentStudentID.isEmpty()) {
                for (Student s : students) {
                    if (s.getStudentID().equals(currentStudentID)) {
                        currentStudent = s;
                        break;
                    }
                }
            }
            
            // Filter courses by year level and semester with status indicators
            java.util.List<String> courseList = new java.util.ArrayList<>();
            Collection<CourseSubject> allCourses = courseLoader.getAllSubjects();
            
            if (allCourses != null && !allCourses.isEmpty()) {
                for (CourseSubject course : allCourses) {
                    if (course.getYearLevel() == yearLevel && course.getSemester() == semester) {
                        String statusPrefix = "[A]"; // Available
                        String prereqNote = "";
                        
                        // Determine status if we have a student selected
                        if (currentStudent != null) {
                            String courseID = course.getCourseSubjectID();
                            java.util.ArrayList<String> enrolledSubjects = currentStudent.getSubjectsEnrolled();
                            
                            // Check if student has passed or is enrolled in this course
                            if (enrolledSubjects != null && enrolledSubjects.contains(courseID)) {
                                statusPrefix = "[/]"; // Passed/Enrolled
                            }
                            // Check if course has prerequisites and student hasn't taken them
                            else if (course.getPrerequisites() != null && !course.getPrerequisites().isEmpty()) {
                                java.util.ArrayList<CourseSubject> prerequisites = course.getPrerequisites();
                                java.util.ArrayList<String> missingPrereqs = new java.util.ArrayList<>();
                                
                                for (CourseSubject prereq : prerequisites) {
                                    String prereqID = prereq.getCourseSubjectID();
                                    if (enrolledSubjects == null || !enrolledSubjects.contains(prereqID)) {
                                        missingPrereqs.add(prereqID);
                                    }
                                }
                                
                                if (!missingPrereqs.isEmpty()) {
                                    statusPrefix = "[X]"; // Blocked by prerequisite
                                    prereqNote = " (Need: " + String.join(", ", missingPrereqs) + ")";
                                }
                            }
                        }
                        
                        String courseInfo = String.format("%s %s - %s (%d units)%s", 
                            statusPrefix,
                            course.getCourseSubjectID(), 
                            course.getCourseSubjectName(), 
                            course.getUnits(),
                            prereqNote);
                        courseList.add(courseInfo);
                    }
                }
            }
            
            // Update the list
            if (courseList.isEmpty()) {
                courseList.add("No courses found for " + selectedYearLevel + ", Semester " + selectedSemester);
            }
            
            ST_SuggestedCoursesList.setListData(courseList.toArray(new String[0]));
            
        } catch (Exception e) {
            logger.warning("Error loading suggested courses: " + e.getMessage());
            ST_SuggestedCoursesList.setListData(new String[]{"Error loading courses"});
        }
    }

    private void ST_SaveAndEnrollActionPerformed(java.awt.event.ActionEvent evt) {
        // First, save the student (same as Add New)
        if (ST_StudentName.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student Name is required!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Syncing student ID
        int maxId = 0;
        for (Student s : students) {
            int idNum = Integer.parseInt(s.getStudentID().replace("STU-", ""));
            if (idNum > maxId) maxId = idNum;
        }
        Student.setNextIdNum(maxId);

        try {
            // Collect data from form fields (same as Add New)
            String name = ST_StudentName.getText().trim();
            String gender = (String) ST_Gender.getSelectedItem();
            String email = ST_Email.getText().trim();
            String phoneNumber = ST_PhoneNumber.getText().trim();
            String address = ST_Address.getText().trim();
            String fathersName = ST_FathersName.getText().trim();
            String mothersName = ST_MothersName.getText().trim();
            String guardiansPhone = ST_GuardiansPhoneNumber.getText().trim();
            
            java.util.Date dobDate = (java.util.Date) ST_DateOfBirth.getValue();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf.format(dobDate);
            
            java.util.Calendar dobCal = java.util.Calendar.getInstance();
            dobCal.setTime(dobDate);
            java.util.Calendar today = java.util.Calendar.getInstance();
            int age = today.get(java.util.Calendar.YEAR) - dobCal.get(java.util.Calendar.YEAR);
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < dobCal.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--;
            }
            
            String yearLevel = (String) ST_YearLevel.getSelectedItem();
            String studentType = (String) ST_StudentType.getSelectedItem();
            String section = ST_Section.getText().equals("(Auto-assigned)") ? "" : ST_Section.getText();
            java.util.ArrayList<String> subjectsEnrolled = new java.util.ArrayList<>();
            double gwa = 0.0;
            try {
                String gwaText = ST_GWA.getText().trim();
                if (!gwaText.isEmpty() && !gwaText.equals("0.00")) {
                    gwa = Double.parseDouble(gwaText);
                }
            } catch (NumberFormatException e) {
                gwa = 0.0;
            }
            
            Student newStudent = new Student(
                name, age, dob, yearLevel, section, studentType,
                subjectsEnrolled, gwa, email, phoneNumber, gender,
                address, fathersName, mothersName, guardiansPhone
            );
            
            students.add(newStudent);
            
            // Save to file
            studentFileSaver.save(getStudentFilePath(), students);
            loadStudentTableData();
            
            // Store student in session for auto-population in Course Tab
            SessionManager.setCurrentStudent(newStudent.getStudentID(), newStudent.getStudentName());
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student saved successfully!\nID: " + newStudent.getStudentID() + "\n\nSwitching to Course Tab for enrollment...", 
                "Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Switch to Course Tab
            MainTabPanel.setSelectedIndex(1);
            
        } catch (Exception e) {
            logger.severe("Error in Save & Enroll: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace to console for debugging
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Error saving student: " + e.getMessage(), 
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SCH_SearchStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Placeholder for Schedule tab student search functionality
        // This would search for students in the schedule system
    }

    private void SCH_MonthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // Placeholder for Schedule tab month selection
        // This would filter schedules by selected month
    }

    private void SCH_RefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Placeholder for Schedule tab refresh functionality
        // This would reload the schedule data
    }

    private void CT_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        // This method is in CourseTab, not StudentCourseTab
        // The action listener should be handled by CourseTab instance
    }

    private void CT_LogoutActionPerformed(java.awt.event.ActionEvent evt) {
        ST_LogoutActionPerformed(evt);
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new StudentCourseTab().setVisible(true));
    }

    // Variables declaration - do not modify                     
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
    private javax.swing.JPanel CourseTab;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JScrollPane MainScrollPane;
    private javax.swing.JTabbedPane MainTabPanel;
    private javax.swing.JPanel MarkSheetTab;
    private javax.swing.JLabel SMS;
    private javax.swing.JPanel SMSPanel;
    private javax.swing.JLabel ST_ADDRESS;
    private javax.swing.JButton ST_AddNew;
    private javax.swing.JTextField ST_Address;
    private javax.swing.JLabel ST_BIRTHDAY;
    private javax.swing.JPanel ST_BottomPanel;
    private javax.swing.JButton ST_Clear;
    private javax.swing.JButton ST_Delete;
    private javax.swing.JLabel ST_EMAIL;
    private javax.swing.JTextField ST_Email;
    private javax.swing.JLabel ST_FATHERS_NAME;
    private javax.swing.JTextField ST_FathersName;
    private javax.swing.JLabel ST_GENDER;
    private javax.swing.JLabel ST_GUARDIANS_PHONE_NUM;
    private javax.swing.JComboBox<String> ST_Gender;
    private javax.swing.JTextField ST_GuardiansPhoneNumber;
    private javax.swing.JPanel ST_LeftPanel;
    private javax.swing.JButton ST_Logout;
    private javax.swing.JLabel ST_MOTHERS_NAME;
    private javax.swing.JTextField ST_MothersName;
    private javax.swing.JLabel ST_PHONE_NUM;
    private javax.swing.JTextField ST_PhoneNumber;
    private javax.swing.JButton ST_Refresh;
    private javax.swing.JPanel ST_RightPanel;
    private javax.swing.JLabel ST_SEARCH_STUDENT;
    private javax.swing.JLabel ST_STUDENT_ID;
    private javax.swing.JLabel ST_STUDENT_NAME;
    private javax.swing.JButton ST_Search;
    private javax.swing.JTextField ST_SearchStudent;
    private javax.swing.JPanel ST_SearchStudentPanel;
    private javax.swing.JTextField ST_StudentID;
    private javax.swing.JTextField ST_StudentName;
    private javax.swing.JTable ST_Table;
    private javax.swing.JScrollPane ST_TableScrollPane;
    private javax.swing.JButton ST_Update;
    private javax.swing.JPanel ScheduleTab;
    private javax.swing.JPanel ScoreTab;
    private javax.swing.JPanel StudentTab;
    private javax.swing.JSpinner ST_DateOfBirth;
    
    // Academic Information Fields
    private javax.swing.JComboBox<String> ST_YearLevel;
    private javax.swing.JComboBox<String> ST_CurrentSemester;
    private javax.swing.JComboBox<String> ST_StudentType;
    private javax.swing.JTextField ST_Section;
    private javax.swing.JTextField ST_GWA;
    private javax.swing.JLabel ST_YEAR_LEVEL;
    private javax.swing.JLabel ST_CURRENT_SEMESTER;
    private javax.swing.JLabel ST_STUDENT_TYPE;
    private javax.swing.JLabel ST_SECTION;
    private javax.swing.JLabel ST_GWA_LABEL;
    private javax.swing.JLabel ST_ACADEMIC_INFO;
    private javax.swing.JPanel ST_SuggestedCoursesPanel;
    private javax.swing.JScrollPane ST_SuggestedCoursesScrollPane;
    private javax.swing.JList<String> ST_SuggestedCoursesList;
    private javax.swing.JButton ST_SaveAndEnroll;
    // End of variables declaration                   
}