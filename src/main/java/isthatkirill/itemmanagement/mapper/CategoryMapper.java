package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.category.Category;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Kirill Emelyanov
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category extractCategoryFromRequest(HttpServletRequest request) {
        Category category = new Category();
        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) category.setId(Long.valueOf(id));

        String name = request.getParameter("name");
        if (name != null && !name.isBlank()) category.setName(decode(name));

        String description = request.getParameter("description");
        if (description != null && !description.isBlank()) category.setDescription(decode(description));

        return category;
    }

    @SneakyThrows
    public static Optional<Category> extractCategoryFromResultSet(ResultSet resultSet) {
        if (!resultSet.next()) return Optional.empty();
        return Optional.of(mapResultSetToCategory(resultSet));
    }

    @SneakyThrows
    private static Category mapResultSetToCategory(ResultSet resultSet) {
        Category category = new Category();
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setDescription(resultSet.getString("description"));
        return category;
    }

    @SneakyThrows
    public static List<Category> extractCategoriesFromResultSet(ResultSet resultSet) {
        List<Category> categories = new ArrayList<>();
        while (resultSet.next()) {
            categories.add(mapResultSetToCategory(resultSet));
        }
        return categories;
    }

    private static String decode(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }
}
