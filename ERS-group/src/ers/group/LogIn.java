    package ers.group;

    import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

    /**
     *
     * @author Eli
     */
    public class LogIn extends javax.swing.JFrame {    
        private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LogIn.class.getName());


        public LogIn() {
            initComponents();
            setupFieldValidation();
        }

        private void setupFieldValidation() {
            final javax.swing.border.Border DEF = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1);
            final javax.swing.border.Border OK  = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(34, 139, 34), 2);
            final javax.swing.border.Border BAD = javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 50, 50), 2);
            Username.setBorder(javax.swing.BorderFactory.createCompoundBorder(DEF, javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
            // Password border changes go to PasswordWrap after initComponents builds it,
            // so we defer the initial set and listeners to after init via invokeLater.
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (PasswordWrap != null) PasswordWrap.setBorder(DEF);
            });
            Username.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                private void update() {
                    javax.swing.border.Border b = Username.getText().trim().isEmpty() ? DEF : OK;
                    Username.setBorder(javax.swing.BorderFactory.createCompoundBorder(b, javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
                }
                @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
                @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
                @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
            });
            Password.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                private void update() {
                    String p = new String(Password.getPassword()).trim();
                    javax.swing.border.Border b = p.isEmpty() ? DEF : (p.length() >= 6 ? OK : BAD);
                    if (PasswordWrap != null) PasswordWrap.setBorder(b);
                }
                @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
                @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
                @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
            });
        }


        private void initComponents() {
            MainPanel = new javax.swing.JPanel();
            University = new javax.swing.JLabel();
            SMS = new javax.swing.JLabel();
            SignInPanel = new javax.swing.JPanel();
            Username = new javax.swing.JTextField();
            Password = new javax.swing.JPasswordField();
            UsernameLabel = new javax.swing.JLabel();
            PasswordLabel = new javax.swing.JLabel();
            SignIn = new javax.swing.JButton();
            SignUp = new javax.swing.JButton();
            NoAccount = new javax.swing.JLabel();


            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


            MainPanel.setBackground(new java.awt.Color(31, 58, 95));


            University.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
            University.setForeground(new java.awt.Color(255, 255, 255));
            University.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            University.setText("Katrice Joy University");
            University.setVerticalAlignment(javax.swing.SwingConstants.TOP);


            SMS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
            SMS.setForeground(new java.awt.Color(255, 255, 255));
            SMS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            SMS.setText("School Management System");


            SignInPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
            SignInPanel.setPreferredSize(new java.awt.Dimension(350, 400));


            Username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            Username.addActionListener(this::SignInActionPerformed);


            Password.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            Password.addActionListener(this::SignInActionPerformed);

            ShowPassword = new javax.swing.JButton() {
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
            ShowPassword.setPreferredSize(new java.awt.Dimension(34, 0));
            ShowPassword.setBackground(java.awt.Color.WHITE);
            ShowPassword.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            ShowPassword.setContentAreaFilled(false);
            ShowPassword.setFocusPainted(false);
            ShowPassword.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            ShowPassword.setToolTipText("Show / hide password");
            ShowPassword.addActionListener(e -> {
                Password.setEchoChar(Password.getEchoChar() != (char)0 ? (char)0 : '\u2022');
                ShowPassword.repaint();
            });
            PasswordWrap = new javax.swing.JPanel(new java.awt.BorderLayout(0, 0));
            PasswordWrap.setBackground(java.awt.Color.WHITE);
            Password.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 0));
            PasswordWrap.add(Password, java.awt.BorderLayout.CENTER);
            PasswordWrap.add(ShowPassword, java.awt.BorderLayout.EAST);

            UsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
            UsernameLabel.setForeground(new java.awt.Color(77, 142, 162));
            UsernameLabel.setText("<html>Username or Email: <font color='red'>*</font></html>");


            PasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
            PasswordLabel.setForeground(new java.awt.Color(77, 142, 162));
            PasswordLabel.setText("<html>Password: <font color='red'>*</font></html>");


            SignIn.setBackground(new java.awt.Color(31, 58, 95));
            SignIn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
            SignIn.setForeground(new java.awt.Color(255, 255, 255));
            SignIn.setText("Sign In");
            SignIn.addActionListener(this::SignInActionPerformed);


            SignUp.setBackground(new java.awt.Color(31, 58, 95));
            SignUp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
            SignUp.setForeground(new java.awt.Color(255, 255, 255));
            SignUp.setText("Sign Up Here");


            SignUp.addActionListener(evt -> {
                SignUp signUpDialog = new SignUp(this, true);
                signUpDialog.setLocationRelativeTo(this); // center on login
                signUpDialog.setVisible(true);
            });


            NoAccount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            NoAccount.setForeground(new java.awt.Color(77, 142, 162));
            NoAccount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            NoAccount.setText("Don't have an account yet?");


            javax.swing.GroupLayout SignInPanelLayout = new javax.swing.GroupLayout(SignInPanel);
            SignInPanel.setLayout(SignInPanelLayout);
            SignInPanelLayout.setHorizontalGroup(
                SignInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(SignInPanelLayout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addGroup(SignInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Username)
                        .addComponent(PasswordWrap)
                        .addComponent(UsernameLabel)
                        .addComponent(PasswordLabel)
                        .addComponent(SignIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SignUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NoAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                    .addContainerGap(36, Short.MAX_VALUE))
            );
            SignInPanelLayout.setVerticalGroup(
                SignInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(SignInPanelLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(UsernameLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(31, 31, 31)
                    .addComponent(PasswordLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(PasswordWrap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20, 20, 20)
                    .addComponent(SignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                    .addComponent(NoAccount)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20, 20, 20))
            );


            javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
            MainPanel.setLayout(MainPanelLayout);
            MainPanelLayout.setHorizontalGroup(
                MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(University, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                            .addGap(0, 556, Short.MAX_VALUE)
                            .addComponent(SMS, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(547, 547, 547))))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SignInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(589, 589, 589))
            );
            MainPanelLayout.setVerticalGroup(
                MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainPanelLayout.createSequentialGroup()
                    .addGap(139, 139, 139)
                    .addComponent(University)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(SMS)
                    .addGap(52, 52, 52)
                    .addComponent(SignInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(82, Short.MAX_VALUE))
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

        private void SignInActionPerformed(java.awt.event.ActionEvent evt) {                                     
            String username = Username.getText().trim();
            String password = new String(Password.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Sign In Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                javax.swing.JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.", "Sign In Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean found = false;

            // Use the FilePathResolver to open the file for reading
            try (BufferedReader br = new BufferedReader(new FileReader(FilePathResolver.resolveLoginFilePath()))) {
                String encryptedLine;

                while ((encryptedLine = br.readLine()) != null) {
                    // 1. Decrypt the WHOLE line first
                    String decryptedFullLine = Encryption.decrypt(encryptedLine);
                    
                    // Debugging line to see what's happening in your console
                    System.out.println("DEBUG: Decrypted -> " + decryptedFullLine);

                    // 2. Split the already-decrypted line
                    String[] data = decryptedFullLine.split(",");

                    // 3. Check if we have Email, Username, and Password (3 parts)
                    if (data.length >= 3) {
                        String fileEmail    = data[0].trim();
                        String fileUsername = data[1].trim();
                        String filePassword = data[2].trim();

                        // 4. Compare credentials - match by username OR email
                        boolean credentialsMatch = (fileUsername.equals(username) || fileEmail.equals(username))
                                                && filePassword.equals(password);
                        if (credentialsMatch) {
                            found = true;
                            StudentCourseTab.SessionManager.setCurrentStudent(fileUsername, fileUsername);
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this, "Error reading login file.");
            }

            // Handle the result after the loop finishes
            if (found) {
                javax.swing.JOptionPane.showMessageDialog(this, "Sign in successful!", "Welcome", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                new StudentCourseTab().setVisible(true);
                this.dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Username or password is incorrect.", "Login Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
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
            java.awt.EventQueue.invokeLater(() -> new LogIn().setVisible(true));
        }


        // Variables declaration - do not modify                    
        private javax.swing.JTextField Username;
        private javax.swing.JLabel UsernameLabel;
        private javax.swing.JPanel MainPanel;
        private javax.swing.JPasswordField Password;
        private javax.swing.JLabel PasswordLabel;
        private javax.swing.JPanel PasswordWrap;
        private javax.swing.JButton ShowPassword;
        private javax.swing.JLabel SMS;
        private javax.swing.JButton SignIn;
        private javax.swing.JPanel SignInPanel;
        private javax.swing.JButton SignUp;
        private javax.swing.JLabel University;
        private javax.swing.JLabel NoAccount;
        // End of variables declaration                  
    }