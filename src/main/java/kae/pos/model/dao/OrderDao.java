package kae.pos.model.dao;

import kae.pos.model.entity.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private static final String DEFAULT_FILE_PATH = "data/orders.dat";
    private List<Order> orders;
    private final String filePath;

    public OrderDao() {
        this(DEFAULT_FILE_PATH);
    }

    public OrderDao(String filePath) {
        this.filePath = filePath;
        this.orders = new ArrayList<>();
        load();
    }

    public void create(Order order) { orders.add(order); save(); }

    public Order findById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    public List<Order> findAll() { return new ArrayList<>(orders); }

    public void delete(int id) { orders.removeIf(o -> o.getId() == id); save(); }

    public void save() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(orders);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            orders = (List<Order>) ois.readObject();
            orders.stream().mapToInt(Order::getId).max()
                    .ifPresent(max -> Order.setCurrentId(max + 1));
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }
}