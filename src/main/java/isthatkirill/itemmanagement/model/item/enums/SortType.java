package isthatkirill.itemmanagement.model.item.enums;

/**
 * @author Kirill Emelyanov
 */

public enum SortType {

    SORT_BY_ID("id"), SORT_BY_NAME("name"), SORT_BY_DESCRIPTION("description"),
    SORT_BY_PURCHASE_PRICE("purchase_price"), SORT_BY_SALE_PRICE("sale_price"),
    SORT_BY_STOCK_UNITS("stock_units"), SORT_BY_CATEGORY_ID("category_id"),
    SORT_BY_BRAND("brand");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String valueToCheck) {
        for (SortType sortType : SortType.values()) {
            if (sortType.getValue().equals(valueToCheck)) {
                return true;
            }
        }
        return false;
    }

}
