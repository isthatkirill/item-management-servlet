package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.ItemExtended;
import isthatkirill.itemmanagement.model.ItemShort;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface ItemService {

    Long create(HttpServletRequest request);

    ItemExtended getById(HttpServletRequest request);

    void update(HttpServletRequest request);

    void deleteById(HttpServletRequest request);

    void deleteButton(Long id);

    List<ItemExtended> getAllExtended();

    List<ItemShort> getAllShort();

}
