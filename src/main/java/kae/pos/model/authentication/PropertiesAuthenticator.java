package kae.pos.model.authentication;

import java.io.*;
import java.util.Properties;

public class PropertiesAuthenticator extends Authenticator {

    private final String filePath;
    private final Properties properties = new Properties();

    public PropertiesAuthenticator(String filePath) {
        this.filePath = filePath;
        load();
    }

    private void load() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (InputStream is = new FileInputStream(file)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void persist() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (OutputStream os = new FileOutputStream(file)) {
            properties.store(os, "User credentials");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String username, String password) {
        properties.setProperty(username, password);
        persist();
    }

    public void setPassword(String username, String password) {
        properties.setProperty(username, password);
        persist();
    }

    public void removeUser(String username) {
        properties.remove(username);
        persist();
    }

    @Override
    protected boolean isLoginExists(String username) {
        return properties.containsKey(username);
    }

    @Override
    protected String getPassword(String username) {
        return properties.getProperty(username);
    }
}