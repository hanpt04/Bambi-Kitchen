package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.DishMapper;
import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.DishCategory;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import gr1.fpt.bambikitchen.repository.*;
import gr1.fpt.bambikitchen.service.DishService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final AccountRepository accountRepository;
    private final DishCategoryRepository dishCategoryRepository;
    private final DishMapper dishMapper;

    @Override
    public Dish save(DishCreateRequest dish) {
        Account account = accountRepository.findById(dish.getAccountId()).orElseThrow(
                () -> new CustomException("Account not found " + dish.getAccountId(), HttpStatus.BAD_REQUEST)
        );

        DishCategory category = dishCategoryRepository.findById(dish.getCategoryId()).orElseThrow(
                () -> new CustomException("Category not found " + dish.getCategoryId(), HttpStatus.BAD_REQUEST)
        );

        Dish newDish = dishMapper.toDish(dish);
        newDish.setAccount(account);
        newDish.setCategory(category);

        return dishRepository.save(newDish);
    }

    @Override
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    @Override
    public Dish findById(int id) {
        return dishRepository.findById(id).orElseThrow(() ->
                new CustomException("Dish not found " + id, HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Dish update(DishUpdateRequest dish) {
        Dish oldDish = dishRepository.findById(dish.getId()).orElseThrow(
                () -> new CustomException("Dish not found " + dish.getId(), HttpStatus.BAD_REQUEST)
        );

        Account account = accountRepository.findById(dish.getAccountId()).orElseThrow(
                () -> new CustomException("Account not found " + dish.getAccountId(), HttpStatus.BAD_REQUEST)
        );

        DishCategory category = dishCategoryRepository.findById(dish.getCategoryId()).orElseThrow(
                () -> new CustomException("Category not found " + dish.getCategoryId(), HttpStatus.BAD_REQUEST)
        );

        Dish newDish = dishMapper.toDish(dish);
        newDish.setId(oldDish.getId()); // get Id from oldDish
        newDish.setAccount(account);
        newDish.setCategory(category);

        return dishRepository.save(newDish);
    }

    @Override
    public String deleteById(int id) {
        Dish dish = findById(id);
        dish.setActive(false);
        dishRepository.save(dish);
        return "Deleted " + dish.getId();
    }
}
