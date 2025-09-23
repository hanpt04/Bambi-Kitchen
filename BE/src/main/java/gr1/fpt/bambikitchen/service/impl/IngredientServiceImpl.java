package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.IngredientMapper;
import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.IngredientCategory;
import gr1.fpt.bambikitchen.model.dto.request.IngredientCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientDtoRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientUpdateRequest;
import gr1.fpt.bambikitchen.repository.IngredientCategoryRepository;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.service.IngredientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient findById(int id) {
        return ingredientRepository.findById(id).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + id, HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Ingredient findByName(String name) {
        return ingredientRepository.findByName(name).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + name, HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Ingredient save(IngredientCreateRequest ingredient) {

        IngredientCategory category = ingredientCategoryRepository.findById(ingredient.getCategoryId()).orElseThrow(
                () -> new CustomException("Ingredient category cannot be found " + ingredient.getCategoryId(), HttpStatus.BAD_REQUEST)
        );
        Ingredient newIngredient = ingredientMapper.toIngredient(ingredient);
        newIngredient.setCategory(category);

        Ingredient ingredientSave = ingredientRepository.save(newIngredient);
        //publisher
//         if(ingredient.getFile().getSize()>0) {
//            applicationEventPublisher.publishEvent(new IngredientDtoRequest(ingredientSave, event.getFile()));
//        }
        return ingredientSave;
    }

    @Override
    public Ingredient update(IngredientUpdateRequest ingredient) {
        Ingredient oldIngredient = ingredientRepository.findById(ingredient.getId()).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + ingredient.getId(), HttpStatus.BAD_REQUEST)
        );

        IngredientCategory category = ingredientCategoryRepository.findById(ingredient.getCategoryId()).orElseThrow(
                () -> new CustomException("Ingredient category cannot be found " + ingredient.getCategoryId(), HttpStatus.BAD_REQUEST)
        );

        Ingredient newIngredient = ingredientMapper.toIngredient(ingredient);
        newIngredient.setId(oldIngredient.getId());
        newIngredient.setCategory(category);

        return ingredientRepository.save(newIngredient);
    }

    @Override
    public String delete(int id) {
        Ingredient oldIngredient = ingredientRepository.findById(id).orElseThrow(
                () -> new CustomException("Ingredient cannot be found " + id, HttpStatus.BAD_REQUEST)
        );

        oldIngredient.setActive(false);
        ingredientRepository.save(oldIngredient);
        return "Deleted Ingredient with id: " + id;
    }

}
