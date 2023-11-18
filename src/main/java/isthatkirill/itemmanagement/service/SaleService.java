package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface SaleService {

    Long create(HttpServletRequest request);

    void update(HttpServletRequest request);

    List<SaleExtended> getAllExtended();

}
