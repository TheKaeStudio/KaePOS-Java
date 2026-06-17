package kae.pos.model.dao;

import kae.pos.model.entity.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private static final String DEFAULT_FILE_PATH = "data/categories.dat";

    private final String filePath;
    private List<Category> categories;

    public CategoryDao() {
        this(DEFAULT_FILE_PATH);
    }

    public CategoryDao(String filePath) {
        this.filePath = filePath;
        this.categories = new ArrayList<>();
        load();
    }

    public void create(Category category) {
        categories.add(category);
        save();
    }

    public Category findById(int id) {
        for (Category c : categories) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public List<Category> findAll() {
        return new ArrayList<>(categories);
    }

    public void update(Category category) {
        Category existing = findById(category.getId());
        if (existing != null) {
            existing.setName(category.getName());
            save();
        }
    }

    public void delete(int id) {
        categories.removeIf(c -> c.getId() == id);
        save();
    }

    public void save() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            categories = (List<Category>) ois.readObject();
            categories.stream()
                    .mapToInt(Category::getId)
                    .max()
                    .ifPresent(max -> Category.setCurrentId(max + 1));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}