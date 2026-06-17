package kae.pos.controller;

import kae.pos.model.dao.CategoryDao;
import kae.pos.model.dao.ProductDao;
import kae.pos.model.entity.Category;
import kae.pos.model.entity.Product;
import kae.pos.view.ProductPanel;
import kae.pos.view.dialogs.ProductDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductController {

    private final Frame parentFrame;
    private final ProductPanel panel;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    public ProductController(Frame parentFrame, ProductPanel panel, ProductDao productDao, CategoryDao categoryDao) {
        this.parentFrame = parentFrame;
        this.panel = panel;
        this.productDao = productDao;
        this.categoryDao = categoryDao;

        panel.getBtnAdd().addActionListener(e -> openAddDialog());
        panel.getBtnEdit().addActionListener(e -> openEditDialog());
        panel.getBtnDelete().addActionListener(e -> deleteSelected());
        panel.getComboCategory().addActionListener(e -> refreshTable());

        refreshCategoryCombo();
        refreshTable();
    }

    private void openAddDialog() {
        String[] names = categoryNames();
        if (names.length == 0) {
            JOptionPane.showMessageDialog(panel, "Please create a category first.",
                    "No category", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ProductDialog dialog = new ProductDialog(parentFrame, "Add product", names);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Category category = findCategoryByName(dialog.getCategory());
            Product product = new Product(dialog.getName(), category, dialog.getPrice());
            product.setStock(dialog.isInStock());
            productDao.create(product);
            refreshTable();
        }
    }

    private void openEditDialog() {
        Object selectedId = panel.getSelectedId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(panel, "Please select a product first.",
                    "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Product product = productDao.findById((int) selectedId);
        if (product == null) return;

        ProductDialog dialog = new ProductDialog(parentFrame, "Edit product", categoryNames());
        dialog.prefill(product.getName(), product.getCategory().getName(), product.getPrice(), product.getStock());
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            product.setName(dialog.getName());
            product.setCategory(findCategoryByName(dialog.getCategory()));
            product.setPrice(dialog.getPrice());
            product.setStock(dialog.isInStock());
            productDao.update(product);
            refreshTable();
        }
    }

    private void deleteSelected() {
        Object selectedId = panel.getSelectedId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(panel, "Please select a product first.",
                    "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(panel, "Delete this product?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productDao.delete((int) selectedId);
            refreshTable();
        }
    }

    public void refreshCategoryCombo() {
        panel.setCategoryItems(categoryNames());
    }

    public void refreshTable() {
        String selected = (String) panel.getComboCategory().getSelectedItem();
        boolean showAll = selected == null || selected.equals("All");

        List<Product> filtered = productDao.findAll().stream()
                .filter(p -> showAll || p.getCategory().getName().equals(selected))
                .toList();

        Object[][] data = new Object[filtered.size()][5];
        for (int i = 0; i < filtered.size(); i++) {
            Product p = filtered.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getName();
            data[i][2] = p.getCategory().getName();
            data[i][3] = String.format("%.2f", p.getPrice());
            data[i][4] = p.getStock() ? "Yes" : "No";
        }
        panel.refreshTable(data);
    }

    private Category findCategoryByName(String name) {
        return categoryDao.findAll().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst().orElse(null);
    }

    private String[] categoryNames() {
        return categoryDao.findAll().stream().map(Category::getName).toArray(String[]::new);
    }
}