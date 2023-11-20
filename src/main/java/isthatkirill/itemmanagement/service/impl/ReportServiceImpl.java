package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

public class ReportServiceImpl implements ReportService {

    private final ItemRepository itemRepository;

    private final List<String> possibleFields = new ArrayList<>() {{
        add("name");
        add("description");
        add("brand");
        add("stock_units");
        add("purchase_price");
        add("stock_purchase_price");
        add("category_name");
        add("last_supply_date");
        add("common_stock");
        add("common_price");
        add("amountPerCategory");
        add("stockPerCategory");
    }};

    public ReportServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public String process(HttpServletRequest request) {
        List<String> selectedFields = request.getParameterMap()
                .keySet().stream().skip(1).collect(Collectors.toList());
        String reportType = request.getParameter("reportType");
        if (reportType == null) {
            return processEmptyFile();
        }

        System.out.println(selectedFields);
        String path = "";
        switch (reportType) {
            case "itemStockReport" -> path = processItemStockReport(selectedFields);
            case "categoryStockReport" ->  path = processCategoryStockReport(selectedFields);
            default ->  path = processEmptyFile();
        }
        return path;
    }

    private String processEmptyFile() {
        return null;
    }

    private String processCategoryStockReport(List<String> selectedFields) {
        return null;
    }

    @SneakyThrows
    private String processItemStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getItemStockReport(selectedFields);
        String csvFilePath = "C:/Users/user/Desktop/spring/apache-tomcat-10.1.7/bin/file.csv";
        try (FileWriter writer = new FileWriter(csvFilePath, false)) {
            for (String[] rawRow : rows) {
                String row = String.join(",", rawRow);
                writer.write(row);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFilePath;
    }




}
