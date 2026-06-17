package kae.pos.model.dao;

import kae.pos.model.entity.Employee;
import kae.pos.model.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    private static final String DEFAULT_FILE_PATH = "data/employees.dat";
    private List<Employee> employees;
    private final String filePath;

    public EmployeeDao() {
        this(DEFAULT_FILE_PATH);
    }

    public EmployeeDao(String filePath) {
        this.filePath = filePath;
        this.employees = new ArrayList<>();
        load();
    }

    public void create(Employee employee) { employees.add(employee); save(); }

    public Employee findById(int id) {
        return employees.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public Employee findByUsername(String username) {
        return employees.stream().filter(e -> e.getUsername().equals(username)).findFirst().orElse(null);
    }

    public List<Employee> findAll() { return new ArrayList<>(employees); }

    public void update(Employee employee) {
        Employee existing = findById(employee.getId());
        if (existing != null) {
            existing.setUsername(employee.getUsername());
            existing.setRole(employee.getRole());
            save();
        }
    }

    public void delete(int id) { employees.removeIf(e -> e.getId() == id); save(); }

    public void save() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(employees);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            employees = (List<Employee>) ois.readObject();
            employees.stream().mapToInt(Employee::getId).max()
                    .ifPresent(max -> User.setCurrentId(max + 1));
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }
}