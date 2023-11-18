package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SupplyRepository;
import isthatkirill.itemmanagement.service.SupplyService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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
        recalculateItemFields(itemId, supply.getAmount());
        return generatedId;
    }

    @Override
    public void update(HttpServletRequest request) {
        Supply supply = SupplyMapper.extractSupplyFromRequest(request);
        Supply oldSupply = checkIfSupplyExistsAndGet(supply.getId());
        supplyRepository.update(supply);
        recalculateItemFields(oldSupply.getItemId(), 0L);
    }

    @Override
    public List<SupplyExtended> getAllExtended() {
        return supplyRepository.findAllExtended();
    }

    private void recalculateItemFields(Long itemId, Long amount) {
        Double currentAveragePrice = supplyRepository.findAveragePurchasePrice(itemId);
        itemRepository.updateWithNewSupplyData(currentAveragePrice, Math.toIntExact(amount), itemId);
    }

    private void checkIfItemExists(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Товар с id = %s " +
                    "не найден. Проверьте правильность вводимых данных.", id));
        }
    }

    private Supply checkIfSupplyExistsAndGet(Long id) {
        return supplyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Поступление с id = %s " +
                        "не найдено. Проверьте правильность вводимых данных.", id)));
    }

}
