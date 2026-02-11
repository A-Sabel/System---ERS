/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package ers.group;

import java.util.*;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author fedoc
 */
public class Marksheettab extends javax.swing.JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Marksheettab.class.getName());
    private Map<String, Marksheet> marksheetMap;
    private List<Marksheet> allMarksheets;
    private MarksheetFileLoader marksheetFileLoader;
    private StudentFileLoader studentFileLoader;
    private Map<String, String> studentNameMap;
    private boolean controlPanelVisible = true;

    /**
     * Creates new form Marksheettab
     */
    public Marksheettab() {
        initComponents();
        jPanel1.setVisible(true);  // Ensure buttons are visible
        loadData();
        Searchbutton.addActionListener(e -> searchStudent());
    }
    
    private void loadData() {
        try {
            // Load marksheet data
            String[] possibleMarksheetPaths = {
                "ERS-group/src/ers/group/master files/marksheet.txt",
                "src/ers/group/master files/marksheet.txt",
                "master files/marksheet.txt",
                "marksheet.txt"
            };
            
            String marksheetPath = null;
            for (String path : possibleMarksheetPaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    marksheetPath = path;
                    break;
                }
            }
            
            if (marksheetPath != null) {
                marksheetFileLoader = new MarksheetFileLoader();
                marksheetFileLoader.load(marksheetPath);
                allMarksheets = new ArrayList<>(marksheetFileLoader.getAllMarksheets());
                marksheetMap = marksheetFileLoader.getMarksheetMap();
                logger.info("Loaded " + allMarksheets.size() + " marksheets");
            }
            
            // Load student names for lookup
            String[] possibleStudentPaths = {
                "ERS-group/src/ers/group/master files/student.txt",
                "src/ers/group/master files/student.txt",
                "master files/student.txt",
                "student.txt"
            };
            
            studentNameMap = new HashMap<>();
            for (String path : possibleStudentPaths) {
                java.io.File f = new java.io.File(path);
                if (f.exists()) {
                    studentFileLoader = new StudentFileLoader();
                    studentFileLoader.load(path);
                    Collection<Student> students = studentFileLoader.getAllStudents();
                    for (Student s : students) {
                        studentNameMap.put(s.getStudentID(), s.getStudentName());
                    }
                    logger.info("Loaded " + students.size() + " students for name mapping");
                    break;
                }
            }
            
        } catch (Exception e) {
            logger.severe("Error loading data: " + e.getMessage());
        }
    }
    
    private void searchStudent() {
        String searchID = Searchbar.getText().trim();
        if (searchID.isEmpty()) {
            clearTable();
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) scoretable.getModel();
        model.setRowCount(0);
        
        // Find all marksheets for the student
        int rowCount = 0;
        for (Marksheet marksheet : allMarksheets) {
            if (marksheet.getStudentID().equalsIgnoreCase(searchID)) {
                String studentName = studentNameMap.getOrDefault(marksheet.getStudentID(), "N/A");
                String[] subjects = marksheet.getSubjects();
                double[] marks = marksheet.getMarks();
                
                double average = 0;
                for (double mark : marks) {
                    average += mark;
                }
                average /= marks.length;
                
                model.addRow(new Object[]{
                    rowCount + 1,
                    marksheet.getStudentID(),
                    marksheet.getSemester(),
                    subjects[0], marks[0],
                    subjects[1], marks[1],
                    subjects[2], marks[2],
                    subjects[3], marks[3],
                    subjects[4], marks[4],
                    String.format("%.2f", average)
                });
                rowCount++;
                
                // Update GWA label with the calculated average
                if (rowCount == 1) {
                    GWA.setText("GWA: " + String.format("%.2f", average));
                }
            }
        }
        
        if (rowCount == 0) {
            GWA.setText("GWA: N/A");
        }
    }
    
    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) scoretable.getModel();
        model.setRowCount(0);
        Searchbar.setText("");
        GWA.setText("GWA: N/A");
    }
    
    public javax.swing.JPanel getControlPanel() {
        return jPanel1;
    }
    
    public void setControlPanelVisible(boolean visible) {
        controlPanelVisible = visible;
        if (!visible) {
            jPanel1.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        Background = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        StudentID = new javax.swing.JLabel();
        Searchbar = new javax.swing.JTextField();
        Searchbutton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        GWA = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scoretable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        printbutton = new javax.swing.JButton();
        clearbutton = new javax.swing.JButton();
        logoutbutton = new javax.swing.JButton();

        Background.setBackground(new java.awt.Color(31, 58, 95));

        jPanel15.setBackground(new java.awt.Color(0, 30, 58));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

        jPanel16.setBackground(new java.awt.Color(0, 30, 58));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        StudentID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        StudentID.setForeground(new java.awt.Color(255, 255, 255));
        StudentID.setText("Student ID");

        Searchbar.setBackground(new java.awt.Color(146, 190, 219));

        Searchbutton.setBackground(new java.awt.Color(189, 216, 233));
        Searchbutton.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        Searchbutton.setText("Search");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(Searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(Searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(StudentID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Searchbutton))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 30, 58));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(189, 216, 233), 4));

        GWA.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        GWA.setForeground(new java.awt.Color(255, 255, 255));
        GWA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GWA.setText("GWA. 3.53");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(GWA, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(GWA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 30, 58));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));

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

        jPanel1.setBackground(new java.awt.Color(0, 30, 58));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));

        printbutton.setBackground(new java.awt.Color(73, 118, 159));
        printbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        printbutton.setText("Print");
        printbutton.addActionListener(this::printbuttonActionPerformed);

        clearbutton.setBackground(new java.awt.Color(73, 118, 159));
        clearbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        clearbutton.setText("Clear");
        clearbutton.addActionListener(this::clearbuttonActionPerformed);

        logoutbutton.setBackground(new java.awt.Color(73, 118, 159));
        logoutbutton.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        logoutbutton.setText("Logout");
        logoutbutton.addActionListener(this::logoutbuttonActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(clearbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(logoutbutton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1314, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        BackgroundLayout.setVerticalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void printbuttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        try {
            if (Searchbar.getText().trim().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please search for a student first!", "Export", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Build a simple HTML representation of the table and GWA
            String studentId = Searchbar.getText().trim();
            String gwaText = GWA.getText();

            StringBuilder html = new StringBuilder();
            html.append("<html><head><meta charset=\"utf-8\"></head><body>");
            html.append("<h2>Student Marksheet</h2>");
            html.append("<p><strong>Student ID:</strong> ").append(escapeHtml(studentId)).append("</p>");
            html.append("<p><strong>").append(escapeHtml(gwaText)).append("</strong></p>");

            html.append("<table border=\"1\" cellpadding=\"6\" cellspacing=\"0\">\n");
            // header
            javax.swing.table.TableModel model = scoretable.getModel();
            html.append("<tr>");
            for (int c = 0; c < model.getColumnCount(); c++) {
                html.append("<th>").append(escapeHtml(String.valueOf(model.getColumnName(c)))).append("</th>");
            }
            html.append("</tr>\n");

            // rows
            for (int r = 0; r < model.getRowCount(); r++) {
                // skip entirely-empty rows
                boolean emptyRow = true;
                for (int c = 0; c < model.getColumnCount(); c++) {
                    Object val = model.getValueAt(r, c);
                    if (val != null && !String.valueOf(val).trim().isEmpty()) { emptyRow = false; break; }
                }
                if (emptyRow) continue;

                html.append("<tr>");
                for (int c = 0; c < model.getColumnCount(); c++) {
                    Object val = model.getValueAt(r, c);
                    html.append("<td>").append(escapeHtml(val == null ? "" : String.valueOf(val))).append("</td>");
                }
                html.append("</tr>\n");
            }

            html.append("</table>");
            html.append("</body></html>");

            // Determine Documents folder and filename
            String userDocs = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents";
            String timestamp = new SimpleDateFormat("yyyyMMdd_Hmmss").format(new Date());
            String fileName = "Marksheet_" + studentId + "_" + timestamp + ".doc";
            Path outPath = Paths.get(userDocs, fileName);

            Files.createDirectories(outPath.getParent());
            Files.write(outPath, html.toString().getBytes(StandardCharsets.UTF_8));

            javax.swing.JOptionPane.showMessageDialog(this, "Exported to " + outPath.toString(), "Export Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error exporting: " + e.getMessage(), "Export Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            logger.severe("Export error: " + e.getMessage());
        }
    }                                           

    // Simple HTML-escape helper
    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    private void clearbuttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        clearTable();
        javax.swing.JOptionPane.showMessageDialog(this, "Table cleared successfully!", "Clear", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }                                           

    private void logoutbuttonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Exit the application
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }
    }                                            


    // Variables declaration - do not modify                     
    private javax.swing.JPanel Background;
    private javax.swing.JLabel GWA;
    private javax.swing.JTextField Searchbar;
    private javax.swing.JButton Searchbutton;
    private javax.swing.JLabel StudentID;
    private javax.swing.JButton clearbutton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logoutbutton;
    private javax.swing.JButton printbutton;
    private javax.swing.JTable scoretable;
    // End of variables declaration
}
