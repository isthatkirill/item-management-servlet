package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.CategoryService;
import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.impl.CategoryServiceImpl;
import isthatkirill.itemmanagement.service.impl.ItemServiceImpl;
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

@WebServlet(urlPatterns = "/item/*")
public class ItemServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ItemServlet.class.getName());
    private ItemService itemService;
    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        itemService = new ItemServiceImpl(new ItemRepository(), new CategoryRepository());
        categoryService = new CategoryServiceImpl(new CategoryRepository());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Get request was received with parameter action = {0}", action);
        if (action == null) {
            List<Item> items = itemService.getAll();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
            return;
        } else if (action.startsWith("button-delete-")) {
            itemService.deleteButton(Long.valueOf(action.substring(14)));
            List<Item> items = itemService.getAll();
            request.setAttribute("items", items);
        } else if (action.equals("update")) {
            List<Item> items = itemService.getAll();
            List<Category> categories = categoryService.getAll();
            request.setAttribute("items", items);
            request.setAttribute("categories", categories);
        } else if (action.equals("create")) {
            List<Category> categories = categoryService.getAll();
            request.setAttribute("categories", categories);
        }

        forwardRequest(action, request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            List<Item> items = itemService.getAll();
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
                List<Item> items = itemService.getAll();
                request.setAttribute("items", items);
                request.setAttribute("categories", categories);
            }
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
                return "/jsp/error/error.jsp";
            }
        }
    }


}