package isthatkirill.itemmanagement.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * @author Kirill Emelyanov
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sale {

    Long id;
    Long amount;
    Double price;
    LocalDateTime createdAt;
    Long itemId;
    
}
