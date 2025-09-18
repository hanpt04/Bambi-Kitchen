package gr1.fpt.bambikitchen.mapper;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.AccountUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    /**
     * Map CreateAccountRequest -> Account
     */
    Account toEntity(AccountCreateRequest request);

    /**
     * Map UpdateAccountRequest -> Account
     */
    Account toEntity(AccountUpdateRequest request);

    /**
     * Map Account -> UpdateAccountRequest (for prefilling forms if needed).
     */
    AccountUpdateRequest toUpdateDto(Account account);
}
