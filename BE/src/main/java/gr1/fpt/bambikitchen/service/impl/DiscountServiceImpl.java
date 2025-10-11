//package gr1.fpt.bambikitchen.service.impl;
//
//import gr1.fpt.bambikitchen.exception.CustomException;
//import gr1.fpt.bambikitchen.mapper.DiscountMapper;
//import gr1.fpt.bambikitchen.model.Discount;
//import gr1.fpt.bambikitchen.model.dto.request.DiscountCreateRequest;
//import gr1.fpt.bambikitchen.model.dto.request.DiscountUpdateRequest;
//import gr1.fpt.bambikitchen.repository.DiscountRepository;
//import gr1.fpt.bambikitchen.service.DiscountService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//public class DiscountServiceImpl implements DiscountService {
//
//    private final DiscountRepository discountRepository;
//    private final DiscountMapper discountMapper;
//
//    @Override
//    public Discount save(DiscountCreateRequest discount) {
//        if (discountRepository.existsByCode(discount.getCode())) {
//            throw new CustomException("Discount already exists",
//                                        HttpStatus.CONFLICT);
//        }
//
//        return discountRepository.save(discountMapper.toDiscount(discount));
//    }
//
//    @Override
//    public List<Discount> findAll() {
//        return discountRepository.findAll();
//    }
//
//    @Override
//    public Discount findById(int id) {
//        return discountRepository.findById(id).orElseThrow(() ->
//                new CustomException("discount not found" + id,
//                                    HttpStatus.BAD_REQUEST)
//        );
//    }
//
//    @Override
//    public Discount update(DiscountUpdateRequest discount) {
//        Discount oldDiscount = discountRepository.findById(discount.getId()).orElseThrow(
//                () -> new CustomException("Discount cannot be found " + discount.getId(), HttpStatus.BAD_REQUEST)
//        );
//
//        Discount newDiscount = discountMapper.toDiscount(discount);
//        newDiscount.setId(oldDiscount.getId());
//
//        return discountRepository.save(newDiscount);
//    }
//
//    @Override
//    public String deleteById(int id) {
//        Discount discount = discountRepository.findById(id).orElseThrow(() ->
//                new CustomException("discount not found" + id,
//                                      HttpStatus.BAD_REQUEST)
//        );
//        discount.setEndTime(Date.valueOf(LocalDate.now()));
//        discountRepository.save(discount);
//        return "delete from discount where id = " + discount.getId();
//    }
//}
