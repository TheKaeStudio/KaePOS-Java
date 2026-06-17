package kae.pos.model.dao;

import kae.pos.model.entity.Employee;
import kae.pos.model.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoTest {

    @TempDir
    File tempDir;

    private EmployeeDao dao;

    @BeforeEach
    void setUp() {
        dao = new EmployeeDao(new File(tempDir, "employees.dat").getPath());
    }

    @Test
    void create_addsEmployee() {
        Employee employee = new Employee("alice", "azerty1", Role.CASHIER);
        dao.create(employee);

        assertEquals(1, dao.findAll().size());
        assertEquals("alice", dao.findByUsername("alice").getUsername());
    }

    @Test
    void update_changesRole() {
        Employee employee = new Employee("alice", "azerty1", Role.CASHIER);
        dao.create(employee);

        employee.setRole(Role.MANAGER);
        dao.update(employee);

        assertEquals(Role.MANAGER, dao.findById(employee.getId()).getRole());
    }

    @Test
    void delete_removesEmployee() {
        Employee employee = new Employee("alice", "azerty1", Role.CASHIER);
        dao.create(employee);

        dao.delete(employee.getId());

        assertNull(dao.findById(employee.getId()));
    }
}