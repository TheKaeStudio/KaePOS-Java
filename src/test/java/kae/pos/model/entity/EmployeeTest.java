package kae.pos.model.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    void testEmployeeRole() {
        Employee e = new Employee("admin", "1234", Role.MANAGER);

        assertEquals(Role.MANAGER, e.getRole());
    }

    @Test
    void testInvalidUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee("ab", "1234", Role.CASHIER);
        });
    }
}