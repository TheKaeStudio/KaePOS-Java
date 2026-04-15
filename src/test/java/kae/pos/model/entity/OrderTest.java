package kae.pos.model.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void testOrderTotal() {
        Category drinks = new Category("Drinks");

        Product coca = new Product("Coca", drinks, 2.0f);
        Product water = new Product("Water", drinks, 1.0f);

        Order order = new Order();

        order.addItem(coca, 2);
        order.addItem(water, 3);

        assertEquals(7.0f, order.getTotalPrice());
    }
}