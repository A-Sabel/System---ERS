// student course tab TAMA SHA

package ers.group;

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eli
 */
public class StudentCourseTab extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentCourseTab.class.getName());
    private ArrayList<Student> students;
    private StudentFileLoader studentFileLoader;
    private StudentFileSaver studentFileSaver;
    private String studentFilePath; // Store the actual file path found during loading

    /**
     * Creates new form Student
     */
    public StudentCourseTab() {
        initComponents();

        students = new ArrayList<>();
        studentFileSaver = new StudentFileSaver();
        loadStudentData();
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
                stud.getStudentType(),
                stud.getGwa(),
                stud.getEmail(),
                stud.getPhoneNumber()
            });
        }
    }

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
        ScheduleTab = new Scheduletab();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MainPanel.setBackground(new java.awt.Color(31, 58, 95));
        MainPanel.setPreferredSize(new java.awt.Dimension(1200, 699));

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
        ST_LeftPanel.setPreferredSize(new java.awt.Dimension(460, 703));

        ST_StudentID.setBackground(new java.awt.Color(146, 190, 219));
        ST_StudentID.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        ST_StudentID.addActionListener(this::ST_StudentIDActionPerformed);

        ST_StudentName.setBackground(new java.awt.Color(146, 190, 219));
        ST_StudentName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        ST_StudentName.addActionListener(this::ST_StudentNameActionPerformed);

        ST_Email.setBackground(new java.awt.Color(146, 190, 219));
        ST_Email.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ST_PhoneNumber.setBackground(new java.awt.Color(146, 190, 219));
        ST_PhoneNumber.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ST_FathersName.setBackground(new java.awt.Color(146, 190, 219));
        ST_FathersName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ST_MothersName.setBackground(new java.awt.Color(146, 190, 219));
        ST_MothersName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ST_GuardiansPhoneNumber.setBackground(new java.awt.Color(146, 190, 219));
        ST_GuardiansPhoneNumber.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ST_Address.setBackground(new java.awt.Color(146, 190, 219));
        ST_Address.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ST_Gender.setBackground(new java.awt.Color(146, 190, 219));
        ST_Gender.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        ST_Gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));

        ST_STUDENT_ID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_STUDENT_ID.setForeground(new java.awt.Color(255, 255, 255));
        ST_STUDENT_ID.setText("Student ID");

        ST_STUDENT_NAME.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_STUDENT_NAME.setForeground(new java.awt.Color(255, 255, 255));
        ST_STUDENT_NAME.setText("Student Name");

        ST_BIRTHDAY.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_BIRTHDAY.setForeground(new java.awt.Color(255, 255, 255));
        ST_BIRTHDAY.setText("Date of Birth");

        ST_GENDER.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_GENDER.setForeground(new java.awt.Color(255, 255, 255));
        ST_GENDER.setText("Gender");

        ST_EMAIL.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_EMAIL.setForeground(new java.awt.Color(255, 255, 255));
        ST_EMAIL.setText("Email");

        ST_PHONE_NUM.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_PHONE_NUM.setForeground(new java.awt.Color(255, 255, 255));
        ST_PHONE_NUM.setText("Phone Number");

        ST_FATHERS_NAME.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_FATHERS_NAME.setForeground(new java.awt.Color(255, 255, 255));
        ST_FATHERS_NAME.setText("Father's Name");

        ST_MOTHERS_NAME.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_MOTHERS_NAME.setForeground(new java.awt.Color(255, 255, 255));
        ST_MOTHERS_NAME.setText("Mother's Name");

        ST_GUARDIANS_PHONE_NUM.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_GUARDIANS_PHONE_NUM.setForeground(new java.awt.Color(255, 255, 255));
        ST_GUARDIANS_PHONE_NUM.setText("Guardian's Phone No.");

        ST_ADDRESS.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        ST_ADDRESS.setForeground(new java.awt.Color(255, 255, 255));
        ST_ADDRESS.setText("Address");

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
                            .addComponent(ST_Address))))
                .addContainerGap())
        );
        ST_LeftPanelLayout.setVerticalGroup(
            ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ST_LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGap(32, 32, 32)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ST_LeftPanelLayout.createSequentialGroup()
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ST_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ST_STUDENT_ID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ST_StudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ST_STUDENT_NAME))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ST_DateOfBirth, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ST_BIRTHDAY))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_Gender, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_GENDER))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_EMAIL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_PhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_PHONE_NUM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_FathersName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_FATHERS_NAME))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_MothersName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_MOTHERS_NAME))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_GuardiansPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_GUARDIANS_PHONE_NUM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ST_LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ST_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ST_ADDRESS))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ST_RightPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_RightPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        ST_RightPanel.setPreferredSize(new java.awt.Dimension(460, 703));

        ST_SearchStudentPanel.setBackground(new java.awt.Color(0, 30, 58));
        ST_SearchStudentPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(189, 216, 233), 4, true));
        ST_SearchStudentPanel.setPreferredSize(new java.awt.Dimension(400, 119));

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
                "Student ID", "Student Name", "Date of Birth", "Gender", "Email", "Phone Number", "Father's Name", "Mother's Name", "Guardian's Phone No.", "Address"
            }
        ));
        
        // Configure table selection behavior
        ST_Table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ST_Table.setRowSelectionAllowed(true);
        ST_Table.setColumnSelectionAllowed(false);
        
        // Add selection listener to populate form when row is clicked
        ST_Table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && ST_Table.getSelectedRow() >= 0) {
                populateStudentFormFromTable(ST_Table.getSelectedRow());
            }
        });
        
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
                .addComponent(ST_TableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
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

        MainTabPanel.addTab("Course", CourseTab);

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

        MarkSheetTab.setBackground(new java.awt.Color(31, 58, 95));

        javax.swing.GroupLayout MarkSheetTabLayout = new javax.swing.GroupLayout(MarkSheetTab);
        MarkSheetTab.setLayout(MarkSheetTabLayout);
        MarkSheetTabLayout.setHorizontalGroup(
            MarkSheetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1520, Short.MAX_VALUE)
        );
        MarkSheetTabLayout.setVerticalGroup(
            MarkSheetTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );

        MainTabPanel.addTab("Mark Sheet", MarkSheetTab);

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
            .addComponent(MainScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
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
                studentFileSaver.save(studentFilePath, students);
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
        if (ST_StudentID.getText().trim().isEmpty() || 
            ST_StudentName.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student ID and Name are required!", 
                "Validation Error", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentID = ST_StudentID.getText().trim();
        
        // Check for duplicate student ID
        for (Student s : students) {
            if (s.getStudentID().equals(studentID)) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Student ID already exists!", 
                    "Duplicate Error", 
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

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
            
            // Use defaults for fields not in the form
            String yearLevel = "";
            String studentType = "";
            java.util.ArrayList<String> subjectsEnrolled = new java.util.ArrayList<>();
            double gwa = 0.0;
            
            // Create new Student object
            Student newStudent = new Student(
                studentID, name, age, dob, yearLevel, studentType,
                subjectsEnrolled, gwa, email, phoneNumber, gender,
                address, fathersName, mothersName, guardiansPhone
            );
            
            // Add to ArrayList
            students.add(newStudent);
            
            // Save to file
            studentFileSaver.save(studentFilePath, students);
            
            // Refresh table
            loadStudentTableData();
            
            // Clear form
            clearStudentForm();
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student added successfully!", 
                "Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
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
            
            // Find the student in the list and preserve their existing data for fields not in form
            String yearLevel = "";
            String studentType = "";
            java.util.ArrayList<String> subjectsEnrolled = new java.util.ArrayList<>();
            double gwa = 0.0;
            
            for (Student s : students) {
                if (s.getStudentID().equals(originalID)) {
                    yearLevel = s.getYearLevel();
                    studentType = s.getStudentType();
                    subjectsEnrolled = s.getSubjectsEnrolled();
                    gwa = s.getGwa();
                    break;
                }
            }
            
            // Create updated Student object
            Student updatedStudent = new Student(
                newID, name, age, dob, yearLevel, studentType,
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
            studentFileSaver.save(studentFilePath, students);
            
            // Refresh table
            loadStudentTableData();
            
            // Clear form
            clearStudentForm();
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Student updated successfully!", 
                "Success", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
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
        ST_StudentID.setText("");
        ST_StudentName.setText("");
        ST_Email.setText("");
        ST_PhoneNumber.setText("");
        ST_Address.setText("");
        ST_FathersName.setText("");
        ST_MothersName.setText("");
        ST_GuardiansPhoneNumber.setText("");
        ST_Gender.setSelectedIndex(0);
        ST_DateOfBirth.setValue(new java.util.Date());
        ST_Table.clearSelection();
    }

    private void populateStudentFormFromTable(int rowIndex) {
        try {
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
            }
        } catch (Exception e) {
            logger.warning("Error populating form from table: " + e.getMessage());
        }
    }

    private void CT_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        // You can add your saving logic here
        // For now, just a popup message
        javax.swing.JOptionPane.showMessageDialog(this, "Course details have been saved!", "Save", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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
    // End of variables declaration                   
}