package kae.pos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TicketPanel extends JPanel {

    private static final String[] COLUMNS = {"ID", "Nb items", "Total (€)"};

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnDelete;

    public TicketPanel() {
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

        int[] widths = {60, 100, 100};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        btnDelete = new JButton("Delete");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(btnDelete);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.NORTH);
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
    public JButton getBtnDelete() { return btnDelete; }
    public JTable getTable() { return table; }
}