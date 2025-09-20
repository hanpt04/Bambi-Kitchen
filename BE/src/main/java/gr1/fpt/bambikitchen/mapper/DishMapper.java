package gr1.fpt.bambikitchen.mapper;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.model.DishCategory;
import gr1.fpt.bambikitchen.model.dto.request.DishCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DishUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DishMapper {

    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    Dish toDish(DishCreateRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "category", ignore = true)
    Dish toDish(DishUpdateRequest request);
}
