package isthatkirill.itemmanagement.model.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * @author Kirill Emelyanov
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemExtended extends Item {

    String categoryName;

}
