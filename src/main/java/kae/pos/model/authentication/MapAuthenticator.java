package kae.pos.model.authentication;

import kae.pos.model.entity.User;
import java.util.Map;

public class MapAuthenticator extends Authenticator {

    private final Map<String, User> users;

    public MapAuthenticator(Map<String, User> users) {
        this.users = users;
    }

    @Override
    protected boolean isLoginExists(String username) {
        return users.containsKey(username);
    }

    @Override
    protected String getPassword(String username) {
        User user = users.get(username);
        return (user != null) ? user.getPassword() : null;
    }
}