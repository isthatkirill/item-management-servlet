package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SupplyRepository;
import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.SupplyService;
import isthatkirill.itemmanagement.service.impl.ItemServiceImpl;
import isthatkirill.itemmanagement.service.impl.SupplyServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kirill Emelyanov
 */

@WebServlet(urlPatterns = "/supply/*")
public class SupplyServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(SupplyServlet.class.getName());
    private SupplyService supplyService;
    private ItemService itemService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        supplyService = new SupplyServiceImpl(new SupplyRepository(), new ItemRepository());
        itemService = new ItemServiceImpl(new ItemRepository(), new CategoryRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/jsp/supply/cSupply.jsp").forward(request, response);
            return;
        } else if (action.equals("create")) {
            List<ItemShort> items = itemService.getAllShort();
            request.setAttribute("items", items);
        } else if (action.equals("update")) {
            List<SupplyExtended> supplies = supplyService.getAllExtended();
            request.setAttribute("supplies", supplies);
        }


        forwardRequest(action, request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/jsp/supply/cSupply.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "create" -> {
                    Long generatedId = supplyService.create(request);
                    List<ItemShort> items = itemService.getAllShort();
                    request.setAttribute("items", items);
                    request.setAttribute("generatedId", generatedId);
                }
                case "update" -> {
                    supplyService.update(request);
                    List<SupplyExtended> supplies = supplyService.getAllExtended();
                    request.setAttribute("supplies", supplies);
                    request.setAttribute("isSuccess", true);
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
                return "/jsp/supply/cSupply.jsp";
            }
            case "update" -> {
                return "/jsp/supply/uSupply.jsp";
            }
            default -> {
                return "/jsp/error/error.jsp";
            }
        }
    }
}
