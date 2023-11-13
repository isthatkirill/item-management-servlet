package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.service.CategoryService;
import isthatkirill.itemmanagement.service.impl.CategoryServiceImpl;
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
            request.getRequestDispatcher("/jsp/category/cCategory.jsp").forward(request, response);
            return;
        } else if (action.equals("update")) {
            List<Category> categories = categoryService.getAll();
            request.setAttribute("categories", categories);
        } else if (action.startsWith("button-delete-")) {
            categoryService.deleteButton(Long.valueOf(action.substring(14)));
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
            request.getRequestDispatcher("/jsp/category/cCategory.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "create" -> {
                    Long generatedId = categoryService.create(request);
                    request.setAttribute("generatedId", generatedId);
                }
                case "read" -> {
                    Category category = categoryService.getById(request);
                    request.setAttribute("category", category);
                }
                case "update" -> {
                    categoryService.update(request);
                    request.setAttribute("isSuccess", true);
                }
            }
        } catch (EntityNotFoundException e) {
            request.setAttribute("error", e.getMessage());
        } finally {
            if (action.equals("update")) {
                List<Category> categories = categoryService.getAll();
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
            return "/jsp/category/uCategory.jsp";
        }
        switch (action) {
            case "create" -> {
                return "/jsp/category/cCategory.jsp";
            }
            case "read" -> {
                return "/jsp/category/rCategory.jsp";
            }
            case "update" -> {
                return "/jsp/category/uCategory.jsp";
            }
            default -> {
                return "/jsp/error/error.jsp";
            }
        }
    }

}
