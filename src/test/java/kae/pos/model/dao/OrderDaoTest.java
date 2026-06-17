package kae.pos.model.dao;

import kae.pos.model.entity.Category;
import kae.pos.model.entity.Order;
import kae.pos.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoTest {

    @TempDir
    File tempDir;

    private OrderDao dao;
    private Product product;

    @BeforeEach
    void setUp() {
        dao = new OrderDao(new File(tempDir, "orders.dat").getPath());
        product = new Product("Coca-Cola", new Category("Drinks"), 1.80f);
    }

    @Test
    void create_addsOrderWithCorrectTotal() {
        Order order = new Order();
        order.addItem(product, 3);
        dao.create(order);

        assertEquals(1, dao.findAll().size());
        assertEquals(5.40f, dao.findById(order.getId()).getTotalPrice(), 0.001f);
    }

    @Test
    void delete_removesOrder() {
        Order order = new Order();
        order.addItem(product, 1);
        dao.create(order);

        dao.delete(order.getId());

        assertNull(dao.findById(order.getId()));
    }
}