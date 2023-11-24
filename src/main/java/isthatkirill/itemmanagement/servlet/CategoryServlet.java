package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.category.Category;
import isthatkirill.itemmanagement.service.CategoryService;
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
@WebServlet(urlPatterns = "/category/*")
public class CategoryServlet extends HttpServlet {

    @Inject
    private CategoryService categoryService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) {
            request.getRequestDispatcher("/jsp/category/cCategory.jsp").forward(request, response);
            return;
        } else if (action.startsWith("button-delete-")) {
            categoryService.deleteById(Long.valueOf(action.substring(14)));

        }
        List<Category> categories = categoryService.getAll();
        request.setAttribute("categories", categories);

        forwardRequest(action, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
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
            if (action.equals("update") || action.equals("read")) {
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
                return "/jsp/init/error.jsp";
            }
        }
    }

}
