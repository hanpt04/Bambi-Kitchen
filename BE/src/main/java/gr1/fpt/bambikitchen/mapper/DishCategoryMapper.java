package gr1.fpt.bambikitchen.mapper;

import gr1.fpt.bambikitchen.model.DishCategory;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DishCategoryMapper {

    DishCategoryMapper INSTANCE = Mappers.getMapper(DishCategoryMapper.class);

    DishCategory toDishCategory(DishCategoryCreateRequest request);

    DishCategory toDishCategory(DishCategoryUpdateRequest request);

}