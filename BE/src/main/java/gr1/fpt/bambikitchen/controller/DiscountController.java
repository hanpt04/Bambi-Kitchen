//package gr1.fpt.bambikitchen.controller;
//
//import gr1.fpt.bambikitchen.model.Discount;
//import gr1.fpt.bambikitchen.model.dto.request.DiscountCreateRequest;
//import gr1.fpt.bambikitchen.model.dto.request.DiscountUpdateRequest;
//import gr1.fpt.bambikitchen.service.DiscountService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/api/discount")
//@CrossOrigin(origins = "*")
//public class DiscountController {
//
//    private final DiscountService discountService;
//
//    @PostMapping
//    public ResponseEntity<Discount> save(@RequestBody DiscountCreateRequest discount){
//        return ResponseEntity.ok(discountService.save(discount));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Discount>> findAll(){
//        return ResponseEntity.ok(discountService.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Discount> findById(@PathVariable Integer id){
//        return ResponseEntity.ok(discountService.findById(id));
//    }
//
//    @PutMapping
//    public ResponseEntity<Discount> update(@RequestBody DiscountUpdateRequest discount){
//        return ResponseEntity.ok(discountService.update(discount));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> delete(@PathVariable Integer id){
//        return ResponseEntity.ok(discountService.deleteById(id));
//    }
//
//}
