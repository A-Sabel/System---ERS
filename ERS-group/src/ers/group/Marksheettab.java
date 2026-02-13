package ers.group;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


/**
 *
 * @author fedoc
 */
public class Marksheettab extends javax.swing.JPanel {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Marksheettab.class.getName());
    private List<Marksheet> marksheets;
    private MarksheetFileLoader marksheetFileLoader;
    private MarksheetFileSaver marksheetFileSaver;
    private String marksheetFilePath;
    private CourseSubjectFileLoader courseFileLoader;
    private java.util.Map<String, String> courseNameMap;


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

    private void loadMarksheetTableData() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();
        model.setRowCount(0); // Clear existing rows
        for (Marksheet m : marksheets) {
            String[] subjects = m.getSubjects();
            double[] marks = m.getMarks();
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
            Object[] row = new Object[14];
            row[0] = "MRK-" + (marksheets.indexOf(m) + 1);
            row[1] = m.getStudentID();
            row[2] = m.getSemester();
            for (int i = 0; i < 5; i++) {
                String courseID = subjects[i] != null ? subjects[i] : "";
                row[3 + (i * 2)] = getCourseName(courseID);
                row[4 + (i * 2)] = marks[i] > 0 ? marks[i] : "";
            }
            row[13] = String.format("%.2f", average);
            model.addRow(row);
        }
    }

    private void SearchbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        String searchID = SearchbarID.getText().trim();
        if (searchID.isEmpty()) {
            loadMarksheetTableData();
            GWA.setText("GWA: --");
            return;
        }
        // Find marksheet by student ID
        for (Marksheet m : marksheets) {
            if (m.getStudentID().equalsIgnoreCase(searchID)) {
                // Update table to show only this marksheet
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) scoretable.getModel();
                model.setRowCount(0);
                String[] subjects = m.getSubjects();
                double[] marks = m.getMarks();
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
                Object[] row = new Object[14];
                row[0] = "MRK-001";
                row[1] = m.getStudentID();
                row[2] = m.getSemester();
                for (int i = 0; i < 5; i++) {
                    String courseID = subjects[i] != null ? subjects[i] : "";
                    row[3 + (i * 2)] = getCourseName(courseID);
                    row[4 + (i * 2)] = marks[i] > 0 ? marks[i] : "";
                }
                row[13] = String.format("%.2f", average);
                model.addRow(row);
                GWA.setText(String.format("GWA: %.2f", average));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Student not found!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
    }

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
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Student ID", "Semester", "Course 1", "Score 1", "Course 2", "Score 2", "Course 3", "Score 3", "Course 4", "Score 4", "Course 5", "Score 5", "Average"
            }
        ));
        scoretable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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
    }                 

    private void printbuttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
    try {
        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
        // Look for Microsoft Print to PDF
        javax.print.PrintService[] services =
                javax.print.PrintServiceLookup.lookupPrintServices(null, null);
        javax.print.PrintService pdfPrinter = null;
        for (javax.print.PrintService service : services) {
            if (service.getName().equalsIgnoreCase("Microsoft Print to PDF")) {
                pdfPrinter = service;
                break;
            }
        }
        if (pdfPrinter == null) {
            JOptionPane.showMessageDialog(this,
                    "Microsoft Print to PDF printer not found!",
                    "Printer Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        job.setPrintService(pdfPrinter);
        job.setPrintable(scoretable.getPrintable(
                javax.swing.JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat("Marksheet"),
                new java.text.MessageFormat("Page {0}")
        ));
        job.print();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error printing: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

    private void clearbuttonActionPerformed(java.awt.event.ActionEvent evt) {
    // Clear search bar
    SearchbarID.setText("");
    // Reset GWA label
    GWA.setText("GWA: --");
    // Clear table data
    javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) scoretable.getModel();
    for (int row = 0; row < model.getRowCount(); row++) {
        for (int col = 0; col < model.getColumnCount(); col++) {
            model.setValueAt(null, row, col);
        }
    }
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



