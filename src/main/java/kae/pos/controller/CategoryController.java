package kae.pos.controller;

import kae.pos.model.dao.CategoryDao;
import kae.pos.model.entity.Category;
import kae.pos.view.ProductPanel;
import kae.pos.view.dialogs.CategoriesDialog;
import kae.pos.view.dialogs.CategoryDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CategoryController {

    private final CategoryDao dao;
    private final ProductPanel productPanel;
    private final Frame parentFrame;
    private final Runnable[] onCategoriesChanged;

    public CategoryController(Frame parentFrame, ProductPanel productPanel, CategoryDao dao,
                              Runnable... onCategoriesChanged) {
        this.parentFrame = parentFrame;
        this.productPanel = productPanel;
        this.dao = dao;
        this.onCategoriesChanged = onCategoriesChanged;

        productPanel.getBtnManageCategories().addActionListener(e -> openManageCategoriesDialog());
        refreshAllCategoryCombos();
    }

    private void openManageCategoriesDialog() {
        CategoriesDialog dialog = new CategoriesDialog(parentFrame);
        refreshDialogTable(dialog);

        dialog.getBtnAdd().addActionListener(e -> {
            CategoryDialog addDialog = new CategoryDialog(parentFrame, "Add category");
            addDialog.setVisible(true);
            if (addDialog.isConfirmed()) {
                try {
                    Category category = new Category(addDialog.getName());
                    dao.create(category);
                    refreshDialogTable(dialog);
                    refreshAllCategoryCombos();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.getBtnDelete().addActionListener(e -> {
            Object selectedId = dialog.getSelectedId();
            if (selectedId == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a category first.",
                        "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) selectedId;
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Delete this category?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.delete(id);
                refreshDialogTable(dialog);
                refreshAllCategoryCombos();
            }
        });

        dialog.setVisible(true);
    }

    private void refreshDialogTable(CategoriesDialog dialog) {
        List<Category> categories = dao.findAll();
        Object[][] data = new Object[categories.size()][2];
        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
        }
        dialog.refreshTable(data);
    }

    private void refreshAllCategoryCombos() {
        List<Category> categories = dao.findAll();
        String[] names = categories.stream().map(Category::getName).toArray(String[]::new);
        productPanel.setCategoryItems(names);

        for (Runnable listener : onCategoriesChanged) {
            listener.run();
        }
    }
}