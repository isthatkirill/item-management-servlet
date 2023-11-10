package isthatkirill.itemmanagement.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import isthatkirill.itemmanagement.model.Item;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

public class ItemMapper {

    @SneakyThrows
    public static Item getItemObject(HttpServletRequest request) {
        String rowContent = getRowContent(request);
        ObjectMapper objectMapper = new ObjectMapper();
        Item item = objectMapper.readValue(rowContent, Item.class);
        item.setCreatedAt(LocalDateTime.now());
        return item;
    }

    @SneakyThrows
    private static String getRowContent(HttpServletRequest request) {
        return request.getReader()
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

}
