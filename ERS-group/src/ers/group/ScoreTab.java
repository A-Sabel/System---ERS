package ers.group;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ScoreTab extends JPanel {

    private JTextField studentIdField, studentNameField, schoolYearField, semesterField;
    private JComboBox<String>[] courseDropdowns = new JComboBox[5];
    private JTextField[] courseScores = new JTextField[5];
    private JTable table;
    private DefaultTableModel model;

    private final String[] courseOptions = {
            "Programming 1","Computer Fundamentals","Discrete Mathematics","Introduction to IT Systems",
            "Ethics in Computing","Programming 2","Object-Oriented Programming","Web Technologies",
            "Linear Algebra for Computing","Human-Computer Interaction",
            "Data Structures and Algorithms","Design and Analysis of Algorithms",
            "Database Management Systems","Computer Networks",
            "Advanced Programming Concepts","Operating Systems",
            "Compiler Design","Information Security",
            "Distributed Systems","Systems Integration Project"
    };

    public ScoreTab() {
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(11,29,56));

        // Create left and right panel containers
        add(createLeftPanel(), BorderLayout.WEST);
        add(createRightContainer(), BorderLayout.CENTER);
    }

    // ================= LEFT PANEL =================
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(11,29,56));
        leftPanel.setPreferredSize(new Dimension(350,600));
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2)); // black border

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Color fieldBg = new Color(139,169,201);

        studentIdField = addField(leftPanel,"Student ID",labelFont,fieldBg);
        studentNameField = addField(leftPanel,"Student Name",labelFont,fieldBg);
        schoolYearField = addField(leftPanel,"Year Level",labelFont,fieldBg);
        semesterField = addField(leftPanel,"Semester",labelFont,fieldBg);

        for(int i=0;i<5;i++){
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
            row.setBackground(new Color(11,29,56));

            JLabel label = new JLabel("Course "+(i+1));
            label.setForeground(Color.WHITE);
            label.setFont(labelFont);
            label.setPreferredSize(new Dimension(120,32));

            JComboBox<String> box = new JComboBox<>(courseOptions);
            box.setPreferredSize(new Dimension(220,32));
            box.setBackground(fieldBg);

            JTextField grade = new JTextField();
            grade.setPreferredSize(new Dimension(60,32));
            grade.setBackground(fieldBg);
            grade.setFont(new Font("Segoe UI", Font.BOLD,14));

            row.add(label); row.add(box); row.add(grade);

            courseDropdowns[i]=box;
            courseScores[i]=grade;

            leftPanel.add(row);
        }

        return leftPanel;
    }

    private JTextField addField(JPanel panel,String text,Font font,Color bg){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        p.setBackground(new Color(11,29,56));
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(font);
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(250,32));
        tf.setBackground(bg);
        tf.setFont(new Font("Segoe UI", Font.BOLD,14));
        p.add(l); p.add(tf);
        panel.add(p);
        return tf;
    }

    // ================= RIGHT CONTAINER =================
    private JPanel createRightContainer() {
        JPanel rightContainer = new JPanel(new BorderLayout(10,10));
        rightContainer.setBackground(new Color(11,29,56));

        rightContainer.add(createRightTopPanel(), BorderLayout.NORTH);
        rightContainer.add(createTablePanel(), BorderLayout.CENTER);
        rightContainer.add(createBottomButtonPanel(), BorderLayout.SOUTH);

        return rightContainer;
    }

    private JPanel createRightTopPanel() {
        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        rightTop.setBackground(new Color(11,29,56));
        rightTop.setBorder(BorderFactory.createLineBorder(Color.BLACK,2)); // black border

        JTextField sId = makeField(150);
        JTextField sSem = makeField(100);
        JTextField sYear = makeField(100);

        JButton searchBtn = makeButton("Search");
        JButton refreshBtn = makeButton("Refresh");

        rightTop.add(label("ID")); rightTop.add(sId);
        rightTop.add(label("Semester")); rightTop.add(sSem);
        rightTop.add(label("Year")); rightTop.add(sYear);
        rightTop.add(searchBtn); rightTop.add(refreshBtn);

        return rightTop;
    }

    private JScrollPane createTablePanel() {
        String[] cols = {
                "ID","Name","Year","Sem",
                "C1","G1","P1","C2","G2","P2",
                "C3","G3","P3","C4","G4","P4",
                "C5","G5","P5","GPA"
        };
        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(new Color(139,169,201));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD,14));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK,2)); // black border around table panel
        return scroll;
    }

    private JPanel createBottomButtonPanel() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
        bottom.setBackground(new Color(11,29,56));
        bottom.setBorder(BorderFactory.createLineBorder(Color.BLACK,2)); // black border

        JButton save = makeButton("Save");
        JButton update = makeButton("Update");
        JButton print = makeButton("Print");
        JButton clear = makeButton("Clear");
        JButton logout = makeButton("Logout");

        bottom.add(save); bottom.add(update); bottom.add(print); bottom.add(clear); bottom.add(logout);
        return bottom;
    }

    private JButton makeButton(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD,18));
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(139,169,201));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        btn.setPreferredSize(new Dimension(120,45));
        return btn;
    }

    private JTextField makeField(int w){
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(w,32));
        t.setBackground(new Color(139,169,201));
        t.setFont(new Font("Segoe UI", Font.BOLD,14));
        return t;
    }

    private JLabel label(String t){
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD,16));
        return l;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Score Tab Styled Panels");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200,800);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ScoreTab());
            frame.setVisible(true);
        });
    }
}