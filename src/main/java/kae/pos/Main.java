package kae.pos;

import kae.pos.model.authentication.Authenticator;
import kae.pos.model.authentication.MapAuthenticator;
import kae.pos.model.entity.*;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Map<String, User> users = new HashMap<>();
    private static final List<Category> categories = new ArrayList<>();
    private static final List<Product> products = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n1. Login test");
            System.out.println("2. Create employee");
            System.out.println("3. Create category");
            System.out.println("4. Create product");
            System.out.println("5. Create order");
            System.out.println("6. List products");
            System.out.println("7. List orders");
            System.out.println("0. Quit");

            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> testLogin();
                case 2 -> createEmployee();
                case 3 -> createCategory();
                case 4 -> createProduct();
                case 5 -> createOrder();
                case 6 -> listProducts();
                case 7 -> listOrders();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void seedData() {
        Employee admin = new Employee("admin", "1234", Role.MANAGER);
        users.put(admin.getUsername(), admin);
    }

    private static void createEmployee() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("Choose role:");
        for (Role r : Role.values()) {
            System.out.println("- " + r);
        }

        String roleInput = scanner.nextLine().toUpperCase();
        Role role;

        try {
            role = Role.valueOf(roleInput);
        } catch (Exception e) {
            System.out.println("Invalid role");
            return;
        }

        Employee e = new Employee(username, password, role);
        users.put(username, e);

        System.out.println("Employee created: " + e);
    }

    private static void testLogin() {
        Authenticator auth = new MapAuthenticator(users);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean result = auth.authenticate(username, password);

        System.out.println(result ? "Login success" : "Login failed");
    }

    private static void createCategory() {
        System.out.print("Category name: ");
        String name = scanner.nextLine();

        Category c = new Category(name);
        categories.add(c);

        System.out.println("Created: " + c);
    }

    private static void createProduct() {
        if (categories.isEmpty()) {
            System.out.println("No categories yet");
            return;
        }

        System.out.print("Product name: ");
        String name = scanner.nextLine();

        System.out.print("Price: ");
        float price = Float.parseFloat(scanner.nextLine());

        System.out.println("Choose category:");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println(i + ". " + categories.get(i).getName());
        }

        int index = Integer.parseInt(scanner.nextLine());

        Category cat = categories.get(index);

        Product p = new Product(name, cat, price);

        products.add(p);

        System.out.println("Product created: " + p);
    }

    private static void createOrder() {

        Order order = new Order();

        while (true) {

            System.out.println("\n--- Add product to order ---");
            listProducts();

            System.out.println("Enter product index (-1 to finish) : ");
            int index = Integer.parseInt(scanner.nextLine());

            if (index == -1) break;

            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());

            Product p = products.get(index);

            order.addItem(p, qty);
        }

        orders.add(order);

        System.out.println("Order created:");
        System.out.println(order);
        System.out.println("Total : " + order.getTotalPrice());
    }

    private static void listProducts() {
        for (int i = 0; i < products.size(); i++) {
            System.out.println(i + ". " + products.get(i));
        }
    }

    private static void listOrders() {
        for (Order o : orders) {
            System.out.println(o);
        }
    }
}