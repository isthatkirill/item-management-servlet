package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.exception.NotEnoughItemException;
import isthatkirill.itemmanagement.mapper.SaleMapper;
import isthatkirill.itemmanagement.model.sale.Sale;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SaleRepository;
import isthatkirill.itemmanagement.service.SaleService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public class SaleServiceImpl implements SaleService {

    private SaleRepository saleRepository;
    private ItemRepository itemRepository;

    public SaleServiceImpl(SaleRepository saleRepository, ItemRepository itemRepository) {
        this.saleRepository = saleRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Long create(HttpServletRequest request) {
        Sale sale = SaleMapper.extractSaleFromRequest(request);
        Long itemId = sale.getItemId();
        checkIfItemExists(itemId);
        checkIfEnoughItems(itemId, sale.getAmount());
        Long generatedId = saleRepository.create(sale);
        recalculateItemFields(itemId, sale.getAmount());
        return generatedId;
    }

    private void recalculateItemFields(Long itemId, Long amount) {
        Double currentAveragePrice = saleRepository.findAverageSalePrice(itemId);
        itemRepository.updateWithNewSaleData(currentAveragePrice, Math.toIntExact(amount), itemId);
    }

    private void checkIfEnoughItems(Long itemId, Long amount) {
        Long stock = itemRepository.findStockById(itemId);
        if (stock < amount) {
            throw new NotEnoughItemException("Недостаточно товаров. Количество на складе: " + stock);
        }
    }

    private void checkIfItemExists(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Товар с id = %s " +
                    "не найден. Проверьте правильность вводимых данных.", id));
        }
    }
}
