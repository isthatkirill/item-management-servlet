package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.model.category.Category;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static isthatkirill.itemmanagement.util.Constants.CREATE_TABLE_TEST;
import static isthatkirill.itemmanagement.util.Constants.DROP_TABLE_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

/**
 * @author Kirill Emelyanov
 */

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CategoryRepositoryTest {

    private final MockedStatic<ConnectionHelper> connectionHelper = mockStatic(ConnectionHelper.class);
    private final CategoryRepository categoryRepository = new CategoryRepository();

    @BeforeEach
    @SneakyThrows
    void getNewConnection() {
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(DriverManager.getConnection("jdbc:h2:~/test", "sa", ""));
    }

    @AfterAll
    void closeStaticMock() {
        connectionHelper.close();
    }

    @Test
    @Order(1)
    void createTables() {
        assertDoesNotThrow(() -> {
            PreparedStatement psDrop = ConnectionHelper.getConnection().prepareStatement(DROP_TABLE_TEST);
            psDrop.executeUpdate();
            PreparedStatement psCreate = ConnectionHelper.getConnection().prepareStatement(CREATE_TABLE_TEST);
            psCreate.executeUpdate();
        });
    }

    @Test
    @Order(2)
    void createTest() {
        Category category = Category.builder().name("category_one").description("test_category_one").build();
        Long generatedId = categoryRepository.create(category);
        assertThat(generatedId).isEqualTo(1);
    }

    @Test
    @Order(3)
    void findByIdTest() {
        Optional<Category> category = categoryRepository.findById(1L);
        assertThat(category).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("name", "category_one")
                .hasFieldOrPropertyWithValue("description", "test_category_one");
    }

    @Test
    @Order(4)
    void findNonExistentByIdTest() {
        Optional<Category> category = categoryRepository.findById(2L);
        assertThat(category).isNotPresent();
    }

    @Test
    @Order(5)
    void existsByIdTest() {
        boolean isExists = categoryRepository.existsById(1L);
        assertThat(isExists).isTrue();
    }

    @Test
    @Order(6)
    void existsFalseByIdTest() {
        boolean isExists = categoryRepository.existsById(2L);
        assertThat(isExists).isFalse();
    }

    @Test
    @Order(7)
    void updateTest() {
        Category category = Category.builder().id(1L).name("category_one_updated").description("test_updated").build();
        assertDoesNotThrow(() -> categoryRepository.update(category));
        getNewConnection();
        Optional<Category> updated = categoryRepository.findById(1L);
        assertThat(updated).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("name", "category_one_updated")
                .hasFieldOrPropertyWithValue("description", "test_updated");
    }

    @Test
    @Order(8)
    void findAllTest() {
        Category category = Category.builder().name("category_two").description("test_category_two").build();
        Long generatedId = categoryRepository.create(category);
        assertThat(generatedId).isEqualTo(2);
        getNewConnection();
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2)
                .extracting(Category::getId)
                .containsExactly(1L, 2L);
    }

    @Test
    @Order(9)
    void deleteTest() {
        assertDoesNotThrow(() -> categoryRepository.delete(1L));
        getNewConnection();
        Optional<Category> updated = categoryRepository.findById(1L);
        assertThat(updated).isNotPresent();
    }
}