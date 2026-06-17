package kae.pos.model.dao;

import kae.pos.model.entity.Category;
import kae.pos.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    @TempDir
    File tempDir;

    private ProductDao dao;
    private Category category;

    @BeforeEach
    void setUp() {
        dao = new ProductDao(new File(tempDir, "products.dat").getPath());
        category = new Category("Drinks");
    }

    @Test
    void create_addsProduct() {
        Product product = new Product("Coca-Cola", category, 1.80f);
        dao.create(product);

        assertEquals(1, dao.findAll().size());
        assertEquals("Coca-Cola", dao.findById(product.getId()).getName());
    }

    @Test
    void update_changesPriceAndStock() {
        Product product = new Product("Coca-Cola", category, 1.80f);
        dao.create(product);

        product.setPrice(2.00f);
        product.setStock(false);
        dao.update(product);

        Product updated = dao.findById(product.getId());
        assertEquals(2.00f, updated.getPrice());
        assertFalse(updated.getStock());
    }

    @Test
    void delete_removesProduct() {
        Product product = new Product("Coca-Cola", category, 1.80f);
        dao.create(product);

        dao.delete(product.getId());

        assertNull(dao.findById(product.getId()));
    }
}