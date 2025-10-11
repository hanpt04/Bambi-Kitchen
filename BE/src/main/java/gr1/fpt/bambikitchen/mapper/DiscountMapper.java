//package gr1.fpt.bambikitchen.mapper;
//
//import gr1.fpt.bambikitchen.model.Discount;
//import gr1.fpt.bambikitchen.model.dto.request.DiscountCreateRequest;
//import gr1.fpt.bambikitchen.model.dto.request.DiscountUpdateRequest;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Mappings;
//
//@Mapper(componentModel = "spring")
//public interface DiscountMapper {
//
//    Discount toDiscount(DiscountCreateRequest discount);
//
//    @Mappings({
//            @Mapping(target = "id", ignore = true),
//            @Mapping(target = "code", ignore = true)
//    })
//    Discount toDiscount(DiscountUpdateRequest discount);
//}
