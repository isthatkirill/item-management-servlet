package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.SupplyMapper;
import isthatkirill.itemmanagement.model.supply.Supply;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SupplyRepository;
import isthatkirill.itemmanagement.service.SupplyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static isthatkirill.itemmanagement.util.Constants.CHECK_DATA;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final ItemRepository itemRepository;

    @Inject
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
                    "не найден. %s", CHECK_DATA, id));
        }
    }

    private Supply checkIfSupplyExistsAndGet(Long id) {
        return supplyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Поступление с id = %s " +
                        "не найдено. %s", CHECK_DATA, id)));
    }

}
