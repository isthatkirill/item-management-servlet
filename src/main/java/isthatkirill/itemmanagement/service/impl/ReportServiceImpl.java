package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
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
    public byte[] process(HttpServletRequest request) {
        List<String> selectedFields = request.getParameterMap()
                .keySet().stream().skip(1).collect(Collectors.toList());
        String reportType = request.getParameter("reportType");
        if (reportType == null || new HashSet<>(selectedFields).containsAll(possibleFields)) {
            return processEmptyFile();
        }

        System.out.println(selectedFields);
        switch (reportType) {
            case "itemStockReport" -> {
                return processItemStockReport(selectedFields);
            }
            case "categoryStockReport" -> {
                return processCategoryStockReport(selectedFields);
            }
            default -> {
                return processEmptyFile();
            }
        }
    }

    private byte[] processEmptyFile() {
        return write(new ArrayList<>(), List.<String[]>of(new String[]{"No data"}));
    }

    private byte[] processCategoryStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getCategoryStockReport(selectedFields);
        return write(selectedFields, rows);
    }

    @SneakyThrows
    private byte[] processItemStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getItemStockReport(selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] write(List<String> selectedFields, List<String[]> rows) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            selectedFields.add(0, "id");
            String header = String.join(",", selectedFields.toArray(new String[0]));
            osw.write(header);
            osw.write("\n");
            for (String[] rawRow : rows) {
                String row = String.join(",", rawRow);
                osw.write(row);
                osw.write("\n");
            }
            osw.flush();
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


}
