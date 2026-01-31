import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentManagementSystem extends JFrame {

    Color darkBlue = new Color(0, 30, 58);
    Color panelBlue = new Color(0, 45, 90);
    Color fieldBlue = new Color(146, 190, 219);

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ===== HEADER =====
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 1200, 60);
        header.setBackground(darkBlue);

        JLabel title = new JLabel("Student Management System");
        title.setBounds(20, 10, 600, 40);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title);

        add(header);

        // ===== TABS =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBounds(20, 80, 1150, 520);

        tabs.add("Student", studentPanel());
        tabs.add("Course", coursePanel());
        tabs.add("Score", new JPanel());
        tabs.add("Mark Sheet", new JPanel());
        tabs.add("Schedule", new JPanel());

        add(tabs);
    }

    // ================= STUDENT PANEL =================
    private JPanel studentPanel() {

        JPanel panel = new JPanel(null);
        panel.setBackground(panelBlue);

        String[] labels = {
                "Student ID", "Student Name", "Date of Birth", "Gender",
                "Email", "Phone Number", "Father's Name",
                "Mother's Name", "Guardian's Phone No.", "Address"
        };

        int y = 20;
        for (String text : labels) {
            JLabel lbl = new JLabel(text);
            lbl.setBounds(20, y, 150, 25);
            lbl.setForeground(Color.WHITE);
            panel.add(lbl);

            if (text.equals("Gender")) {
                JComboBox<String> cb = new JComboBox<>(new String[]{"Male", "Female"});
                cb.setBounds(180, y, 180, 25);
                panel.add(cb);
            } else {
                JTextField tf = new JTextField();
                tf.setBounds(180, y, 180, 25);
                tf.setBackground(fieldBlue);
                panel.add(tf);
            }
            y += 35;
        }

        // TABLE
        String[] columns = {
                "Student ID", "Student Name", "Date of Birth",
                "Gender", "Email", "Phone", "Father", "Mother"
        };

        JTable table = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(420, 60, 700, 350);
        panel.add(scroll);

        return panel;
    }

    // ================= COURSE PANEL =================
    private JPanel coursePanel() {

        JPanel panel = new JPanel(null);
        panel.setBackground(panelBlue);

        // SEARCH PANEL
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(20, 20, 400, 80);
        searchPanel.setBackground(darkBlue);
        panel.add(searchPanel);

        JLabel lblSearchId = new JLabel("Student ID");
        lblSearchId.setBounds(20, 20, 100, 25);
        lblSearchId.setForeground(Color.WHITE);
        searchPanel.add(lblSearchId);

        JTextField txtSearchId = new JTextField();
        txtSearchId.setBounds(120, 20, 150, 25);
        searchPanel.add(txtSearchId);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(280, 20, 90, 25);
        searchPanel.add(btnSearch);

        // FORM
        String[] labels = {
                "ID", "Student ID", "Semester",
                "Course 1", "Course 2", "Course 3",
                "Course 4", "Course 5"
        };

        int y = 120;
        for (String text : labels) {
            JLabel lbl = new JLabel(text);
            lbl.setBounds(20, y, 120, 25);
            lbl.setForeground(Color.WHITE);
            panel.add(lbl);

            if (text.equals("Semester") || text.startsWith("Course")) {
                JComboBox<String> cb = new JComboBox<>(new String[]{"Item 1", "Item 2", "Item 3"});
                cb.setBounds(140, y, 200, 25);
                panel.add(cb);
            } else {
                JTextField tf = new JTextField();
                tf.setBounds(140, y, 200, 25);
                tf.setBackground(fieldBlue);
                panel.add(tf);
            }
            y += 40;
        }

        // TABLE
        String[] columns = {
                "Student ID", "Student Name", "Semester",
                "Course 1", "Course 2", "Course 3",
                "Course 4", "Course 5"
        };

        JTable table = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(420, 70, 700, 330);
        panel.add(scroll);

        return panel;
    }

    public static void main(String[] args) {
        new StudentManagementSystem().setVisible(true);
    }
}
