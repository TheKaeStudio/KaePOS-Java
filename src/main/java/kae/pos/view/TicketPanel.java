package kae.pos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TicketPanel extends JPanel {

    private static final String[] COLUMNS = {"ID", "Date", "Content", "Total (€)"};

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnDelete;
    private JButton btnExportCsv;

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

        int[] widths = {50, 100, 280, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        btnDelete = new JButton("Delete");
        btnExportCsv = new JButton("Export to CSV");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(btnDelete);
        bottom.add(btnExportCsv);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
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
    public JButton getBtnExportCsv() { return btnExportCsv; }
    public JTable getTable() { return table; }
}