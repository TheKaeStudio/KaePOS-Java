package kae.pos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private static final String[] COLUMNS = {"ID", "Username", "Role"};

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnAdd;
    private JButton btnDelete;

    public EmployeePanel() {
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

        int[] widths = {50, 200, 120};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(btnAdd);
        toolbar.add(btnDelete);

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshTable(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) tableModel.addRow(row);
    }

    public Object getSelectedId() {
        int row = table.getSelectedRow();
        return (row >= 0) ? tableModel.getValueAt(row, 0) : null;
    }

    public int getSelectedRow() { return table.getSelectedRow(); }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JTable getTable() { return table; }
}