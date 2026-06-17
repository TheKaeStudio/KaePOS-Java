package kae.pos.controller;

import kae.pos.model.dao.OrderDao;
import kae.pos.model.entity.Order;
import kae.pos.model.entity.OrderItem;
import kae.pos.model.io.OrderCsvExporter;
import kae.pos.view.TicketPanel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final TicketPanel panel;
    private final OrderDao orderDao;

    public TicketController(TicketPanel panel, OrderDao orderDao) {
        this.panel = panel;
        this.orderDao = orderDao;

        panel.getBtnDelete().addActionListener(e -> deleteSelected());
        panel.getBtnExportCsv().addActionListener(e -> exportToCsv());

        refreshTable();
    }

    public void refreshTable() {
        List<Order> orders = orderDao.findAll();
        Object[][] data = new Object[orders.size()][4];
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            data[i][0] = o.getId();
            data[i][1] = o.getDate().format(DATE_FORMATTER);
            data[i][2] = formatContent(o);
            data[i][3] = String.format("%.2f", o.getTotalPrice());
        }
        panel.refreshTable(data);
    }

    private String formatContent(Order order) {
        return order.getItems().stream()
                .map(OrderItem::toString)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    private void deleteSelected() {
        Object selectedId = panel.getSelectedId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(panel, "Please select an order first.",
                    "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(panel, "Delete this order?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            orderDao.delete((int) selectedId);
            refreshTable();
        }
    }

    private void exportToCsv() {
        List<Order> orders = orderDao.findAll();
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "No orders to export.",
                    "Nothing to export", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("orders.csv"));
        int result = chooser.showSaveDialog(panel);
        if (result != JFileChooser.APPROVE_OPTION) return;

        String path = chooser.getSelectedFile().getPath();
        if (!path.toLowerCase().endsWith(".csv")) path += ".csv";

        try {
            new OrderCsvExporter().export(orders, path);
            JOptionPane.showMessageDialog(panel, "Export successful:\n" + path);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel, "Export failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}