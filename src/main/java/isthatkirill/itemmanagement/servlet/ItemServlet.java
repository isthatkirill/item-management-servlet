package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.repository.ItemRepository;
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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        itemService = new ItemServiceImpl(new ItemRepository(), new CategoryRepository());
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
            case "create" -> request.getRequestDispatcher("/cItem.jsp").forward(request, response);
            case "read" -> request.getRequestDispatcher("/rItem.jsp").forward(request, response);
            case "update" -> request.getRequestDispatcher("/uItem.jsp").forward(request, response);
            case "delete" -> request.getRequestDispatcher("/dItem.jsp").forward(request, response);
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
            case "create" -> {
                try {
                    Long generatedId = itemService.create(request);
                    request.setAttribute("generatedId", generatedId);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/cItem.jsp").forward(request, response);
                }
            }
            case "read" -> {
                try {
                    Item item = itemService.getById(request);
                    request.setAttribute("item", item);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/rItem.jsp").forward(request, response);
                }
            }
            case "update" -> {
                try {
                    itemService.update(request);
                    request.setAttribute("isSuccess", true);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/uItem.jsp").forward(request, response);
                }
            }
            case "delete" -> {
                try {
                    itemService.delete(request);
                    request.setAttribute("isSuccess", true);
                } catch (EntityNotFoundException e) {
                    request.setAttribute("error", e.getMessage());
                } finally {
                    request.getRequestDispatcher("/dItem.jsp").forward(request, response);
                }
            }
        }
    }


}