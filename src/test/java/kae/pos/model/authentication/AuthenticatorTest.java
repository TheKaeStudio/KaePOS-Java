package kae.pos.model.authentication;

import kae.pos.model.entity.Employee;
import kae.pos.model.entity.Role;
import kae.pos.model.entity.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticatorTest {

    @Test
    void testAuthenticationSuccess() {

        Map<String, User> users = new HashMap<>();

        Employee admin = new Employee("admin", "1234", Role.MANAGER);

        users.put(admin.getUsername(), admin);

        Authenticator auth = new MapAuthenticator(users);

        assertTrue(auth.authenticate("admin", "1234"));
    }

    @Test
    void testAuthenticationFail() {

        Map<String, User> users = new HashMap<>();

        Employee admin = new Employee("admin", "1234", Role.MANAGER);

        users.put("admin", admin);

        Authenticator auth = new MapAuthenticator(users);

        assertFalse(auth.authenticate("admin", "wrong"));
    }
}