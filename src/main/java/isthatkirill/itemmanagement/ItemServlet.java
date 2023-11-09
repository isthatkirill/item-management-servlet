package isthatkirill.itemmanagement;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import isthatkirill.itemmanagement.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(urlPatterns = "/item/*")
public class ItemServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ItemServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        logger.info(request.getRequestURI());

        request.getRequestDispatcher("/main.jsp").forward(request, response);
    }

    public void destroy() {
    }
}