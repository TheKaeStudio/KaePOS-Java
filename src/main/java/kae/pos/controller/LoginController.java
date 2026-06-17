package kae.pos.controller;

import kae.pos.model.authentication.PropertiesAuthenticator;
import kae.pos.model.dao.EmployeeDao;
import kae.pos.model.entity.Employee;
import kae.pos.view.MainFrame;
import kae.pos.view.dialogs.LoginDialog;

import javax.swing.*;

public class LoginController {

    private final MainFrame frame;
    private final PropertiesAuthenticator authenticator;
    private final EmployeeDao employeeDao;

    public LoginController(MainFrame frame, PropertiesAuthenticator authenticator, EmployeeDao employeeDao) {
        this.frame = frame;
        this.authenticator = authenticator;
        this.employeeDao = employeeDao;

        frame.getMiLogin().addActionListener(e -> openLoginDialog());
        frame.getMiLogout().addActionListener(e -> frame.applyInitialState());
    }

    private void openLoginDialog() {
        LoginDialog dialog = new LoginDialog(frame);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        String username = dialog.getUsername();
        Employee employee = employeeDao.findByUsername(username);

        if (employee != null && !employee.isPasswordSet()) {
            promptCreatePassword(employee);
            return;
        }

        if (authenticator.authenticate(username, dialog.getPassword())) {
            boolean isManager = employee != null && employee.isManager();
            frame.applyConnectedState(username, isManager);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password.",
                    "Login failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void promptCreatePassword(Employee employee) {
        JPasswordField pf1 = new JPasswordField();
        JPasswordField pf2 = new JPasswordField();
        Object[] message = {
                "This account has no password yet.\nPlease create one:",
                "New password:", pf1,
                "Confirm password:", pf2
        };
        int option = JOptionPane.showConfirmDialog(frame, message, "Create password",
                JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String p1 = new String(pf1.getPassword());
        String p2 = new String(pf2.getPassword());

        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            employee.setPassword(p1);
            employee.setPasswordSet(true);
            employeeDao.save();
            authenticator.setPassword(employee.getUsername(), p1);
            frame.applyConnectedState(employee.getUsername(), employee.isManager());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}