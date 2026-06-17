package kae.pos.model.dao;

import kae.pos.model.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDaoTest {

    @TempDir
    File tempDir;

    private String filePath;
    private CategoryDao dao;

    @BeforeEach
    void setUp() {
        filePath = new File(tempDir, "categories.dat").getPath();
        dao = new CategoryDao(filePath);
    }

    @Test
    void create_addsCategory() {
        Category category = new Category("Drinks");
        dao.create(category);

        assertEquals(1, dao.findAll().size());
        assertEquals("Drinks", dao.findById(category.getId()).getName());
    }

    @Test
    void update_changesName() {
        Category category = new Category("Drinks");
        dao.create(category);

        category.setName("Beverages");
        dao.update(category);

        assertEquals("Beverages", dao.findById(category.getId()).getName());
    }

    @Test
    void delete_removesCategory() {
        Category category = new Category("Drinks");
        dao.create(category);

        dao.delete(category.getId());

        assertNull(dao.findById(category.getId()));
    }

    @Test
    void load_recoversPersistedDataAfterRestart() {
        dao.create(new Category("Drinks"));

        CategoryDao reloaded = new CategoryDao(filePath);

        assertEquals(1, reloaded.findAll().size());
        assertEquals("Drinks", reloaded.findAll().get(0).getName());
    }
}