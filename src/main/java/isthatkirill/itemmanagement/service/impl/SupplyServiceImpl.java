package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.model.Supply;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SupplyRepository;
import isthatkirill.itemmanagement.service.SupplyService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public class SupplyServiceImpl implements SupplyService {

    private SupplyRepository supplyRepository;
    private ItemRepository itemRepository;

    public SupplyServiceImpl(SupplyRepository supplyRepository, ItemRepository itemRepository) {
        this.supplyRepository = supplyRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Long create(HttpServletRequest request) {
        Supply supply = SupplyMapper.extractSupplyFromRequest(request);
        Long itemId = supply.getItemId();
        checkIfItemExists(itemId);
        Long generatedId = supplyRepository.create(supply);
        recalculateItemFields(itemId);
        return generatedId;
    }

    @Override
    public void update(HttpServletRequest request) {
        Supply supply = SupplyMapper.extractSupplyFromRequest(request);
        Supply oldSupply = checkIfSupplyExists(supply.getId());
        supplyRepository.update(supply);
        recalculateItemFields(oldSupply.getItemId());
    }

    private void recalculateItemFields(Long itemId) {
        //TODO SUPPLIES - SALES = STOCK UNITS
        Double currentAveragePrice = supplyRepository.findAveragePurchasePrice(itemId);
        Integer stockUnits = supplyRepository.findAllUnits(itemId);
        itemRepository.update(currentAveragePrice, stockUnits, itemId);
    }


    private Item checkIfItemExists(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Товар с id = %s " +
                        "не найден. Проверьте правильность вводимых данных.", id)));
    }

    private Supply checkIfSupplyExists(Long id) {
        return supplyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Поступление с id = %s " +
                        "не найдено. Проверьте правильность вводимых данных.", id)));
    }

}
