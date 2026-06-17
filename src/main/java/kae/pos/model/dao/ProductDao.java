package kae.pos.model.dao;

import kae.pos.model.entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static final String DEFAULT_FILE_PATH = "data/products.dat";
    private List<Product> products;
    private final String filePath;

    public ProductDao() {
        this(DEFAULT_FILE_PATH);
    }

    public ProductDao(String filePath) {
        this.filePath = filePath;
        this.products = new ArrayList<>();
        load();
    }

    public void create(Product product) { products.add(product); save(); }

    public Product findById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Product> findAll() { return new ArrayList<>(products); }

    public void update(Product product) {
        Product existing = findById(product.getId());
        if (existing != null) {
            existing.setName(product.getName());
            existing.setCategory(product.getCategory());
            existing.setPrice(product.getPrice());
            existing.setStock(product.getStock());
            save();
        }
    }

    public void delete(int id) { products.removeIf(p -> p.getId() == id); save(); }

    public void save() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(products);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            products = (List<Product>) ois.readObject();
            products.stream().mapToInt(Product::getId).max()
                    .ifPresent(max -> Product.setCurrentId(max + 1));
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }
}