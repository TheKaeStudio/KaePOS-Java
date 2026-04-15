package kae.pos.model.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    void testCategoryEquals() {
        Category c1 = new Category("Food");
        Category c2 = new Category("Food");

        assertNotEquals(c1, c2);
    }
}
