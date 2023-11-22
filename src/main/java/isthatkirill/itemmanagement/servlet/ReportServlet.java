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

import java.io.*;

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
        request.getRequestDispatcher("/jsp/report/report.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = reportService.process(request);
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=" + path);
        File file = new File(path);
        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[2048];
            int numBytesRead;
            while ((numBytesRead = is.read(buffer)) > 0) {
                os.write(buffer, 0, numBytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
