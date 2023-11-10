package isthatkirill.itemmanagement.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Kirill Emelyanov
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    Long id;
    String name;
    String description;

}
