package isthatkirill.itemmanagement.service.impl;

import isthatkirill.itemmanagement.exception.EntityNotFoundException;
import isthatkirill.itemmanagement.mapper.CategoryMapper;
import isthatkirill.itemmanagement.model.Category;
import isthatkirill.itemmanagement.repository.CategoryRepository;
import isthatkirill.itemmanagement.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

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
        return checkIfCategoryExists(Long.valueOf(request.getParameter("id")));
    }

    @Override
    public void update(HttpServletRequest request) {
        Category category = CategoryMapper.extractCategoryFromRequest(request);
        checkIfCategoryExists(category.getId());
        categoryRepository.update(category);
    }

    @Override
    public void deleteById(HttpServletRequest request) {
        Long id = Long.valueOf(request.getParameter("id"));
        checkIfCategoryExists(id);
        categoryRepository.delete(id);
    }

    @Override
    public void deleteButton(Long id) {
        categoryRepository.delete(id);
    }


    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    private Category checkIfCategoryExists(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Категория с id = %s " +
                        "не найдена. Проверьте правильность вводимых данных.", id)));
    }

}
