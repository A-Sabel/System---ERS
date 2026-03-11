package ers.group;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Eli
 */
public class SignUp extends javax.swing.JDialog {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SignUp.class.getName());
    private JFrame loginFrame;

    public SignUp(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.loginFrame = (JFrame) parent;
        initComponents();
        setupFieldValidation();
    }

    private void setupFieldValidation() {
        final javax.swing.border.Border DEF = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1);
        final javax.swing.border.Border OK  = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(34, 139, 34), 2);
        final javax.swing.border.Border BAD = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 50, 50), 2);
        Email.setBorder(javax.swing.BorderFactory.createCompoundBorder(DEF, javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        Username.setBorder(javax.swing.BorderFactory.createCompoundBorder(DEF, javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (PasswdWrap != null) PasswdWrap.setBorder(DEF);
        });
        Email.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String v = Email.getText().trim();
                javax.swing.border.Border b = v.isEmpty() ? DEF : (v.contains("@") && v.contains(".") ? OK : BAD);
                Email.setBorder(javax.swing.BorderFactory.createCompoundBorder(b, javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
        Username.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                javax.swing.border.Border b = Username.getText().trim().isEmpty() ? DEF : OK;
                Username.setBorder(javax.swing.BorderFactory.createCompoundBorder(b, javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
        Password.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String p = new String(Password.getPassword()).trim();
                javax.swing.border.Border b = p.isEmpty() ? DEF : (p.length() >= 6 ? OK : BAD);
                if (PasswdWrap != null) PasswdWrap.setBorder(b);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
    }                        

    private void initComponents() {
        MainPanel = new javax.swing.JPanel();
        Username = new javax.swing.JTextField();
        Email = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        SignUp = new javax.swing.JButton();
        UsernameLabel = new javax.swing.JLabel();
        PasswordLabel = new javax.swing.JLabel();
        EmailLabel = new javax.swing.JLabel();


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sign Up");
        setModal(true);


        Username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N


        Email.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N


        Password.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Password.addActionListener(this::PasswordActionPerformed);

        ShowPasswd = new javax.swing.JButton() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                int cx = getWidth() / 2, cy = getHeight() / 2;
                boolean vis = Password.getEchoChar() == (char)0;
                g2.setColor(new java.awt.Color(150, 150, 150));
                g2.setStroke(new java.awt.BasicStroke(1.6f));
                g2.drawOval(cx - 8, cy - 5, 16, 10);
                g2.fillOval(cx - 3, cy - 3, 6, 6);
                if (vis) {
                    g2.setStroke(new java.awt.BasicStroke(2.0f));
                    g2.drawLine(cx - 10, cy + 8, cx + 10, cy - 8);
                }
                g2.dispose();
            }
        };
        ShowPasswd.setPreferredSize(new java.awt.Dimension(34, 0));
        ShowPasswd.setBackground(java.awt.Color.WHITE);
        ShowPasswd.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        ShowPasswd.setContentAreaFilled(false);
        ShowPasswd.setFocusPainted(false);
        ShowPasswd.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        ShowPasswd.setToolTipText("Show / hide password");
        ShowPasswd.addActionListener(e -> {
            Password.setEchoChar(Password.getEchoChar() != (char)0 ? (char)0 : '\u2022');
            ShowPasswd.repaint();
        });
        PasswdWrap = new javax.swing.JPanel(new java.awt.BorderLayout(0, 0));
        PasswdWrap.setBackground(java.awt.Color.WHITE);
        Password.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 0));
        PasswdWrap.add(Password, java.awt.BorderLayout.CENTER);
        PasswdWrap.add(ShowPasswd, java.awt.BorderLayout.EAST);

        SignUp.setBackground(new java.awt.Color(31, 58, 95));
        SignUp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SignUp.setForeground(new java.awt.Color(255, 255, 255));
        SignUp.setText("Sign Up");
        SignUp.addActionListener(this::SignUpActionPerformed);


        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UsernameLabel.setForeground(new java.awt.Color(77, 142, 162));
        UsernameLabel.setText("<html>Username: <font color='red'>*</font></html>");


        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PasswordLabel.setForeground(new java.awt.Color(77, 142, 162));
        PasswordLabel.setText("<html>Password: <font color='red'>*</font></html>");


        EmailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EmailLabel.setForeground(new java.awt.Color(77, 142, 162));
        EmailLabel.setText("<html>Email: <font color='red'>*</font></html>");


        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(PasswdWrap)
                        .addComponent(Username)
                        .addComponent(SignUp, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                        .addComponent(UsernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Email, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(EmailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
    MainPanelLayout.setVerticalGroup(
        MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MainPanelLayout.createSequentialGroup()
            .addContainerGap(43, Short.MAX_VALUE)
            .addComponent(EmailLabel) // Email Label
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Email Field
            .addGap(26, 26, 26)
            .addComponent(UsernameLabel) // Username Label
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Username Field
            .addGap(29, 29, 29)
            .addComponent(PasswordLabel) // Password Label
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(PasswdWrap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Password Field + Eye
            .addGap(20, 20, 20)
            .addComponent(SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(30, 30, 30))
    );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );


        pack();
    }// </editor-fold>                        

    private void PasswordActionPerformed(java.awt.event.ActionEvent evt) {
        // Event handler for password field (currently unused)
    }

    private void SignUpActionPerformed(java.awt.event.ActionEvent evt) {                                             
        String email = Email.getText().trim();
        String username = Username.getText().trim();
        String password = new String(Password.getPassword()).trim();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.",
                "Sign Up Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters.",
                "Sign Up Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Prepare the plain text line
            String plainLine = email + "," + username + "," + password;
            
            // 1. ENCRYPT the data string
            String encryptedLine = Encryption.encrypt(plainLine);

            // 2. Write the encrypted string to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FilePathResolver.resolveLoginFilePath(), true))) {
                bw.write(encryptedLine);
                bw.newLine();
            }

            JOptionPane.showMessageDialog(this, "Admin account created successfully!");

            // Open main system
            new StudentCourseTab().setVisible(true);
            this.dispose();

            // Close the login window
            if (loginFrame != null) {
                loginFrame.dispose();
            }

        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error saving user", e);
            JOptionPane.showMessageDialog(this, "Error saving account data.");
        }
    }                                    

    // Variables declaration - do not modify                    
    private javax.swing.JPanel MainPanel;
    private javax.swing.JTextField Email;
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JPasswordField Password;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JPanel PasswdWrap;
    private javax.swing.JButton ShowPasswd;
    private javax.swing.JButton SignUp;
    private javax.swing.JTextField Username;
    private javax.swing.JLabel UsernameLabel;
    // End of variables declaration                  
}