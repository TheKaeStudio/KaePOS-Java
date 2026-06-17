package kae.pos;

import com.formdev.flatlaf.FlatDarkLaf;
import kae.pos.controller.*;
import kae.pos.model.authentication.PropertiesAuthenticator;
import kae.pos.model.dao.CategoryDao;
import kae.pos.model.dao.EmployeeDao;
import kae.pos.model.dao.OrderDao;
import kae.pos.model.dao.ProductDao;
import kae.pos.model.entity.Employee;
import kae.pos.model.entity.Role;
import kae.pos.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();

            CategoryDao categoryDao = new CategoryDao();
            ProductDao productDao = new ProductDao();
            OrderDao orderDao = new OrderDao();
            EmployeeDao employeeDao = new EmployeeDao();
            PropertiesAuthenticator authenticator = new PropertiesAuthenticator("data/users.properties");

            if (employeeDao.findAll().isEmpty()) {
                Employee admin = new Employee("admin", "1234", Role.MANAGER);
                employeeDao.create(admin);
                authenticator.addUser("admin", "1234");
            }

            new LoginController(frame, authenticator, employeeDao);
            new AppController(frame);

            TicketController ticketController = new TicketController(frame.getTicketPanel(), orderDao);
            CashierController cashierController = new CashierController(frame.getCashierPanel(), productDao,
                    orderDao, categoryDao, ticketController::refreshTable);

            new CategoryController(frame, frame.getProductPanel(), categoryDao,
                    cashierController::refreshCategoryCombo);
            new ProductController(frame, frame.getProductPanel(), productDao, categoryDao);
            new EmployeeController(frame, frame.getEmployeePanel(), employeeDao, authenticator);

            frame.setVisible(true);
        });
    }
}