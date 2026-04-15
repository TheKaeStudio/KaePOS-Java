package kae.pos.view.dialogs;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog implements ActionListener {

    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JButton btnOk;
    private JButton btnCancel;

    private boolean confirmed = false;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fieldUsername = new JTextField(18);
        fieldPassword = new JPasswordField(18);
        btnOk = new JButton("Sign in");
        btnCancel = new JButton("Cancel");

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        panel.add(new JLabel("Username:"));
        panel.add(fieldUsername);

        panel.add(new JLabel("Password:"));
        panel.add(fieldPassword);

        panel.add(btnCancel);
        panel.add(btnOk);

        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        getRootPane().setDefaultButton(btnOk);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOk) {
            if (fieldUsername.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Username is required.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            confirmed = true;
            setVisible(false);
        }
        if (e.getSource() == btnCancel) {
            setVisible(false);
        }
    }

    public boolean isConfirmed() { return confirmed; }
    public String getUsername() { return fieldUsername.getText().trim(); }
    public String getPassword() { return new String(fieldPassword.getPassword()); }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            LoginDialog dlg = new LoginDialog(null);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                System.out.println("Username: " + dlg.getUsername());
                System.out.println("Password: " + dlg.getPassword());
            } else {
                System.out.println("Cancelled.");
            }
            dlg.dispose();
        });
    }
}