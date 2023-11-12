package isthatkirill.itemmanagement.exception;

/**
 * @author Kirill Emelyanov
 */

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityClass, Long entityId) {
        super(String.format("Entity %s not found. Id = %s", entityClass.getName(), entityId));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
