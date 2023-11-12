package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */


public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

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
    public Item getById(HttpServletRequest request) {
        return checkIfItemExists(Long.valueOf(request.getParameter("id")));
    }

    @Override
    public void update(HttpServletRequest request) {
        Item item = ItemMapper.extractItemFromRequest(request);
        checkIfItemExists(item.getId());
        if (item.getCategoryId() != null) {
            checkIfCategoryExists(item.getCategoryId());
        }
        itemRepository.update(item);
    }

    @Override
    public void delete(HttpServletRequest request) {
        Long id = Long.valueOf(request.getParameter("id"));
        checkIfItemExists(id);
        itemRepository.delete(id);
    }


    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    private void checkIfCategoryExists(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категория с id = %s " +
                        "не найдена. Проверьте правильность вводимых данных.", id)));
    }

    private Item checkIfItemExists(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Товар с id = %s " +
                        "не найден. Проверьте правильность вводимых данных.", id)));
    }

}
