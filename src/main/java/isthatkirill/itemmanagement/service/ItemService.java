package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Item;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface ItemService {

    Long create(HttpServletRequest request);

    Item getById(HttpServletRequest request);

    List<Item> getAll();

}
