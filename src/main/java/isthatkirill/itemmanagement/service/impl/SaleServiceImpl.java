package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.exception.NotEnoughItemException;
import isthatkirill.itemmanagement.mapper.SaleMapper;
import isthatkirill.itemmanagement.model.sale.Sale;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SaleRepository;
import isthatkirill.itemmanagement.service.SaleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static isthatkirill.itemmanagement.util.Constants.CHECK_DATA;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class SaleServiceImpl implements SaleService {

    private final ItemRepository itemRepository;
    private final SaleRepository saleRepository;

    @Inject
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

    @Override
    public void update(HttpServletRequest request) {
        Sale sale = SaleMapper.extractSaleFromRequest(request);
        Sale oldSale = checkIfSaleExistsAndGet(sale.getId());
        Long itemId = oldSale.getItemId();
        Long amountDifference = sale.getAmount() - oldSale.getAmount();
        if (amountDifference > 0) {
            checkIfEnoughItems(itemId, amountDifference);
        }
        saleRepository.update(sale);
        recalculateItemFields(itemId, amountDifference);
    }


    @Override
    public List<SaleExtended> getAllExtended() {
        return saleRepository.findAllExtended();
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

    private Sale checkIfSaleExistsAndGet(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Продажа с id = %s " +
                        "не найден. %s", CHECK_DATA, saleId)));
    }

    private void checkIfItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new EntityNotFoundException(String.format("Товар с id = %s " +
                    "не найден. %s", CHECK_DATA, itemId));
        }
    }
}
