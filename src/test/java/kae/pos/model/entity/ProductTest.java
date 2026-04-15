package kae.pos.model.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void testProductCreation() {
        Category drinks = new Category("Drinks");

        Product p = new Product("Coca", drinks, 2.5f);

        assertEquals("Coca", p.getName());
        assertEquals(drinks, p.getCategory());
        assertEquals(2.5f, p.getPrice());
    }

    @Test
    void testAutoIncrementId() {
        Category drinks = new Category("Drinks");

        Product p1 = new Product("Coca", drinks, 1f);
        Product p2 = new Product("Pepsi", drinks, 1f);

        assertTrue(p2.getId() > p1.getId());
    }
}