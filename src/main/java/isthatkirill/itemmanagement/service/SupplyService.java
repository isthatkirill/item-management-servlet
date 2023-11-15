package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.model.SupplyExtended;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface SupplyService {

    Long create(HttpServletRequest request);

    void update(HttpServletRequest request);

    List<SupplyExtended> getAllExtended();

}
