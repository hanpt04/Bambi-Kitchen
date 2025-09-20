package gr1.fpt.bambikitchen.mapper;

import gr1.fpt.bambikitchen.model.Ingredient;
import gr1.fpt.bambikitchen.model.dto.request.IngredientCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.IngredientUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientMapper INSTANCE = Mappers.getMapper(IngredientMapper.class);

    @Mapping(target = "category", ignore = true)
    Ingredient toIngredient(IngredientCreateRequest ingredient);

    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Ingredient toIngredient(IngredientUpdateRequest ingredient);

}
