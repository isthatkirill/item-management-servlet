package isthatkirill.itemmanagement.model.sale;

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
public class Sale {

    Long id;
    Long amount;
    Double price;
    LocalDateTime createdAt;
    Long itemId;
    
}