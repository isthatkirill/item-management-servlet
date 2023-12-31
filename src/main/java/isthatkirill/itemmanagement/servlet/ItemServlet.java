package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.category.Category;
import isthatkirill.itemmanagement.model.enums.SortOrder;
import isthatkirill.itemmanagement.model.enums.SortType;
import isthatkirill.itemmanagement.model.item.Item;
import isthatkirill.itemmanagement.model.item.ItemExtended;
import isthatkirill.itemmanagement.service.CategoryService;
import isthatkirill.itemmanagement.service.ItemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
@WebServlet(urlPatterns = "/item/*")
public class ItemServlet extends HttpServlet {

    @Inject
    private ItemService itemService;

    @Inject
    private CategoryService categoryService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) {
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            if (!SortType.contains(sortBy) || !SortOrder.contains(sortOrder)) {
                sortBy = null;
                sortOrder = null;
            }
            List<ItemExtended> items = itemService.getAllExtended(sortBy, sortOrder);
            request.setAttribute("items", items);
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
            return;
        } else if (action.startsWith("button-delete-")) {
            itemService.deleteById(Long.valueOf(action.substring(14)));
        }
        List<ItemExtended> items = itemService.getAllExtended(null, null);
        List<Category> categories = categoryService.getAll();
        request.setAttribute("items", items);
        request.setAttribute("categories", categories);

        forwardRequest(action, request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<ItemExtended> items = itemService.getAllExtended(null, null);
            request.setAttribute("items", items);
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "create" -> {
                    Long generatedId = itemService.create(request);
                    request.setAttribute("generatedId", generatedId);
                }
                case "read" -> {
                    Item item = itemService.getById(request);
                    request.setAttribute("item", item);
                }
                case "update" -> {
                    itemService.update(request);
                    request.setAttribute("isSuccess", true);
                }
            }
        } catch (EntityNotFoundException e) {
            request.setAttribute("error", e.getMessage());
        } finally {
            if (action.equals("update") || action.equals("create")) {
                List<Category> categories = categoryService.getAll();
                request.setAttribute("categories", categories);
            }
            List<ItemExtended> items = itemService.getAllExtended(null, null);
            request.setAttribute("items", items);
            forwardRequest(action, request, response);
        }
    }

    private void forwardRequest(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jspPath = getJspPath(action);
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    private String getJspPath(String action) {
        if (action.startsWith("button-delete-")) {
            return "/jsp/item/uItem.jsp";
        }
        switch (action) {
            case "create" -> {
                return "/jsp/item/cItem.jsp";
            }
            case "read" -> {
                return "/jsp/item/rItem.jsp";
            }
            case "update" -> {
                return "/jsp/item/uItem.jsp";
            }
            default -> {
                return "/jsp/init/error.jsp";
            }
        }
    }
}