package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.mapper.CategoryMapper;
import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Kirill Emelyanov
 */

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long createCategory(HttpServletRequest request) {
        Category category = CategoryMapper.extractCategoryFromRequest(request);
        return categoryRepository.create(category);
    }

}
