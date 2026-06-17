package kae.pos.model.entity;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {

    private static int currentId = 1;

    private int id;
    private String name;

    public Category(String name) {
        this.id = currentId++;
        setName(name);
    }

    public static void setCurrentId(int value) {
        currentId = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Invalid Category Name");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}