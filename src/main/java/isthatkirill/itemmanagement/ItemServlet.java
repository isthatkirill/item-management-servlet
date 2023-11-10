package isthatkirill.itemmanagement;

import isthatkirill.itemmanagement.model.Item;
import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.ItemServiceImpl;
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
        itemService = new ItemServiceImpl(
                new ItemRepository()
        );
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
            case "create-item" -> request.getRequestDispatcher("/citem.jsp").forward(request, response);
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
                Long generatedId = itemService.createItem(request);
                request.setAttribute("generatedId", generatedId);
                request.getRequestDispatcher("/citem.jsp").forward(request, response);
            }
        }
    }


}