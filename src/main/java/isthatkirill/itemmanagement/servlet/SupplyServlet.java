package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.item.ItemShort;
import isthatkirill.itemmanagement.model.supply.SupplyExtended;
import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.SupplyService;
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
@WebServlet(urlPatterns = "/supply/*")
public class SupplyServlet extends HttpServlet {

    @Inject
    private SupplyService supplyService;

    @Inject
    private ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
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
