package isthatkirill.itemmanagement.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public interface ReportService {

    byte[] process(HttpServletRequest request);

}
