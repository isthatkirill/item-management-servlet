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
public class ItemManagementServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ItemManagementServlet.class.getName());
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
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            List<Item> items = itemService.getAll();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "create-item" -> request.getRequestDispatcher("/cItem.jsp").forward(request, response);
            case "read-item" -> request.getRequestDispatcher("/rItem.jsp").forward(request, response);
            case "create-category" -> request.getRequestDispatcher("/cCategory.jsp").forward(request, response);
            case "read-category" -> request.getRequestDispatcher("/rCategory.jsp").forward(request, response);
            case "update-category" -> request.getRequestDispatcher("/uCategory.jsp").forward(request, response);
            case "delete-category" -> request.getRequestDispatcher("/dCategory.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/main.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "create-item" -> {
                try {
                    Long generatedId = itemService.create(request);
                    request.setAttribute("generatedId", generatedId);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/cItem.jsp").forward(request, response);
                }
            }
            case "read-item" -> {
                try {
                    Item item = itemService.getById(request);
                    request.setAttribute("item", item);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/rItem.jsp").forward(request, response);
                }
            }
            case "create-category" -> {
                Long generatedId = categoryService.create(request);
                request.setAttribute("generatedId", generatedId);
                request.getRequestDispatcher("/cCategory.jsp").forward(request, response);
            }
            case "read-category" -> {
                try {
                    Category category = categoryService.getById(request);
                    request.setAttribute("category", category);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/rCategory.jsp").forward(request, response);
                }
            }
            case "update-category" -> {
                try {
                    categoryService.update(request);
                    request.setAttribute("isSuccess", true);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/uCategory.jsp").forward(request, response);
                }
            }
            case "delete-category" -> {
                try {
                    categoryService.delete(request);
                    request.setAttribute("isSuccess", true);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/dCategory.jsp").forward(request, response);
                }
            }
        }
    }


}