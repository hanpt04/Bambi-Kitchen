package gr1.fpt.bambikitchen.mapper;

import gr1.fpt.bambikitchen.model.Discount;
import gr1.fpt.bambikitchen.model.dto.request.DiscountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.DiscountUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountMapper {

    Discount toDiscount(DiscountCreateRequest discount);

    Discount toDiscount(DiscountUpdateRequest discount);
}
