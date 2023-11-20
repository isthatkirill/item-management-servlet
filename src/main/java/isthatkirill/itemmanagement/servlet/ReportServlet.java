package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ReportService;
import isthatkirill.itemmanagement.service.impl.ReportServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kirill Emelyanov
 */

@WebServlet(urlPatterns = "/report/*")
public class ReportServlet extends HttpServlet {

    private ReportService reportService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        reportService = new ReportServiceImpl(new ItemRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/report/reportStock.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = reportService.process(request);
        request.setAttribute("path", path);
        request.getRequestDispatcher("/jsp/report/reportStock.jsp").forward(request, response);
    }
}
