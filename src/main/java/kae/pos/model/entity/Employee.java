package kae.pos.model.entity;

public class Employee extends User {

    private Role role;

    public Employee(String username, String password, Role role) {
        super(username, password);
        setRole(role);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    public boolean isManager() {
        return this.role == Role.MANAGER;
    }

    public boolean isCashier() {
        return this.role == Role.CASHIER;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}