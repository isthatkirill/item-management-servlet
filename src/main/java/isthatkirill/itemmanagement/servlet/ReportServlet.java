package isthatkirill.itemmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kirill Emelyanov
 */

@WebServlet(urlPatterns = "/report/*")
public class ReportServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ReportServlet.class.getName());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Get request was received with parameter action = {0}", action);
        if (action == null || action.equals("stock")) {
            request.getRequestDispatcher("/jsp/report/reportStock.jsp").forward(request, response);
        } else {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
