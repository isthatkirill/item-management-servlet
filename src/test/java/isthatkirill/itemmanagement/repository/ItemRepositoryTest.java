package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.model.category.Category;
import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.repository.util.ConnectionHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
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
class ItemRepositoryTest {

    private final MockedStatic<ConnectionHelper> connectionHelper = mockStatic(ConnectionHelper.class);
    private final ItemRepository itemRepository = new ItemRepository();
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
        categoryRepository.create(category);
        getNewConnection();
        Item item = Item.builder().name("item_1").description("desc_1").stockUnits(0)
                .brand("brand").createdAt(LocalDateTime.now()).categoryId(1L).build();
        Long generatedId = itemRepository.create(item);
        assertThat(generatedId).isEqualTo(1L);
    }

    @Test
    @Order(3)
    void createWithPossibleNullFieldsTest() {
        Item item = Item.builder().name("item_2").description("desc_2").stockUnits(0)
                .createdAt(LocalDateTime.now()).build();
        Long generatedId = itemRepository.create(item);
        assertThat(generatedId).isEqualTo(2L);
    }

    @Test
    @Order(4)
    void findByIdTest() {
        Optional<ItemExtended> item = itemRepository.findById(1L);
        assertThat(item).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "item_1")
                .hasFieldOrPropertyWithValue("description", "desc_1")
                .hasFieldOrPropertyWithValue("stockUnits", 0)
                .hasFieldOrPropertyWithValue("brand", "brand")
                .hasFieldOrPropertyWithValue("brand", "brand")
                .hasFieldOrPropertyWithValue("averagePurchasePrice", 0.0)
                .hasFieldOrPropertyWithValue("averageSalePrice", 0.0)
                .hasFieldOrPropertyWithValue("categoryId", 1L)
                .hasFieldOrPropertyWithValue("categoryName", "category_one")
                .hasFieldOrProperty("createdAt");
    }

    @Test
    @Order(5)
    void findByIdNonExistentTest() {
        Optional<ItemExtended> item = itemRepository.findById(Long.MAX_VALUE);
        assertThat(item).isNotPresent();
    }

    @Test
    @Order(6)
    void existsByIdTest() {
        boolean isExists = itemRepository.existsById(1L);
        assertThat(isExists).isTrue();
    }

    @Test
    @Order(7)
    void existsFalseByIdTest() {
        boolean isExists = itemRepository.existsById(Long.MAX_VALUE);
        assertThat(isExists).isFalse();
    }

    @Test
    @Order(8)
    void findAllShortTest() {
        List<ItemShort> items = itemRepository.findAllShort();
        assertThat(items).hasSize(2)
                .extracting(ItemShort::getId)
                .containsExactly(1L, 2L);

        assertThat(items)
                .element(0)
                .hasOnlyFields("id", "name");
    }

    @Test
    @Order(9)
    void findAllExtendedTest() {
        List<ItemExtended> items = itemRepository.findAllExtended(null, null);
        assertThat(items).hasSize(2)
                .extracting(Item::getId)
                .containsExactly(1L, 2L);

        assertThat(items)
                .element(0)
                .extracting(ItemExtended::getCategoryName)
                .isEqualTo("category_one");
    }

    @Test
    @Order(10)
    void findAllExtendedSortByNameDescTest() {
        List<ItemExtended> items = itemRepository.findAllExtended("name", "desc");
        assertThat(items).hasSize(2)
                .extracting(Item::getId)
                .containsExactly(2L, 1L);
    }

    @Test
    @Order(11)
    void updateTest() {
        Item item = Item.builder().id(2L).description("item_2_new_description").build();
        assertDoesNotThrow(() -> itemRepository.update(item));
        getNewConnection();
        Optional<ItemExtended> updated = itemRepository.findById(2L);
        assertThat(updated).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("description", "item_2_new_description");
    }

    @Test
    @Order(12)
    void updateWithNewSupplyDataTest() {
        Double averagePrice = 150.0;
        Integer stockUnits = 10;
        Long itemId = 2L;
        assertDoesNotThrow(() -> itemRepository.updateWithNewSupplyData(averagePrice, stockUnits, itemId));
        getNewConnection();
        Optional<ItemExtended> updated = itemRepository.findById(itemId);
        assertThat(updated).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", itemId)
                .hasFieldOrPropertyWithValue("stockUnits", stockUnits)
                .hasFieldOrPropertyWithValue("averagePurchasePrice", averagePrice);
    }

    @Test
    @Order(13)
    void updateWithNewSaleDataTest() {
        Double averagePrice = 160.0;
        Integer stockUnits = 6;
        Long itemId = 2L;
        assertDoesNotThrow(() -> itemRepository.updateWithNewSaleData(averagePrice, stockUnits, itemId));
        getNewConnection();
        Optional<ItemExtended> updated = itemRepository.findById(itemId);
        assertThat(updated).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", itemId)
                .hasFieldOrPropertyWithValue("stockUnits", 4)
                .hasFieldOrPropertyWithValue("averageSalePrice", averagePrice);
    }

    @Test
    @Order(14)
    void findStockByIdTest() {
        Long unitsStock = itemRepository.findStockById(2L);
        assertThat(unitsStock)
                .isEqualTo(4L);
    }


    @Test
    void deleteTest() {
        assertDoesNotThrow(() -> itemRepository.delete(2L));
        getNewConnection();
        Optional<ItemExtended> item = itemRepository.findById(2L);
        assertThat(item).isNotPresent();
    }

}