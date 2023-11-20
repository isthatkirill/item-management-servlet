package isthatkirill.itemmanagement.mapper;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public class ReportDataMapper {

    private ReportDataMapper() {

    }

    @SneakyThrows
    public static List<String[]> extractRows(List<String> selectedFields, ResultSet resultSet) {
        List<String[]> rows = new ArrayList<>();
        while (resultSet.next()) {
            String[] row = extractRow(selectedFields, resultSet);
            rows.add(row);
        }
        return rows;
    }


    @SneakyThrows
    private static String[] extractRow(List<String> selectedFields, ResultSet resultSet) {
        String[] newRow = new String[selectedFields.size() + 1];
        newRow[0] = resultSet.getString("id");
        for (int i = 0; i < selectedFields.size(); i++) {
            String tmp = resultSet.getString(selectedFields.get(i));
            newRow[i + 1] = (tmp == null) ? "-" : tmp;
        }
        return newRow;
    }

}
