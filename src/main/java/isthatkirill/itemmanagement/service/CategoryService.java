package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.Category;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public interface CategoryService {

    Long create(HttpServletRequest request);

    Category getById(HttpServletRequest request);

    void update(HttpServletRequest request);

    void delete(HttpServletRequest request);

}
