package kae.pos.controller;

import kae.pos.model.authentication.PropertiesAuthenticator;
import kae.pos.model.dao.EmployeeDao;
import kae.pos.model.entity.Employee;
import kae.pos.model.entity.Role;
import kae.pos.view.EmployeePanel;
import kae.pos.view.dialogs.EmployeeDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeeController {

    private final Frame parentFrame;
    private final EmployeePanel panel;
    private final EmployeeDao employeeDao;
    private final PropertiesAuthenticator authenticator;

    public EmployeeController(Frame parentFrame, EmployeePanel panel, EmployeeDao employeeDao,
                              PropertiesAuthenticator authenticator) {
        this.parentFrame = parentFrame;
        this.panel = panel;
        this.employeeDao = employeeDao;
        this.authenticator = authenticator;

        panel.getBtnAdd().addActionListener(e -> openAddDialog());
        panel.getBtnDelete().addActionListener(e -> deleteSelected());
        refreshTable();
    }

    private void openAddDialog() {
        EmployeeDialog dialog = new EmployeeDialog(parentFrame);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String username = dialog.getUsername();
            if (employeeDao.findByUsername(username) != null) {
                JOptionPane.showMessageDialog(panel, "This username already exists.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Role role = dialog.isManager() ? Role.MANAGER : Role.CASHIER;

            Employee employee = new Employee(username, "temp0000", role);
            employee.setPasswordSet(false);

            employeeDao.create(employee);
            authenticator.addUser(username, "temp0000");
            refreshTable();
        }
    }

    private void deleteSelected() {
        Object selectedId = panel.getSelectedId();
        if (selectedId == null) {
            JOptionPane.showMessageDialog(panel, "Please select an employee first.",
                    "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) selectedId;
        Employee employee = employeeDao.findById(id);
        if (employee == null) return;

        if (employee.isManager() && countManagers() <= 1) {
            JOptionPane.showMessageDialog(panel,
                    "Cannot delete the last manager. At least one manager must remain.",
                    "Action blocked", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(panel, "Delete this employee?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            authenticator.removeUser(employee.getUsername());
            employeeDao.delete(id);
            refreshTable();
        }
    }

    private long countManagers() {
        return employeeDao.findAll().stream().filter(Employee::isManager).count();
    }

    private void refreshTable() {
        List<Employee> employees = employeeDao.findAll();
        Object[][] data = new Object[employees.size()][3];
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            data[i][0] = emp.getId();
            data[i][1] = emp.getUsername();
            data[i][2] = emp.getRole().toString();
        }
        panel.refreshTable(data);
    }


}