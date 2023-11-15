package isthatkirill.itemmanagement.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author Kirill Emelyanov
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    Long id;
    String name;
    String description;
    Double averagePurchasePrice;
    Double averageSalePrice;
    Integer stockUnits;
    Long categoryId;
    String brand;
    LocalDateTime createdAt;

}
