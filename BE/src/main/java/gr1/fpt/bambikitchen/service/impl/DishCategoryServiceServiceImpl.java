package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.DishCategoryMapper;
import gr1.fpt.bambikitchen.model.DishCategory;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryUpdateRequest;
import gr1.fpt.bambikitchen.repository.DishCategoryRepository;
import gr1.fpt.bambikitchen.service.DishCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DishCategoryServiceServiceImpl implements DishCategoryService {

    private final DishCategoryRepository dishCategoryRepository;
    private final DishCategoryMapper dishCategoryMapper;

    @Override
    public DishCategory save(DishCategoryCreateRequest dishCategory) {
        if (dishCategoryRepository.existsByName(dishCategory.getName())) {
            throw new CustomException("DishCategory already exists",
                                        HttpStatus.BAD_REQUEST);
        }

        return dishCategoryRepository.save(dishCategoryMapper.toDishCategory(dishCategory));
    }

    @Override
    public List<DishCategory> findAll() {
        return dishCategoryRepository.findAll();
    }

    @Override
    public DishCategory findById(int id) {
        return dishCategoryRepository.findById(id).orElseThrow(() ->
                new CustomException("DishCategory does not exists",
                                    HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public DishCategory update(DishCategoryUpdateRequest dishCategory) {
        if (!dishCategoryRepository.existsById(dishCategory.getId())) {
            throw new CustomException("DishCategory cannot be found " + dishCategory.getId(),
                                        HttpStatus.BAD_REQUEST);
        }

        return dishCategoryRepository.save(dishCategoryMapper.toDishCategory(dishCategory));
    }

    @Override
    public String deleteById(int id) {
        if  (!dishCategoryRepository.existsById(id)) {
            throw new CustomException("DishCategory cannot be found " + id,
                                        HttpStatus.BAD_REQUEST);
        }
        dishCategoryRepository.deleteById(id);
        return "Deleted DishCategory " + id;
    }
}
