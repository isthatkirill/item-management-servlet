package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.category.Category;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface CategoryService {

    Long create(HttpServletRequest request);

    Category getById(HttpServletRequest request);

    void update(HttpServletRequest request);

    void deleteById(Long id);

    List<Category> getAll();

}
