package kae.pos.model.entity;

import java.util.Objects;

public abstract class User {
    private static int currentId = 1;

    protected int id;
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.id = currentId++;
        setUsername(username);
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.length() < 3) {
            throw new IllegalArgumentException("Invalid Username");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password too short");
        }
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}