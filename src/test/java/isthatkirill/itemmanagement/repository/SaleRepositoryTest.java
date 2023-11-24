package isthatkirill.itemmanagement.repository;

import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.sale.Sale;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
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
class SaleRepositoryTest {

    private final MockedStatic<ConnectionHelper> connectionHelper = mockStatic(ConnectionHelper.class);
    private final SaleRepository saleRepository = new SaleRepository();
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
        Sale sale = Sale.builder().amount(100L).price(100.0).itemId(1L).build();
        Long generatedId = saleRepository.create(sale);
        assertThat(generatedId).isEqualTo(1);
    }

    @Test
    @Order(3)
    void findByIdTest() {
        Optional<Sale> sale = saleRepository.findById(1L);
        assertThat(sale).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("amount", 100L)
                .hasFieldOrPropertyWithValue("price", 100.0)
                .hasFieldOrPropertyWithValue("itemId", 1L);
    }

    @Test
    @Order(4)
    void findByIdNonExistentTest() {
        Optional<Sale> sale = saleRepository.findById(Long.MAX_VALUE);
        assertThat(sale).isNotPresent();
    }

    @Test
    @Order(5)
    void updateTest() {
        Sale sale = Sale.builder().id(1L).amount(200L).price(200.0).build();
        assertDoesNotThrow(() -> saleRepository.update(sale));
        getNewConnection();
        Optional<Sale> updated = saleRepository.findById(1L);
        assertThat(updated).isPresent()
                .get()
                .hasFieldOrPropertyWithValue("amount", 200L)
                .hasFieldOrPropertyWithValue("price", 200.0)
                .hasFieldOrPropertyWithValue("itemId", 1L);
    }

    @Test
    @Order(6)
    void findAllExtendedTest() {
        Sale sale = Sale.builder().amount(200L).price(450.0).itemId(1L).build();
        Long generatedId = saleRepository.create(sale);
        assertThat(generatedId).isEqualTo(2);
        getNewConnection();
        List<SaleExtended> sales = saleRepository.findAllExtended();
        assertThat(sales).hasSize(2)
                .extracting(Sale::getId)
                .containsExactly(1L, 2L);
        assertThat(sales.get(0)).isNotNull()
                .extracting(SaleExtended::getItemName)
                .isEqualTo("item_name");
    }

    @Test
    @Order(7)
    void findAverageSalePriceTest() {
        Double averagePrice = saleRepository.findAverageSalePrice(1L);
        assertThat(averagePrice).isEqualTo(325.0);
    }

    @Test
    @Order(8)
    void findAverageSalePriceForNonExistentItemTest() {
        Double averagePrice = saleRepository.findAverageSalePrice(Long.MAX_VALUE);
        assertThat(averagePrice).isZero();
    }

}