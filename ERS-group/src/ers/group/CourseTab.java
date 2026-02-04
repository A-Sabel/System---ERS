package ers.group;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CourseTab extends JPanel {

    public CourseTab() {
        initComponents();
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

    private void CT_SearchActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void CT_idActionPerformed(java.awt.event.ActionEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void CT_StudentIDActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }

    private void CT_SaveActionPerformed(java.awt.event.ActionEvent evt) {
        // You can add your saving logic here
        // For now, just a popup message
        javax.swing.JOptionPane.showMessageDialog(this, "Course details have been saved!", "Save", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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