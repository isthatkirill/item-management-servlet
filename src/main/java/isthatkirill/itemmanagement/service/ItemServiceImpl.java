package isthatkirill.itemmanagement.service;

import isthatkirill.itemmanagement.mapper.ItemMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * @author Kirill Emelyanov
 */


public class ItemServiceImpl implements ItemService {

    @Override
    public void createItem(HttpServletRequest request) {
        System.out.println(ItemMapper.getItemObject(request));
    }

}
