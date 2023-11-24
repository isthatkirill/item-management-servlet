package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.model.enums.ReportType;
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

import static isthatkirill.itemmanagement.util.Constants.*;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class ReportServiceImpl implements ReportService {

    private final ItemRepository itemRepository;

    @Inject
    public ReportServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

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
                if (new HashSet<>(POSSIBLE_STOCK_ITEM_FIELDS).containsAll(selectedFields)) {
                    return processItemStockReport(selectedFields);
                }
            }
            case "categoryStockReport" -> {
                if (new HashSet<>(POSSIBLE_STOCK_CATEGORY_FIELDS).containsAll(selectedFields)) {
                    return processCategoryStockReport(selectedFields);
                }
            }
            case "itemSaleReport" -> {
                if (new HashSet<>(POSSIBLE_PROFIT_ITEM_FIELDS).containsAll(selectedFields)) {
                    return processItemSaleReport(selectedFields);
                }
            }
            case "categorySaleReport" -> {
                if (new HashSet<>(POSSIBLE_PROFIT_CATEGORY_FIELDS).containsAll(selectedFields)) {
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
        List<String[]> rows = itemRepository.getReport(ReportType.CATEGORY_STOCK, selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] processItemStockReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getReport(ReportType.ITEM_STOCK, selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] processCategorySaleReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getReport(ReportType.CATEGORY_SALE, selectedFields);
        return write(selectedFields, rows);
    }

    private byte[] processItemSaleReport(List<String> selectedFields) {
        List<String[]> rows = itemRepository.getReport(ReportType.ITEM_SALE, selectedFields);
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
