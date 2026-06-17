package kae.pos.controller;

import kae.pos.model.dao.CategoryDao;
import kae.pos.model.dao.OrderDao;
import kae.pos.model.dao.ProductDao;
import kae.pos.model.entity.Category;
import kae.pos.model.entity.Order;
import kae.pos.model.entity.Product;
import kae.pos.view.CashierPanel;
import kae.pos.view.MainFrame;

import javax.swing.*;
import java.util.List;

public class CashierController {

    private final CashierPanel panel;
    private final ProductDao productDao;
    private final OrderDao orderDao;
    private final CategoryDao categoryDao;
    private final Runnable onOrderCompleted;

    private Order currentOrder;

    public CashierController(MainFrame frame, CashierPanel panel, ProductDao productDao, OrderDao orderDao,
                             CategoryDao categoryDao, Runnable onOrderCompleted) {
        this.panel = panel;
        this.productDao = productDao;
        this.orderDao = orderDao;
        this.categoryDao = categoryDao;
        this.onOrderCompleted = onOrderCompleted;
        this.currentOrder = new Order();

        panel.getBtnAdd().addActionListener(e -> addToCart());
        panel.getBtnRemove().addActionListener(e -> removeFromCart());
        panel.getBtnCheckout().addActionListener(e -> checkout());
        panel.getBtnCancel().addActionListener(e -> cancelOrder());
        panel.getComboCategory().addActionListener(e -> refreshCatalogue());

        frame.getTabbedPane().addChangeListener(e -> {
            if (frame.getTabbedPane().getSelectedIndex() == MainFrame.TAB_CASHIER) {
                refreshCategoryCombo();
                refreshCatalogue();
            }
        });

        refreshCategoryCombo();
        refreshCatalogue();
        refreshCart();
    }

    public void refreshCategoryCombo() {
        panel.setCategoryItems(categoryDao.findAll().stream().map(Category::getName).toArray(String[]::new));
    }

    public void refreshCatalogue() {
        String selected = (String) panel.getComboCategory().getSelectedItem();
        boolean showAll = selected == null || selected.equals("All");

        List<Product> filtered = productDao.findAll().stream()
                .filter(Product::getStock)
                .filter(p -> showAll || p.getCategory().getName().equals(selected))
                .toList();

        Object[][] data = new Object[filtered.size()][3];
        for (int i = 0; i < filtered.size(); i++) {
            Product p = filtered.get(i);
            data[i][0] = p.getName();
            data[i][1] = p.getCategory().getName();
            data[i][2] = String.format("%.2f", p.getPrice());
        }
        panel.refreshCatalogue(data);
    }

    private void addToCart() {
        int row = panel.getSelectedCatalogueRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(panel, "Please select a product first.",
                    "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productName = (String) panel.getTableCatalogue().getValueAt(row, 0);
        Product product = productDao.findAll().stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst().orElse(null);
        if (product == null) return;

        String input = JOptionPane.showInputDialog(panel, "Quantity:", "1");
        if (input == null) return;

        try {
            int qty = Integer.parseInt(input);
            currentOrder.addItem(product, qty);
            refreshCart();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(panel, "Invalid quantity.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromCart() {
        int row = panel.getSelectedCartRow();
        if (row < 0) return;

        String label = (String) panel.getTableCart().getValueAt(row, 0);
        String productName = label.substring(0, label.lastIndexOf(" x"));

        currentOrder.getItems().stream()
                .filter(item -> item.getProduct().getName().equals(productName))
                .findFirst()
                .ifPresent(item -> currentOrder.removeProduct(item.getProduct()));

        refreshCart();
    }

    private void checkout() {
        if (currentOrder.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Cart is empty.",
                    "Nothing to checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }
        orderDao.create(currentOrder);
        JOptionPane.showMessageDialog(panel, "Order completed. Total: "
                + String.format("%.2f €", currentOrder.getTotalPrice()));
        currentOrder = new Order();
        refreshCart();
        onOrderCompleted.run();
    }

    private void cancelOrder() {
        currentOrder = new Order();
        refreshCart();
    }

    private void refreshCart() {
        Object[][] data = new Object[currentOrder.getItems().size()][3];
        for (int i = 0; i < currentOrder.getItems().size(); i++) {
            var item = currentOrder.getItems().get(i);
            data[i][0] = item.getProduct().getName() + " x" + item.getQuantity();
            data[i][1] = item.getQuantity();
            data[i][2] = String.format("%.2f", item.getTotalPrice());
        }
        panel.refreshCart(data);
        panel.setTotal(currentOrder.getTotalPrice());
    }
}