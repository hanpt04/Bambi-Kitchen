package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.IngredientDetail;
import gr1.fpt.bambikitchen.repository.IngredientDetailRepository;
import gr1.fpt.bambikitchen.service.IngredientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class IngredientDetailServiceImpl implements IngredientDetailService {

    @Autowired
    private IngredientDetailRepository repo;

    @Override
    public List<IngredientDetail> findAll() {
        return repo.findAll();
    }

    @Override
    public IngredientDetail findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException("Ingredient detail not found!", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<IngredientDetail> findByIngredientId(int id) {
        return repo.findByIngredientId(id);
    }

    @Override
    public IngredientDetail save(IngredientDetail ingredientDetail) {
        return repo.save(ingredientDetail);
    }

    @Override
    public IngredientDetail update(IngredientDetail ingredientDetail) {
        if (repo.existsById(ingredientDetail.getId())) {
            return repo.save(ingredientDetail);
        } else {
            throw new CustomException("Not Found !", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException("Ingredient detail not found!", HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
    }

    @Override
    public void setActive(int id) {
        IngredientDetail ingredientDetail = repo.findById(id).get();
        if (ingredientDetail.isActive()) {
            ingredientDetail.setActive(false);
        } else {
            ingredientDetail.setActive(true);
        }
        repo.save(ingredientDetail);
    }
}
