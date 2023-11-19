package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.model.item.ItemShort;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface ItemService {

    Long create(HttpServletRequest request);

    ItemExtended getById(HttpServletRequest request);

    void update(HttpServletRequest request);

    void deleteById(Long id);

    List<ItemExtended> getAllExtended(String sortBy, String sortOrder);

    List<ItemShort> getAllShort();

}
