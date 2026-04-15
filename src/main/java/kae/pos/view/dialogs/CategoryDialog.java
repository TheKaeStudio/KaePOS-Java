package kae.pos.view.dialogs;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryDialog extends JDialog implements ActionListener {

    private JTextField fieldName;
    private JButton btnOk;
    private JButton btnCancel;

    private boolean confirmed = false;

    public CategoryDialog(Frame parent, String title) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fieldName = new JTextField(18);
        btnOk = new JButton("Save");
        btnCancel = new JButton("Cancel");

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        panel.add(new JLabel("Name:"));
        panel.add(fieldName);

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
            if (fieldName.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Name is required.",
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

    public void prefill(String name) {
        fieldName.setText(name);
    }

    public boolean isConfirmed() { return confirmed; }
    public String getName() { return fieldName.getText().trim(); }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            CategoryDialog dlg = new CategoryDialog(null, "Add category");
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                System.out.println("Name: " + dlg.getName());
            } else {
                System.out.println("Cancelled.");
            }
            dlg.dispose();
        });
    }
}