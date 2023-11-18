package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.exception.NotEnoughItemException;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.repository.SaleRepository;
import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.SaleService;
import isthatkirill.itemmanagement.service.impl.ItemServiceImpl;
import isthatkirill.itemmanagement.service.impl.SaleServiceImpl;
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

@WebServlet(urlPatterns = "/sale/*")
public class SaleServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(SaleServlet.class.getName());
    private SaleService saleService;
    private ItemService itemService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        saleService = new SaleServiceImpl(new SaleRepository(), new ItemRepository());
        itemService = new ItemServiceImpl(new ItemRepository(), new CategoryRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            List<ItemShort> items = itemService.getAllShort();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/jsp/sale/cSale.jsp").forward(request, response);
            return;
        } else if (action.equals("create")) {
            List<ItemShort> items = itemService.getAllShort();
            request.setAttribute("items", items);
        } else if (action.equals("update")) {

        }


        forwardRequest(action, request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/jsp/sale/cSale.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "create" -> {
                    Long generatedId = saleService.create(request);;
                    request.setAttribute("generatedId", generatedId);
                }
                case "update" -> {
                    /*supplyService.update(request);
                    List<SupplyExtended> supplies = supplyService.getAllExtended();
                    request.setAttribute("supplies", supplies);
                    request.setAttribute("isSuccess", true);*/
                }
            }
        } catch (EntityNotFoundException | NotEnoughItemException e) {
            request.setAttribute("error", e.getMessage());
        } finally {
            List<ItemShort> items = itemService.getAllShort();
            request.setAttribute("items", items);
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
                return "/jsp/sale/cSale.jsp";
            }
            case "update" -> {
                return "/jsp/sale/uSale.jsp";
            }
            default -> {
                return "/jsp/error/error.jsp";
            }
        }
    }
}
