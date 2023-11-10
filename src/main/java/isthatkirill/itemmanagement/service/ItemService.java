package isthatkirill.itemmanagement.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public interface ItemService {

    void createItem(HttpServletRequest request);

}
