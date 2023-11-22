package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.repository.ItemRepository;
import isthatkirill.itemmanagement.service.ReportService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

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

@ApplicationScoped
public class ReportServiceImpl implements ReportService {

    @Inject
    private ItemRepository itemRepository;

    private final List<String> possibleStockItemFields = new ArrayList<>() {{
        add("name");
        add("description");
        add("brand");
        add("stock_units");
        add("purchase_price");
        add("stock_price");
        add("category_name");
        add("supplies_count");
        add("last_supply_date");
    }};

    private final List<String> possibleStockCategoryFields = new ArrayList<>() {{
        add("name");
        add("description");
        add("items_in_category");
        add("stock_units");
        add("stock_price");
        add("supplies_count");
        add("last_supply_date");
        add("most_units_item");
        add("less_units_item");
        add("most_cheap_item");
        add("most_expensive_item");
    }};

    private final List<String> possibleProfitItemFields = new ArrayList<>() {{
        add("name");
        add("description");
        add("brand");
        add("supply_price");
        add("sale_price");
        add("profit");
        add("profit_percentage");
        add("sold");
        add("last_sale_date");
        add("sales_count");
        add("most_big_sale_ttl_price");
    }};

    private final List<String> possibleProfitCategoryFields = new ArrayList<>() {{
        add("name");
        add("description");
        add("supply_price");
        add("sale_price");
        add("profit");
        add("profit_percentage");
        add("sold");
        add("last_sale_date");
        add("sales_count");
    }};

    @Override
    public byte[] process(HttpServletRequest request) {
        List<String> selectedFields = request.getParameterMap()
                .keySet().stream().skip(1).collect(Collectors.toList());
        String reportType = request.getParameter("reportType");
        if (reportType == null) {
            return processEmptyFile("Wrong report type");
        }

        switch (reportType) {
            case "itemStockReport" -> {
                if (new HashSet<>(possibleStockItemFields).containsAll(selectedFields)) {
                    return processItemStockReport(selectedFields);
                }
            }
            case "categoryStockReport" -> {
                if (new HashSet<>(possibleStockCategoryFields).containsAll(selectedFields)) {
                    return processCategoryStockReport(selectedFields);
                }
            }
            case "itemSaleReport" -> {
                if (new HashSet<>(possibleProfitItemFields).containsAll(selectedFields)) {
                    return processItemSaleReport(selectedFields);
                }
            }
            case "categorySaleReport" -> {
                if (new HashSet<>(possibleProfitCategoryFields).containsAll(selectedFields)) {
                    return processCategorySaleReport(selectedFields);
                }
            }
            default -> {
                return processEmptyFile("Wrong report fields");
            }
        }
        return processEmptyFile("Wrong report fields");
    }

    private byte[] processEmptyFile(String message) {
        return write(new ArrayList<>(), List.<String[]>of(message.split(" ")));
    }

    private byte[] processCategoryStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getCategoryStockReport(selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] processItemStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getItemStockReport(selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] processCategorySaleReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getCategorySaleReport(selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] processItemSaleReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getItemSaleReport(selectedFields);
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
