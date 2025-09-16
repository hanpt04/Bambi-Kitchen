package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.IngredientCategory;
import gr1.fpt.bambikitchen.repository.IngredientCategoryRepository;
import gr1.fpt.bambikitchen.service.IngredientCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IngredientCategoryServiceImpl implements IngredientCategoryService {

    @Autowired
    private IngredientCategoryRepository repo;

    @Override
    public List<IngredientCategory> findAll() {
        return repo.findAll();
    }

    @Override
    public IngredientCategory findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Category not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public IngredientCategory save(IngredientCategory ingredientCategory) {
        if (repo.existsByName(ingredientCategory.getName())) {
            throw new CustomException("Category already exists!", HttpStatus.CONFLICT);
        }
        return repo.save(ingredientCategory);
    }

    @Override
    public IngredientCategory update(IngredientCategory ingredientCategory) {
        if (!repo.existsById(ingredientCategory.getId())) {
            throw new CustomException("Category not found!", HttpStatus.NOT_FOUND);
        }
        return repo.save(ingredientCategory);
    }

    @Override
    public void delete(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException("Category not found!", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
    }
}
