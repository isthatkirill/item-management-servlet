package isthatkirill.itemmanagement.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public interface CategoryService {

    Long createCategory(HttpServletRequest request);

}
