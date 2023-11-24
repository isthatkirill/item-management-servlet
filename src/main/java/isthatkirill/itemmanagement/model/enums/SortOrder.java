package isthatkirill.itemmanagement.model.enums;

/**
 * @author Kirill Emelyanov
 */

public enum SortOrder {

    DESC("desc"), ASC("asc");

    private final String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String valueToCheck) {
        for (SortOrder sortOrder : SortOrder.values()) {
            if (sortOrder.getValue().equals(valueToCheck)) {
                return true;
            }
        }
        return false;
    }

}
