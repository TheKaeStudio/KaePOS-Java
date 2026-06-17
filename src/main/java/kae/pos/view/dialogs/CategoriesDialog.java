package kae.pos.view.dialogs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CategoriesDialog extends JDialog {

    private static final String[] COLUMNS = {"ID", "Name"};

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnClose;

    public CategoriesDialog(Frame parent) {
        super(parent, "Manage categories", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        btnClose = new JButton("Close");
        btnClose.addActionListener(e -> setVisible(false));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(btnAdd);
        toolbar.add(btnDelete);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnClose);

        setLayout(new BorderLayout(0, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(400, 350);
        setLocationRelativeTo(parent);
    }

    public void refreshTable(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) tableModel.addRow(row);
    }

    public Object getSelectedId() {
        int row = table.getSelectedRow();
        return (row >= 0) ? tableModel.getValueAt(row, 0) : null;
    }

    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnClose() { return btnClose; }
    public JTable getTable() { return table; }
}