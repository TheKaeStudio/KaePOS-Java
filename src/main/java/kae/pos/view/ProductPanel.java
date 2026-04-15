package kae.pos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductPanel extends JPanel {

    private static final String[] COLUMNS = {"ID", "Name", "Category", "Price (€)", "In stock"};

    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> comboCategory;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;

    public ProductPanel() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        int[] widths = {40, 180, 110, 80, 70};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        comboCategory = new JComboBox<>();
        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Category:"));
        toolbar.add(comboCategory);
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshTable(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) tableModel.addRow(row);
    }

    public void setCategoryItems(String[] items) {
        comboCategory.removeAllItems();
        comboCategory.addItem("All");
        for (String item : items) comboCategory.addItem(item);
    }

    public Object getSelectedId() {
        int row = table.getSelectedRow();
        return (row >= 0) ? tableModel.getValueAt(row, 0) : null;
    }

    public int getSelectedRow() { return table.getSelectedRow(); }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public JComboBox<String> getComboCategory() { return comboCategory; }
    public JTable getTable() { return table; }
}