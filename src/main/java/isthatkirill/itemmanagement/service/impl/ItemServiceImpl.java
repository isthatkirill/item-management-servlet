package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ItemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

import static isthatkirill.itemmanagement.util.Constants.CHECK_DATA;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Inject
    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long create(HttpServletRequest request) {
        Item item = ItemMapper.extractItemFromRequest(request);
        item.setCreatedAt(LocalDateTime.now());
        if ((item.getCategoryId() != null)) {
            checkIfCategoryExists(item.getCategoryId());
        }
        item.setStockUnits(0);
        return itemRepository.create(item);
    }

    @Override
    public ItemExtended getById(HttpServletRequest request) {
        return checkIfItemExistsAndGet(Long.valueOf(request.getParameter("id")));
    }

    @Override
    public void update(HttpServletRequest request) {
        Item item = ItemMapper.extractItemFromRequest(request);
        checkIfItemExistsAndGet(item.getId());
        if (item.getCategoryId() != null) {
            checkIfCategoryExists(item.getCategoryId());
        }
        itemRepository.update(item);
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemExtended> getAllExtended(String sortBy, String sortOrder) {
        return itemRepository.findAllExtended(sortBy, sortOrder);
    }

    @Override
    public List<ItemShort> getAllShort() {
        return itemRepository.findAllShort();
    }

    private void checkIfCategoryExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Категория с id = %s " +
                    "не найдена. %s", CHECK_DATA, id));
        }
    }

    private ItemExtended checkIfItemExistsAndGet(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Товар с id = %s " +
                        "не найден. %s", CHECK_DATA, id)));
    }

    private void checkIfItemExists(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Товар с id = %s " +
                    "не найден. %s", CHECK_DATA, id));
        }
    }


}
