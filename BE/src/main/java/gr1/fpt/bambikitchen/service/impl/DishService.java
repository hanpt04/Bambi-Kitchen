package gr1.fpt.bambikitchen.service.impl;


import gr1.fpt.bambikitchen.Utils.FileUtil;
import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.Recipe;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishDtoRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import gr1.fpt.bambikitchen.model.enums.DishType;
import gr1.fpt.bambikitchen.repository.DishRepository;
import gr1.fpt.bambikitchen.repository.IngredientRepository;
import gr1.fpt.bambikitchen.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DishService {

    @Autowired
    DishRepository dishRepository;

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Dish save(DishCreateRequest request) {
        Dish dish = Dish.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .dishType(request.getDishType())
                .isActive(true)
                .isPublic(request.isPublic())
                .account(request.getAccount())
                .build();

        if (request.getId() != null) {
            dish.setId(request.getId());
            dish.setActive(request.isActive());
            deleteRecipeWithDishId(request.getId());
        }


        Dish savedDish = dishRepository.save(dish);
        saveRecipe(request.getIngredients(), savedDish);

        return savedDish;
    }

    @Transactional
    public Dish saveMenu(DishCreateRequest request) throws IOException {
        if (request.getId() == null) {
            Dish dish = Dish.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .dishType(request.getDishType())
                    .isActive(true)
                    .isPublic(request.isPublic())
                    .account(request.getAccount())
                    .usedQuantity(request.getUsedQuantity())
                    .build();
            Dish savedDish = dishRepository.save(dish);
            saveRecipe(request.getIngredients(), savedDish);

            //publisher
            if (!request.getFile().isEmpty()) {
                String absolutePath = FileUtil.saveFile(request.getFile());
                File file = FileUtil.getFileByPath(absolutePath);
                MultipartFile multipartFile = FileUtil.convertFileToMultipart(file);
                file.delete();
                applicationEventPublisher.publishEvent(new DishDtoRequest(savedDish, multipartFile));
            }
            return savedDish;
        } else {
            Dish dishExist = dishRepository.findById(request.getId()).get();
            Dish dish = new Dish();
            dish.setId(dishExist.getId());
            dish.setName(request.getName());
            dish.setDescription(request.getDescription());
            dish.setPrice(request.getPrice());
            dish.setDishType(request.getDishType());
            dish.setActive(request.isActive());
            dish.setAccount(request.getAccount());
            dish.setPublic(request.isPublic());
            dish.setUsedQuantity(request.getUsedQuantity());
            deleteRecipeWithDishId(request.getId());
            if (dishExist.getImageUrl() != null) {
                System.out.println("Img" + dishExist.getImageUrl());
                dish.setImageUrl(dishExist.getImageUrl());
                dish.setPublicId(dishExist.getPublicId());
            }
            Dish savedDish = dishRepository.save(dish);
            if (!request.getFile().isEmpty()) {
                String path = FileUtil.saveFile(request.getFile());
                File file = FileUtil.getFileByPath(path);
                MultipartFile multipartFile = FileUtil.convertFileToMultipart(file);
                file.delete();
                applicationEventPublisher.publishEvent(new DishDtoRequest(savedDish, multipartFile));
            }
            saveRecipe(request.getIngredients(), savedDish);
            return savedDish;
        }
    }

    public Dish update(DishUpdateRequest dto) throws IOException {
        Dish dishExist = dishRepository.findById(dto.getId()).get();
        if ( dishExist == null ) {
            throw new CustomException("Dish not found", HttpStatus.BAD_REQUEST);
        }
        Dish dish = new Dish();
        dish.setId(dishExist.getId());
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setPrice(dto.getPrice());
        dish.setDishType(dto.getDishType());
        dish.setActive(dto.isActive());
        dish.setAccount(dto.getAccount());
        dish.setPublic(dto.isPublic());
        dish.setUsedQuantity(dto.getUsedQuantity());
        if (dishExist.getImageUrl() != null) {
            System.out.println("Img" + dishExist.getImageUrl());
            dish.setImageUrl(dishExist.getImageUrl());
            dish.setPublicId(dishExist.getPublicId());
        }
        Dish savedDish = dishRepository.save(dish);
        if (!dto.getFile().isEmpty()) {
            String path = FileUtil.saveFile(dto.getFile());
            File file = FileUtil.getFileByPath(path);
            MultipartFile multipartFile = FileUtil.convertFileToMultipart(file);
            file.delete();
            applicationEventPublisher.publishEvent(new DishDtoRequest(savedDish, multipartFile));
        }
        return savedDish;
    }

    public void saveRecipe(Map<Integer, Integer> ingredients, Dish dish) {
        ingredients.forEach((ingredientId, quantity) -> {

            Recipe recipe = Recipe.builder()
                    .ingredient(ingredientRepository.findById(ingredientId).orElseThrow(() -> new CustomException("Ingredient not found", HttpStatus.BAD_REQUEST)))
                    .quantity(quantity)
                    .dish(dish)
                    .build();
            recipeRepository.save(recipe);
        });
    }

    public void deleteRecipeWithDishId(int dishId) {
        recipeRepository.deleteByDish_Id(dishId);
    }


    public Map<Integer, Integer> getIngredientsByDishId(int dishId) {
        Map<Integer, Integer> ingredients = new HashMap<>();
        for (Recipe recipe : recipeRepository.getIngredientsByDish_Id(dishId)) {
            ingredients.put(recipe.getIngredient().getId(), recipe.getQuantity());
        }
        return ingredients;
    }

    public boolean togglePublic(int dishId) {
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish != null) {
            if (dish.isPublic()) {
                dish.setPublic(false);
            } else {
                dish.setPublic(true);
            }
            dishRepository.save(dish);
            return true;
        }
        return false;
    }

    public boolean toggleActive(int dishId) {
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish != null) {
            if (dish.isActive()) {
                dish.setActive(false);
            } else {
                dish.setActive(true);
            }
            dishRepository.save(dish);
            return true;
        }
        return false;
    }

    public void customToPreset(int id, boolean isPublic) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new CustomException("Dish not found", HttpStatus.BAD_REQUEST));
        dish.setPublic(isPublic);
        dish.setDishType(DishType.PRESET);
        dishRepository.save(dish);
    }

    public Dish getDishById(int id) {
        return dishRepository.findById(id).orElseThrow(() -> new CustomException("Dish not found", HttpStatus.BAD_REQUEST));
    }

    public List<Dish> getAll() {
        return dishRepository.findAllByIsActiveAndIsPublic(true, true);
    }

    public List<Dish> getDishedByAccount(int accountId) {
        return dishRepository.findByAccount_Id((accountId));
    }

    public List<Dish> getAllDish() {
        return dishRepository.findAll();
    }

    public List<Dish> getTop5Dish() {
        return dishRepository.findTop5ByOrderByUsedQuantityDesc();
    }
}
