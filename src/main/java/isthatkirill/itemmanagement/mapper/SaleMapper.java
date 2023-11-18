package isthatkirill.itemmanagement.mapper;

import isthatkirill.itemmanagement.model.sale.Sale;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public class SaleMapper {

    private SaleMapper() {

    }

    public static Sale extractSaleFromRequest(HttpServletRequest request) {
        Sale sale = new Sale();
        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) sale.setId(Long.valueOf(id));

        String amount = request.getParameter("amount");
        if (amount != null && !amount.isBlank()) sale.setAmount(Long.valueOf(amount));

        String price = request.getParameter("price");
        if (price != null && !price.isBlank()) sale.setPrice(Double.valueOf(price));

        String itemId = request.getParameter("itemId");
        if (itemId != null && !itemId.isBlank()) sale.setItemId(Long.valueOf(itemId));

        return sale;
    }

}
