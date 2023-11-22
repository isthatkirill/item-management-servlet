package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static isthatkirill.itemmanagement.util.Constants.FORMATTER_FILE;

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
        add("items_in_category");
        add("purchase_price");
        add("less_units_item");
        add("most_cheap_item");
        add("stock_price");
        add("category_name");
        add("supplies_count");
        add("last_supply_date");
        add("most_units_item");
        add("most_expensive_item");
    }};

    public ReportServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public String process(HttpServletRequest request) {
        List<String> selectedFields = request.getParameterMap()
                .keySet().stream().skip(1).collect(Collectors.toList());
        String reportType = request.getParameter("reportType");
        if (reportType == null || new HashSet<>(selectedFields).containsAll(possibleFields)) {
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
        return write(new ArrayList<>(), List.<String[]>of(new String[]{"No data"}));
    }

    private String processCategoryStockReport(List<String> selectedFields) {

        List<String[]> rows = itemRepository.getCategoryStockReport(selectedFields);
        return write(selectedFields, rows);
    }

    @SneakyThrows
    private String processItemStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getItemStockReport(selectedFields);
        return write(selectedFields, rows);
    }

    private String write(List<String> selectedFields, List<String[]> rows) {
        String csvFilePath = LocalDateTime.now().format(FORMATTER_FILE) + ".csv";
        try (FileWriter writer = new FileWriter(csvFilePath, false)) {
            selectedFields.add(0, "id");
            String header = String.join(",", selectedFields.toArray(new String[0]));
            writer.write(header);
            writer.write("\n");
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
