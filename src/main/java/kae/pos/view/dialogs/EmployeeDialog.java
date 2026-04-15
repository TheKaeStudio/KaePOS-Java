package kae.pos.view.dialogs;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeDialog extends JDialog implements ActionListener {

    private JTextField fieldUsername;
    private JRadioButton rbCashier;
    private JRadioButton rbManager;
    private ButtonGroup groupRole;
    private JButton btnOk;
    private JButton btnCancel;

    private boolean confirmed = false;

    public EmployeeDialog(Frame parent) {
        super(parent, "Add employee", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fieldUsername = new JTextField(18);
        rbCashier = new JRadioButton("Cashier", true);
        rbManager = new JRadioButton("Manager");
        groupRole = new ButtonGroup();
        groupRole.add(rbCashier);
        groupRole.add(rbManager);
        btnOk = new JButton("Create");
        btnCancel = new JButton("Cancel");

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rolePanel.add(rbCashier);
        rolePanel.add(rbManager);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        panel.add(new JLabel("Username:"));
        panel.add(fieldUsername);

        panel.add(new JLabel("Role:"));
        panel.add(rolePanel);

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
    public boolean isManager() { return rbManager.isSelected(); }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            EmployeeDialog dlg = new EmployeeDialog(null);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                System.out.println("Username: " + dlg.getUsername());
                System.out.println("Role: " + (dlg.isManager() ? "Manager" : "Cashier"));
            } else {
                System.out.println("Cancelled.");
            }
            dlg.dispose();
        });
    }
}