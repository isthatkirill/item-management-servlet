package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.mapper.ItemMapper;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.repository.ItemRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */


public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public Long createItem(HttpServletRequest request) {
        // TODO CHECK IF CATEGOERY EXISTS
        Item item = ItemMapper.extractItemFromRequest(request);
        item.setCreatedAt(LocalDateTime.now());
        if (item.getStockUnits() == null) item.setStockUnits(0);
        if (item.getBrand() == null) item.setBrand("N/A");
        if (item.getCategoryId() == null) item.setCategoryId(-1L);
        return itemRepository.create(item);
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

}
