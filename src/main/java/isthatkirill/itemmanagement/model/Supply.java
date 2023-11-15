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
public class Supply {

    Long id;
    String company;
    LocalDateTime receivedAt;
    Long amount;
    Double price;
    Long itemId;

}
