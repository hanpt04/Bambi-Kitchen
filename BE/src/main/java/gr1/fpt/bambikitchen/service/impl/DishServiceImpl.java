package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.DishMapper;
import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import gr1.fpt.bambikitchen.repository.DishRepository;
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
    private final DishMapper dishMapper;

    @Override
    public Dish save(DishCreateRequest account) {
        return dishRepository.save(dishMapper.toDish(account));
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
    public Dish update(DishUpdateRequest account) {
        return dishRepository.save(dishMapper.toDish(account));
    }

    @Override
    public String deleteById(int id) {
        Dish dish = findById(id);
        dish.setActive(false);
        dishRepository.save(dish);
        return "Deleted " + dish.getId();
    }
}
