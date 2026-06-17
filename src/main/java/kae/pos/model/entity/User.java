package kae.pos.model.entity;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static int currentId = 1;

    protected int id;
    protected String username;
    protected String password;
    protected boolean passwordSet = true;

    public User(String username, String password) {
        this.id = currentId++;
        setUsername(username);
        setPassword(password);
    }

    public static void setCurrentId(int value) {
        currentId = value;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }

    public void setUsername(String username) {
        if (username == null || username.length() < 3) {
            throw new IllegalArgumentException("Invalid Username");
        }
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password too short");
        }
        this.password = password;
    }

    public boolean isPasswordSet() { return passwordSet; }
    public void setPasswordSet(boolean passwordSet) { this.passwordSet = passwordSet; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        return this.id == ((User) obj).id;
    }
}