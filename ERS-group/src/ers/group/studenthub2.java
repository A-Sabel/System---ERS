package ers.group;

/**
 *
 * @author fedoc
 */
public class studenthub2 extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(studenthub2.class.getName());

    /**
     * Creates new form studenthub2
     */
    public studenthub2() {
    initComponents();

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

        javax.swing.GroupLayout studentTablePanelLayout = new javax.swing.GroupLayout(studentTablePanel);
        studentTablePanel.setLayout(studentTablePanelLayout);
        studentTablePanelLayout.setHorizontalGroup(
            studentTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1314, Short.MAX_VALUE)
        );
        studentTablePanelLayout.setVerticalGroup(
            studentTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

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

        semesterSearchField.setBackground(new java.awt.Color(146, 190, 219));

        studentIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        studentIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        studentIDLabel.setText("Student ID");

        searchSemesterButton.setBackground(new java.awt.Color(189, 216, 233));
        searchSemesterButton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        searchSemesterButton.setText("Search");

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
                {"08:00 - 09:00", null, null, null, null, null, null},
                {"09:00 - 10:00", null, null, null, null, null, null},
                {"10:00 - 11:00", null, null, null, null, null, null},
                {"11:00 - 12:00", null, null, null, null, null, null},
                {"12:00 - 01:00", null, null, null, null, null, null},
                {"01:00 - 02:00", null, null, null, null, null, null},
                {"02:00 - 03:00", null, null, null, null, null, null},
                {"03:00 - 04:00", null, null, null, null, null, null},
                {"04:00 - 05:00", null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
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

        pack();
    }// </editor-fold>                        

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

private void monthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateMonthYearLabel();
    }

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        updateMonthYearLabel();
    }
                                    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        java.awt.EventQueue.invokeLater(() -> new studenthub2().setVisible(true));
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
