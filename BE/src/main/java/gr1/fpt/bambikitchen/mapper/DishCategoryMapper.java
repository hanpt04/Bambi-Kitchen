package gr1.fpt.bambikitchen.mapper;

import gr1.fpt.bambikitchen.model.dto.request.DishCategoryCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishCategoryUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DishCategoryMapper {

    DishCategoryMapper INSTANCE = Mappers.getMapper(DishCategoryMapper.class);

    DishCategory toDishCategory(DishCategoryCreateRequest request);


    @Mapping(target = "id", ignore = true)
    DishCategory toDishCategory(DishCategoryUpdateRequest request, @MappingTarget DishCategory dishCategory);

}