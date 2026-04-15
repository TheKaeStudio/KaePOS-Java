package kae.pos.view.dialogs;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDialog extends JDialog implements ActionListener {

    private JTextField fieldName;
    private JComboBox<String> comboCategory;
    private JTextField fieldPrice;
    private JCheckBox checkInStock;
    private JButton btnOk;
    private JButton btnCancel;

    private boolean confirmed = false;

    public ProductDialog(Frame parent, String title, String[] categories) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        fieldName = new JTextField(18);
        comboCategory = new JComboBox<>(categories);
        fieldPrice = new JTextField(10);
        checkInStock = new JCheckBox("In stock", true);
        btnOk = new JButton("Save");
        btnCancel = new JButton("Cancel");

        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        panel.add(new JLabel("Name:"));
        panel.add(fieldName);

        panel.add(new JLabel("Category:"));
        panel.add(comboCategory);

        panel.add(new JLabel("Price (€):"));
        panel.add(fieldPrice);

        panel.add(new JLabel("Availability:"));
        panel.add(checkInStock);

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
            try {
                float price = Float.parseFloat(fieldPrice.getText().replace(',', '.'));
                if (price < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price.",
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

    public void prefill(String name, String category, float price, boolean inStock) {
        fieldName.setText(name);
        comboCategory.setSelectedItem(category);
        fieldPrice.setText(String.valueOf(price));
        checkInStock.setSelected(inStock);
    }

    public boolean isConfirmed() { return confirmed; }
    public String getName() { return fieldName.getText().trim(); }
    public String getCategory() { return (String) comboCategory.getSelectedItem(); }
    public float getPrice() { return Float.parseFloat(fieldPrice.getText().replace(',', '.')); }
    public boolean isInStock() { return checkInStock.isSelected(); }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            String[] cats = {"Drinks", "Snacks", "Hygiene"};

            ProductDialog dlg = new ProductDialog(null, "Add product", cats);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                System.out.println("Name: " + dlg.getName());
                System.out.println("Category: " + dlg.getCategory());
                System.out.println("Price: " + dlg.getPrice());
                System.out.println("In stock: " + dlg.isInStock());
            } else {
                System.out.println("Cancelled.");
            }
            dlg.dispose();

            ProductDialog dlg2 = new ProductDialog(null, "Edit product", cats);
            dlg2.prefill("Coca-Cola", "Drinks", 1.80f, true);
            dlg2.setVisible(true);
            if (dlg2.isConfirmed()) {
                System.out.println("Name: " + dlg2.getName());
                System.out.println("Category: " + dlg2.getCategory());
                System.out.println("Price: " + dlg2.getPrice());
                System.out.println("In stock: " + dlg2.isInStock());
            } else {
                System.out.println("Cancelled.");
            }
            dlg2.dispose();
        });
    }
}