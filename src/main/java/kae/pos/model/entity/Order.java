package kae.pos.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {

    private static int currentId = 1;

    private int id;
    private List<OrderItem> items;
    private LocalDate date;

    public Order() {
        this.id = currentId++;
        this.items = new ArrayList<>();
        this.date = LocalDate.now();
    }

    public static void setCurrentId(int value) {
        currentId = value;
    }

    public int getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    public void removeProduct(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
    }

    public float getTotalPrice() {
        float total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", total=" + getTotalPrice() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;

        Order other = (Order) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}