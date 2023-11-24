package isthatkirill.itemmanagement.model.enums;

import static isthatkirill.itemmanagement.util.Constants.*;

/**
 * @author Kirill Emelyanov
 */

public enum ReportType {

    ITEM_STOCK(GET_ITEM_STOCK_REPORT),
    CATEGORY_STOCK(GET_CATEGORY_STOCK_REPORT),
    ITEM_SALE(GET_ITEM_SALE_REPORT),
    CATEGORY_SALE(GET_CATEGORY_SALE_REPORT);

    private final String value;

    ReportType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
