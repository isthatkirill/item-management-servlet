package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.CategoryMapper;
import isthatkirill.itemmanagement.model.category.Category;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.service.CategoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static isthatkirill.itemmanagement.util.Constants.CHECK_DATA;

/**
 * @author Kirill Emelyanov
 */

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long create(HttpServletRequest request) {
        Category category = CategoryMapper.extractCategoryFromRequest(request);
        return categoryRepository.create(category);
    }

    @Override
    public Category getById(HttpServletRequest request) {
        return checkIfCategoryExistsAndGet(Long.valueOf(request.getParameter("id")));
    }

    @Override
    public void update(HttpServletRequest request) {
        Category category = CategoryMapper.extractCategoryFromRequest(request);
        checkIfCategoryExists(category.getId());
        categoryRepository.update(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.delete(id);
    }


    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    private Category checkIfCategoryExistsAndGet(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категория с id = %s " +
                        "не найдена. %s", CHECK_DATA, id)));
    }

    private void checkIfCategoryExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Категория с id = %s " +
                    "не найдена. %s", CHECK_DATA, id));
        }
    }

}
