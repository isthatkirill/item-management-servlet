package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SupplyRepository;
import isthatkirill.itemmanagement.service.SupplyService;
import isthatkirill.itemmanagement.service.impl.SupplyServiceImpl;
import jakarta.servlet.ServletConfig;
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

@WebServlet(urlPatterns = "/supply/*")
public class SupplyServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(SupplyServlet.class.getName());
    private SupplyService supplyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        supplyService = new SupplyServiceImpl(new SupplyRepository(), new ItemRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/cSupply.jsp").forward(request, response);
            return;
        }

        forwardRequest(action, request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/cSupply.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "create" -> {
                    Long generatedId = supplyService.create(request);
                    request.setAttribute("generatedId", generatedId);
                    request.getRequestDispatcher("/cSupply.jsp").forward(request, response);
                }
            }
        } catch (EntityNotFoundException e) {
            request.setAttribute("error", e.getMessage());
        } finally {
            forwardRequest(action, request, response);
        }
    }

    private void forwardRequest(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jspPath = getJspPath(action);
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    private String getJspPath(String action) {
        switch (action) {
            case "create" -> {
                return "/cSupply.jsp";
            }
            case "update" -> {
                return "/uSupply.jsp";
            }
            default -> {
                return "/error.jsp";
            }
        }
    }
}
