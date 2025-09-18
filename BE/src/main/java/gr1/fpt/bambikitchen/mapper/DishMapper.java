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


    @Mapping(target = "account", source = "accountId")
    @Mapping(target = "category", source = "categoryId")
    Dish toDish(DishCreateRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", source = "accountId")
    @Mapping(target = "category", source = "categoryId")
    Dish toDish(DishUpdateRequest request);

    default Account mapAccount(Integer accountId) {
        if (accountId == null) {
            return null;
        }
        Account account = new Account();
        account.setId(accountId);
        return account;
    }

    default DishCategory mapCategory(Integer categoryId) {
        if (categoryId == null) {
            return null;
        }
        DishCategory category = new DishCategory();
        category.setId(categoryId);
        return category;
    }
}
