package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.DishTemplate;
import gr1.fpt.bambikitchen.model.enums.SizeCode;
import gr1.fpt.bambikitchen.service.impl.DishTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dish-template")
public class DishTemplateController {

    @Autowired
    DishTemplateService dishTemplateService;

    @GetMapping
    public List<DishTemplate> findAll() {
        return dishTemplateService.getAllDishTemplates();
    }

    @PostMapping
    public DishTemplate create(@RequestBody  DishTemplate dishTemplate) {
        System.out.println(dishTemplate);
        return dishTemplateService.saveDishTemplate(dishTemplate);
    }

    @GetMapping ("/{sizeCode}")
    public DishTemplate findBySizeCode(@PathVariable SizeCode sizeCode) {
        return dishTemplateService.findBySizeCode(sizeCode);
    }

    @DeleteMapping("/{sizeCode}")
    public void delete(@PathVariable SizeCode sizeCode) {
        dishTemplateService.deleteDishTemplate( sizeCode);
    }



}
