package kae.pos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CashierPanel extends JPanel {

    private static final String[] COL_CATALOGUE = {"Product", "Category", "Price (€)"};
    private static final String[] COL_CART = {"Item", "Qty", "Subtotal (€)"};

    private DefaultTableModel modelCatalogue;
    private DefaultTableModel modelCart;
    private JTable tableCatalogue;
    private JTable tableCart;

    private JComboBox<String> comboCategory;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnCheckout;
    private JButton btnCancel;
    private JLabel lblTotal;

    public CashierPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modelCatalogue = new DefaultTableModel(COL_CATALOGUE, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCatalogue = new JTable(modelCatalogue);
        tableCatalogue.setRowHeight(24);
        tableCatalogue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCatalogue.getTableHeader().setReorderingAllowed(false);

        comboCategory = new JComboBox<>();
        btnAdd = new JButton("Add to cart");

        JPanel toolbarCatalogue = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbarCatalogue.add(new JLabel("Category:"));
        toolbarCatalogue.add(comboCategory);
        toolbarCatalogue.add(btnAdd);

        JPanel leftPanel = new JPanel(new BorderLayout(0, 6));
        leftPanel.add(toolbarCatalogue, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(tableCatalogue), BorderLayout.CENTER);

        modelCart = new DefaultTableModel(COL_CART, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCart = new JTable(modelCart);
        tableCart.setRowHeight(24);
        tableCart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCart.getTableHeader().setReorderingAllowed(false);

        btnRemove = new JButton("Remove");
        btnCheckout = new JButton("Checkout");
        btnCancel = new JButton("Cancel order");
        lblTotal = new JLabel("Total: 0.00 €");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));

        JPanel toolbarCart = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbarCart.add(btnRemove);

        JPanel bottomCart = new JPanel(new GridLayout(3, 1, 4, 4));
        bottomCart.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        bottomCart.add(lblTotal);
        bottomCart.add(btnCheckout);
        bottomCart.add(btnCancel);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 6));
        rightPanel.add(toolbarCart, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(tableCart), BorderLayout.CENTER);
        rightPanel.add(bottomCart, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setDividerLocation(520);
        split.setBorder(null);

        add(split, BorderLayout.CENTER);
    }

    public void refreshCatalogue(Object[][] data) {
        modelCatalogue.setRowCount(0);
        for (Object[] row : data) modelCatalogue.addRow(row);
    }

    public void refreshCart(Object[][] data) {
        modelCart.setRowCount(0);
        for (Object[] row : data) modelCart.addRow(row);
    }

    public void setTotal(float total) {
        lblTotal.setText(String.format("Total: %.2f €", total));
    }

    public void setCategoryItems(String[] items) {
        comboCategory.removeAllItems();
        comboCategory.addItem("All");
        for (String item : items) comboCategory.addItem(item);
    }

    public int getSelectedCatalogueRow() { return tableCatalogue.getSelectedRow(); }
    public int getSelectedCartRow() { return tableCart.getSelectedRow(); }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnRemove() { return btnRemove; }
    public JButton getBtnCheckout() { return btnCheckout; }
    public JButton getBtnCancel() { return btnCancel; }
    public JComboBox<String> getComboCategory() { return comboCategory; }
    public JTable getTableCatalogue() { return tableCatalogue; }
    public JTable getTableCart() { return tableCart; }
}