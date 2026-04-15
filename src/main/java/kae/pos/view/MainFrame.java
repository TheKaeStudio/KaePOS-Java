package kae.pos.view;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int TAB_CASHIER = 0;
    public static final int TAB_PRODUCTS = 1;
    public static final int TAB_TICKETS = 2;
    public static final int TAB_EMPLOYEES = 3;

    private JMenu menuFile;
    private JMenuItem miSave;
    private JMenuItem miQuit;

    private JMenu menuAccount;
    private JMenuItem miLogin;
    private JMenuItem miLogout;

    private JTabbedPane tabbedPane;
    private CashierPanel cashierPanel;
    private ProductPanel productPanel;
    private TicketPanel ticketPanel;
    private EmployeePanel employeePanel;

    private JLabel labelUser;

    public MainFrame() {
        super("KaePOS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));

        buildMenuBar();
        buildUI();
        applyInitialState();

        pack();
        setLocationRelativeTo(null);
    }

    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        menuFile = new JMenu("File");
        miSave = new JMenuItem("Save");
        miQuit = new JMenuItem("Quit");
        menuFile.add(miSave);
        menuFile.addSeparator();
        menuFile.add(miQuit);
        bar.add(menuFile);

        menuAccount = new JMenu("Account");
        miLogin = new JMenuItem("Login");
        miLogout = new JMenuItem("Logout");
        menuAccount.add(miLogin);
        menuAccount.add(miLogout);
        bar.add(menuAccount);

        bar.add(Box.createHorizontalGlue());
        labelUser = new JLabel("Not logged in");
        labelUser.setFont(labelUser.getFont().deriveFont(Font.ITALIC, 12f));
        bar.add(labelUser);

        setJMenuBar(bar);
    }

    private void buildUI() {
        cashierPanel = new CashierPanel();
        productPanel = new ProductPanel();
        ticketPanel = new TicketPanel();
        employeePanel = new EmployeePanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Cashier", cashierPanel);
        tabbedPane.addTab("Products", productPanel);
        tabbedPane.addTab("Tickets", ticketPanel);
        tabbedPane.addTab("Employees", employeePanel);

        setContentPane(tabbedPane);
    }

    public void applyInitialState() {
        miLogin.setEnabled(true);
        miLogout.setEnabled(false);
        miSave.setEnabled(false);
        for (int i = 0; i < tabbedPane.getTabCount(); i++)
            tabbedPane.setEnabledAt(i, false);
        labelUser.setText("Not logged in");
    }

    public void applyConnectedState(String username, boolean isManager) {
        miLogin.setEnabled(false);
        miLogout.setEnabled(true);
        miSave.setEnabled(true);
        tabbedPane.setEnabledAt(TAB_CASHIER, true);
        tabbedPane.setEnabledAt(TAB_PRODUCTS, true);
        tabbedPane.setEnabledAt(TAB_TICKETS, true);
        tabbedPane.setEnabledAt(TAB_EMPLOYEES, isManager);
        tabbedPane.setSelectedIndex(TAB_CASHIER);
        labelUser.setText(username + " [" + (isManager ? "Manager" : "Cashier") + "]");
    }

    public CashierPanel getCashierPanel() { return cashierPanel; }
    public ProductPanel getProductPanel() { return productPanel; }
    public TicketPanel getTicketPanel() { return ticketPanel; }
    public EmployeePanel getEmployeePanel() { return employeePanel; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }
    public JMenuItem getMiSave() { return miSave; }
    public JMenuItem getMiQuit() { return miQuit; }
    public JMenuItem getMiLogin() { return miLogin; }
    public JMenuItem getMiLogout() { return miLogout; }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();

            f.getProductPanel().refreshTable(new Object[][]{
                    {1, "Coca-Cola", "Drinks", "1.80", "Yes"},
                    {2, "Lay's Chips", "Snacks", "2.50", "Yes"},
            });
            f.getCashierPanel().refreshCatalogue(new Object[][]{
                    {"Coca-Cola", "Drinks", "1.80"},
                    {"Lay's Chips", "Snacks", "2.50"},
            });
            f.getTicketPanel().refreshTable(new Object[][]{
                    {1, 3, "7.38"},
                    {2, 5, "21.78"},
            });
            f.getEmployeePanel().refreshTable(new Object[][]{
                    {1, "admin", "Manager"},
                    {2, "ali", "Cashier"},
            });

            Timer t = new Timer(600, e -> f.applyConnectedState("admin", true));
            t.setRepeats(false);
            t.start();

            f.setVisible(true);
        });
    }
}