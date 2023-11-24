package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
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
class SupplyRepositoryTest {

    private final MockedStatic<ConnectionHelper> connectionHelper = mockStatic(ConnectionHelper.class);
    private final SupplyRepository supplyRepository = new SupplyRepository();
    private final ItemRepository itemRepository = new ItemRepository();

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
        Item item = Item.builder().name("item_name").description("item_description")
                .createdAt(LocalDateTime.now()).stockUnits(0).build();
        itemRepository.create(item);
        getNewConnection();
        Supply supply = Supply.builder().amount(10L).price(15.5).company("company")
                .itemId(1L).build();
        Long generatedId = supplyRepository.create(supply);
        assertThat(generatedId).isEqualTo(1);
    }

    @Test
    @Order(3)
    void findByIdTest() {
        Optional<Supply> supply = supplyRepository.findById(1L);
        assertThat(supply).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("amount", 10L)
                .hasFieldOrPropertyWithValue("price", 15.5)
                .hasFieldOrPropertyWithValue("company", "company")
                .hasFieldOrProperty("createdAt");
    }

    @Test
    @Order(4)
    void findByIdNonExistentTest() {
        Optional<Supply> supply = supplyRepository.findById(Long.MAX_VALUE);
        assertThat(supply).isNotPresent();
    }

    @Test
    @Order(5)
    @SneakyThrows
    void updateTest() {
        Supply supply = Supply.builder().id(1L).company("new_company").price(20.0).build();
        assertDoesNotThrow(() -> supplyRepository.update(supply));
        getNewConnection();
        Optional<Supply> updated = supplyRepository.findById(1L);
        assertThat(updated).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("amount", 10L)
                .hasFieldOrPropertyWithValue("price", 20.0)
                .hasFieldOrPropertyWithValue("company", "new_company")
                .hasFieldOrProperty("createdAt");
    }

    @Test
    @Order(6)
    void findAllExtendedTest() {
        Supply supply = Supply.builder().amount(10L).price(30.0).company("company")
                .itemId(1L).build();
        Long generatedId = supplyRepository.create(supply);
        assertThat(generatedId).isEqualTo(2);
        getNewConnection();
        List<SupplyExtended> supplies = supplyRepository.findAllExtended();
        assertThat(supplies).hasSize(2)
                .extracting(Supply::getId)
                .containsExactly(1L, 2L);
        assertThat(supplies.get(0)).isNotNull()
                .extracting(SupplyExtended::getItemName)
                .isEqualTo("item_name");
    }

    @Test
    @Order(7)
    void findAveragePurchasePriceTest() {
        Double averagePrice = supplyRepository.findAveragePurchasePrice(1L);
        assertThat(averagePrice).isEqualTo(25);
    }

    @Test
    @Order(8)
    void findAveragePurchasePriceForNonExistentItemTest() {
        Double averagePrice = supplyRepository.findAveragePurchasePrice(Long.MAX_VALUE);
        assertThat(averagePrice).isZero();
    }

}