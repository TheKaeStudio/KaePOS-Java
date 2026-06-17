package kae.pos.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    private static int currentId = 1;

    private int id;
    private String name;
    private Category category;
    private float price;
    private boolean stock;
    private String image;

    public Product(String name, Category category, float price) {
        this.id = currentId++;
        this.name = name;
        setCategory(category);
        setPrice(price);
        setStock(true);
        this.image = null;
    }

    public static void setCurrentId(int value) {
        currentId = value;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public float getPrice() { return price; }
    public boolean getStock() { return stock; }
    public String getImage() { return image; }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Undefined Category");
        }
        this.category = category;
    }

    public void setPrice(float price) {
        if (price < 0) {
            throw new IllegalArgumentException("Invalid Price");
        }
        this.price = price;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}