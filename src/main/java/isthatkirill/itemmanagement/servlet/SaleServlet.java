package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.exception.NotEnoughItemException;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.model.sale.SaleExtended;
import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.SaleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
@WebServlet(urlPatterns = "/sale/*")
public class SaleServlet extends HttpServlet {

    @Inject
    private SaleService saleService;

    @Inject
    private ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<ItemShort> items = itemService.getAllShort();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/jsp/sale/cSale.jsp").forward(request, response);
            return;
        } else if (action.equals("create")) {
            List<ItemShort> items = itemService.getAllShort();
            request.setAttribute("items", items);
        } else if (action.equals("update")) {
            List<SaleExtended> sales = saleService.getAllExtended();
            request.setAttribute("sales", sales);
        }

        forwardRequest(action, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            request.getRequestDispatcher("/jsp/sale/cSale.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "create" -> {
                    Long generatedId = saleService.create(request);
                    request.setAttribute("generatedId", generatedId);
                }
                case "update" -> {
                    saleService.update(request);
                    request.setAttribute("isSuccess", true);
                }
            }
        } catch (EntityNotFoundException | NotEnoughItemException e) {
            request.setAttribute("error", e.getMessage());
        } finally {
            if (action.equals("update")) {
                List<SaleExtended> sales = saleService.getAllExtended();
                request.setAttribute("sales", sales);
            } else if (action.equals("create")) {
                List<ItemShort> items = itemService.getAllShort();
                request.setAttribute("items", items);
            }
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
