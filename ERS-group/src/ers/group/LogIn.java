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
        SignInPanel.setPreferredSize(new java.awt.Dimension(350, 360));


        Username.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N


        Password.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Password.addActionListener(this::PasswordActionPerformed);


        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UsernameLabel.setForeground(new java.awt.Color(77, 142, 162));
        UsernameLabel.setText("Username:");


        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PasswordLabel.setForeground(new java.awt.Color(77, 142, 162));
        PasswordLabel.setText("Password:");


        SignIn.setBackground(new java.awt.Color(31, 58, 95));
        SignIn.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        SignIn.setForeground(new java.awt.Color(255, 255, 255));
        SignIn.setText("Sign In");
        SignIn.setPreferredSize(new java.awt.Dimension(80, 25));
        SignIn.addActionListener(this::SignInActionPerformed);


        SignUp.setBackground(new java.awt.Color(31, 58, 95));
        SignUp.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        SignUp.setForeground(new java.awt.Color(255, 255, 255));
        SignUp.setText("Sign Up Here");
        SignUp.setPreferredSize(new java.awt.Dimension(75, 25));


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
                    .addComponent(Password)
                    .addComponent(UsernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(SignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(NoAccount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
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
                .addComponent(SignInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        // TODO add your handling code here:
    }                                        


    private void SignInActionPerformed(java.awt.event.ActionEvent evt) {                                      
        String username = Username.getText();
        String password = new String(Password.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Sign In Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("login.txt"))) { // change path if needed
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // file format: username,email,password
                if (data.length >= 3) {
                    if (data[0].trim().equals(username) && data[2].trim().equals(password)) {
                        found = true;
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (found) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Sign in successful!",
                "Welcome",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            // open main system
            new StudentCourseTab().setVisible(true);
            this.dispose();

        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Username or password is incorrect.",
                "Login Failed",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
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
    private javax.swing.JLabel SMS;
    private javax.swing.JButton SignIn;
    private javax.swing.JPanel SignInPanel;
    private javax.swing.JButton SignUp;
    private javax.swing.JLabel University;
    private javax.swing.JLabel NoAccount;
    // End of variables declaration                  
}