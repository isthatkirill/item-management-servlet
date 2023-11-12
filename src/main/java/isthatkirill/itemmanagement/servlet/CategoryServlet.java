package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.CategoryService;
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

/**
 * @author Kirill Emelyanov
 */

@WebServlet(urlPatterns = "/category/*")
public class CategoryServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(CategoryServlet.class.getName());
    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        categoryService = new CategoryServiceImpl(new CategoryRepository());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/cCategory.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "create" -> request.getRequestDispatcher("/cCategory.jsp").forward(request, response);
            case "read" -> request.getRequestDispatcher("/rCategory.jsp").forward(request, response);
            case "update" -> request.getRequestDispatcher("/uCategory.jsp").forward(request, response);
            case "delete" -> request.getRequestDispatcher("/dCategory.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        logger.log(Level.INFO, "Post request was received with parameter action = {0}", action);
        if (action == null) {
            request.getRequestDispatcher("/cCategory.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "create" -> {
                Long generatedId = categoryService.create(request);
                request.setAttribute("generatedId", generatedId);
                request.getRequestDispatcher("/cCategory.jsp").forward(request, response);
            }
            case "read" -> {
                try {
                    Category category = categoryService.getById(request);
                    request.setAttribute("category", category);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/rCategory.jsp").forward(request, response);
                }
            }
            case "update" -> {
                try {
                    categoryService.update(request);
                    request.setAttribute("isSuccess", true);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/uCategory.jsp").forward(request, response);
                }
            }
            case "delete" -> {
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
