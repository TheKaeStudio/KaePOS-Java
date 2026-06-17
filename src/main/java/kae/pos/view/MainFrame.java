package kae.pos.view;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int TAB_CASHIER = 0;
    public static final int TAB_PRODUCTS = 1;
    public static final int TAB_TICKETS = 2;
    public static final int TAB_EMPLOYEES = 3;

    private static final String CARD_LOCKED = "locked";
    private static final String CARD_MAIN = "main";

    private JMenu menuFile;
    private JMenuItem miQuit;

    private JMenu menuAccount;
    private JMenuItem miLogin;
    private JMenuItem miLogout;

    private JPanel cardPanel;
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

        setAppIcon();
        buildMenuBar();
        buildUI();
        applyInitialState();

        pack();
        setLocationRelativeTo(null);
    }

    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        menuFile = new JMenu("Menu");
        miQuit = new JMenuItem("Quit");
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

        JPanel lockedPanel = new JPanel(new GridBagLayout());
        JLabel lockedLabel = new JLabel("Please log in to use the application.");
        lockedLabel.setFont(lockedLabel.getFont().deriveFont(Font.ITALIC, 16f));
        lockedPanel.add(lockedLabel);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(lockedPanel, CARD_LOCKED);
        cardPanel.add(tabbedPane, CARD_MAIN);

        setContentPane(cardPanel);
    }

    private void setAppIcon() {
        java.net.URL iconUrl = getClass().getResource("/icon.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        } else {
            System.err.println("Icon not found: /icon.png");
        }
    }

    public void applyInitialState() {
        miLogin.setEnabled(true);
        miLogout.setEnabled(false);
        labelUser.setText("Not logged in");
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, CARD_LOCKED);
    }

    public void applyConnectedState(String username, boolean isManager) {
        miLogin.setEnabled(false);
        miLogout.setEnabled(true);
        tabbedPane.setEnabledAt(TAB_EMPLOYEES, isManager);
        tabbedPane.setSelectedIndex(TAB_CASHIER);
        labelUser.setText(username + " [" + (isManager ? "Manager" : "Cashier") + "]");
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, CARD_MAIN);
    }

    public CashierPanel getCashierPanel() { return cashierPanel; }
    public ProductPanel getProductPanel() { return productPanel; }
    public TicketPanel getTicketPanel() { return ticketPanel; }
    public EmployeePanel getEmployeePanel() { return employeePanel; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }
    public JMenuItem getMiQuit() { return miQuit; }
    public JMenuItem getMiLogin() { return miLogin; }
    public JMenuItem getMiLogout() { return miLogout; }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();
            f.setVisible(true);
        });
    }
}