package isthatkirill.itemmanagement.servlet;

import isthatkirill.itemmanagement.service.ReportService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import static isthatkirill.itemmanagement.util.Constants.FORMATTER_FILE;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
@WebServlet(urlPatterns = "/report/*")
public class ReportServlet extends HttpServlet {

    @Inject
    private ReportService reportService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/report/report.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] data = reportService.process(request);
        response.setContentType("text/plain");
        response.setContentLength(data.length);
        response.setHeader("Content-disposition", "attachment; filename=" + LocalDateTime.now()
                .format(FORMATTER_FILE) + ".csv");
        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
