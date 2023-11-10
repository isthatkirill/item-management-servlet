package isthatkirill.itemmanagement;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import isthatkirill.itemmanagement.service.ItemService;
import isthatkirill.itemmanagement.service.ItemServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(urlPatterns = "/item/*")
public class ItemServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ItemServlet.class.getName());
    private ItemService itemService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        itemService = new ItemServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        logger.log(Level.INFO, "Get request was received at {0}", uri);

        switch (uri) {
            case "/item/create-item" -> request.getRequestDispatcher("/citem.jsp").forward(request, response);
            default -> request.getRequestDispatcher("/main.jsp").forward(request, response);
        }

        logger.info(request.getRequestURI());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        logger.log(Level.INFO, "Post request was received at {0}", uri);

        switch (uri) {
            case "/item/create-item" -> {
                itemService.createItem(request);


            }
        }

    }


}