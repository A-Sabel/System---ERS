package ers.group;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ScoreTab extends JPanel {

    private JTextField studentIdField;
    private JComboBox<String> semesterField;

    @SuppressWarnings("unchecked")
    private JComboBox<String>[] courseDropdowns = new JComboBox[5];
    private JTextField[] courseScores = new JTextField[5];

    private JTextField searchIdField;
    private JComboBox<String> searchSemField;
    private JTable table;
    private DefaultTableModel model;

    private JButton saveBtn, clearBtn, updateBtn;

    private final Map<String, String> courseMap = new LinkedHashMap<>();
    private final String[] semesters = {"1st Semester", "2nd Semester", "3rd Semester", "4th Semester"};

    private final String[] courseOptions = {
            "Programming 1", "Computer Fundamentals", "Discrete Mathematics", "Introduction to IT Systems",
            "Ethics in Computing", "Programming 2", "Object-Oriented Programming", "Web Technologies",
            "Linear Algebra for Computing", "Human-Computer Interaction",
            "Data Structures and Algorithms", "Design and Analysis of Algorithms",
            "Database Management Systems", "Computer Networks",
            "Advanced Programming Concepts", "Operating Systems",
            "Compiler Design", "Information Security",
            "Distributed Systems", "Systems Integration Project"
    };

    private final String[] courseCodes = {
            "CS101", "CS102", "CS103", "CS104", "CS105",
            "CS201", "CS202", "CS203", "CS204", "CS205",
            "CS301", "CS302", "CS303", "CS304", "CS305",
            "CS401", "CS402", "CS403", "CS404", "CS405"
    };

    private String getMarksheetPath() {
        return FilePathResolver.resolveMarksheetFilePath();
    }

    public ScoreTab() {
        setLayout(new BorderLayout());
        setBackground(new Color(31, 58, 95));

        initCourseMap();

        add(createTopSpacer(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadAllRecordsToTable();
    }

    private void initCourseMap() {
        for (int i = 0; i < courseCodes.length; i++) {
            courseMap.put(courseCodes[i], courseOptions[i]);
        }
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
        semesterField = addComboAbove(left, "Semester", semesters, labelFont, fieldBg);

        for (int i = 0; i < 5; i++) {
            JPanel row = new JPanel(new GridBagLayout());
            row.setBackground(new Color(0, 30, 58));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 5, 3, 5);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel label = new JLabel("Course " + (i + 1));
            label.setForeground(Color.WHITE);
            label.setFont(labelFont);

            JComboBox<String> box = new JComboBox<>(courseOptions);
            box.setPreferredSize(new Dimension(180, 30));
            box.setBackground(fieldBg);

            JTextField score = new JTextField("0.00");
            score.setPreferredSize(new Dimension(60, 30));
            score.setBackground(fieldBg);
            score.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            gbc.gridx = 0; gbc.gridy = 0; row.add(label, gbc);
            gbc.gridx = 0; gbc.gridy = 1; row.add(box, gbc);
            gbc.gridx = 1; gbc.gridy = 1; row.add(score, gbc);

            courseDropdowns[i] = box;
            courseScores[i] = score;
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
        JButton searchBtn = new StyledButton("Search");
        searchBtn.setPreferredSize(new Dimension(110, 38));
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchBtn.addActionListener(e -> searchRecord());
        search.add(label("ID:"));
        search.add(searchIdField);
        search.add(label("Sem:"));
        search.add(searchSemField);
        search.add(searchBtn);
        return search;
    }

    // ================= TABLE CUSTOMIZATION FOR PHOTO MATCH =================
    private JScrollPane createTablePanel() {
        String[] cols = {"ID", "Student ID", "Semester", "Course 1", "Score 1", "Course 2", "Score 2", 
                        "Course 3", "Score 3", "Course 4", "Score 4", "Course 5", "Score 5", "GPA"};

        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // CHANGE 1: Enable Auto-Resize so it fits the frame width
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        
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
        int[] minWidths = {50, 70, 90, 110, 45, 110, 45, 110, 45, 110, 45, 110, 45, 40};
        for(int i = 0; i < minWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(minWidths[i]);
            if (i % 2 == 0 && i > 3) { // Score columns and GPA
                table.getColumnModel().getColumn(i).setMinWidth(40);
            }
        }

        JScrollPane scroll = new JScrollPane(table);
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
        saveBtn.addActionListener(e -> saveRecord());
        clearBtn.addActionListener(e -> clearFields());
        updateBtn.addActionListener(e -> updateRecord());
        bottom.add(saveBtn);
        bottom.add(clearBtn);
        bottom.add(updateBtn);
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
        try(BufferedReader br = new BufferedReader(new FileReader(getMarksheetPath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 13) continue;
                Object[] row = new Object[14];
                row[0] = d[0]; row[1] = d[1]; row[2] = d[2];
                for (int i = 0; i < 5; i++) {
                    row[3 + i * 2] = courseMap.getOrDefault(d[3 + i * 2], d[3 + i * 2]);
                    row[4 + i * 2] = d[4 + i * 2];
                }
                row[13] = (d.length > 13) ? d[13] : "";
                model.addRow(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void searchRecord() {
        String id = searchIdField.getText().trim();
        String sem = searchSemField.getSelectedItem().toString();
        model.setRowCount(0);

        try(BufferedReader br = new BufferedReader(new FileReader(getMarksheetPath()))) {
            String line;
            boolean found = false;
            while((line = br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length<13) continue;
                if(d[1].equals(id) && d[2].equals(sem)){
                    Object[] row = new Object[14];
                    row[0] = d[0];
                    row[1] = d[1];
                    row[2] = d[2];
                    for(int i=0;i<5;i++){
                        row[3 + i*2] = courseMap.getOrDefault(d[3 + i*2], d[3 + i*2]);
                        row[4 + i*2] = d[4 + i*2];
                        courseDropdowns[i].setSelectedItem(courseMap.getOrDefault(d[3 + i*2], d[3 + i*2]));
                        courseScores[i].setText(d[4 + i*2]);
                    }
                    semesterField.setSelectedItem(d[2]);
                    studentIdField.setText(d[1]);
                    model.addRow(row);
                    found = true;
                    break;
                }
            }
            if(!found) JOptionPane.showMessageDialog(this,"Record not found");
        } catch(Exception e){ e.printStackTrace();}
    }
    
    private void clearFields() {
        studentIdField.setText("");
        semesterField.setSelectedIndex(0);
        for (int i = 0; i < 5; i++) {
            courseDropdowns[i].setSelectedIndex(0);
            courseScores[i].setText("0.00");
        }
        searchIdField.setText("");
        loadAllRecordsToTable();
    }

    private void saveRecord() {
        String id = studentIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            studentIdField.requestFocus();
            return;
        }

        try(PrintWriter pw = new PrintWriter(new FileWriter(getMarksheetPath(),true))){
            String nextMRK = getNextMRK();
            String sem = semesterField.getSelectedItem().toString();
            StringBuilder sb = new StringBuilder();
            sb.append(nextMRK).append(",").append(id).append(",").append(sem);

            for(int i=0;i<5;i++){
                sb.append(",").append(getCourseCode(courseDropdowns[i].getSelectedItem().toString()));
                sb.append(",").append(courseScores[i].getText().trim());
            }

            double gpa = calculateGPA();
            sb.append(",").append(String.format("%.2f",gpa));
            pw.println(sb.toString());
        } catch(Exception e){ e.printStackTrace(); }
        loadAllRecordsToTable();
        
        // Update student's overall GWA in student.txt
        updateStudentGWA(studentIdField.getText().trim());
        
        JOptionPane.showMessageDialog(this,"Record Saved!");
    }

    private void updateRecord() {
        try {
            File inputFile = new File(getMarksheetPath());
            File tempFile = new File("temp_marksheet.txt");
            boolean updated = false;

            try(BufferedReader br = new BufferedReader(new FileReader(inputFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {

                String line;
                while((line=br.readLine())!=null){
                    String[] d = line.split(",");
                    if(d.length<13){
                        pw.println(line);
                        continue;
                    }
                    if(d[1].equals(studentIdField.getText().trim()) &&
                       d[2].equals(semesterField.getSelectedItem().toString())) {

                        StringBuilder sb = new StringBuilder();
                        sb.append(d[0]).append(",").append(d[1]).append(",").append(d[2]);
                        for(int i=0;i<5;i++){
                            sb.append(",").append(getCourseCode(courseDropdowns[i].getSelectedItem().toString()));
                            sb.append(",").append(courseScores[i].getText().trim());
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
                JOptionPane.showMessageDialog(this,"Record Updated!");
            }
            else JOptionPane.showMessageDialog(this,"Record not found");
            loadAllRecordsToTable();
        } catch (Exception e) { e.printStackTrace(); }
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

    private double calculateGPA(){
        double sum=0; int count=0;
        for(JTextField tf : courseScores){
            try { sum+=Double.parseDouble(tf.getText().trim()); count++;} catch(Exception ignored){}
        }
        return count>0 ? sum/count : 0;
    }

    private String getCourseCode(String name) {
        for (Map.Entry<String, String> e : courseMap.entrySet())
            if (e.getValue().equals(name)) return e.getKey();
        return name;
    }

    /**
     * Calculate the overall GWA for a student across all their marksheet records
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
                        double semesterGPA = Double.parseDouble(parts[13]);
                        totalGPA += semesterGPA;
                        semesterCount++;
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