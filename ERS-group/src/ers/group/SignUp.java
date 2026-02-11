package ers.group;


/**
 *
 * @author Eli
 */
public class SignUp extends javax.swing.JDialog {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SignUp.class.getName());


    public SignUp(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
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


        SignUp.setBackground(new java.awt.Color(31, 58, 95));
        SignUp.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        SignUp.setForeground(new java.awt.Color(255, 255, 255));
        SignUp.setText("Sign Up");
        SignUp.addActionListener(this::SignUpActionPerformed);


        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UsernameLabel.setForeground(new java.awt.Color(77, 142, 162));
        UsernameLabel.setText("Username:");


        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PasswordLabel.setForeground(new java.awt.Color(77, 142, 162));
        PasswordLabel.setText("Password:");


        EmailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EmailLabel.setForeground(new java.awt.Color(77, 142, 162));
        EmailLabel.setText("Email:");


        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Password)
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
                .addComponent(EmailLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(UsernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(PasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        // TODO add your handling code here:
    }                                        


    private void SignUpActionPerformed(java.awt.event.ActionEvent evt) {                                      
        String email = Email.getText();
        String username = Username.getText();
        String password = new String(Password.getPassword());


        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Sign Up Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Account created successfully!",
                "Success",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
            );


            // Optional: close the dialog after successful sign up
            this.dispose();
        }
    }                                      

    // Variables declaration - do not modify                    
    private javax.swing.JPanel MainPanel;
    private javax.swing.JTextField Email;
    private javax.swing.JLabel EmailLabel;
    private javax.swing.JPasswordField Password;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JButton SignUp;
    private javax.swing.JTextField Username;
    private javax.swing.JLabel UsernameLabel;
    // End of variables declaration                  
}