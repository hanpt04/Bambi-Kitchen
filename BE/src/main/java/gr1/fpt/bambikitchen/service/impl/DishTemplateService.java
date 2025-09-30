package gr1.fpt.bambikitchen.service.impl;


import gr1.fpt.bambikitchen.model.DishTemplate;
import gr1.fpt.bambikitchen.model.enums.SizeCode;
import gr1.fpt.bambikitchen.repository.DishTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishTemplateService {

    @Autowired
    DishTemplateRepository dishTemplateRepository;

   public DishTemplate findBySizeCode(SizeCode sizeCode) {
        return dishTemplateRepository.findById(sizeCode).orElse(null);
    }

    public List<DishTemplate> getAllDishTemplates() {
        return dishTemplateRepository.findAll();
    }

    public DishTemplate saveDishTemplate(DishTemplate dishTemplate) {
        return dishTemplateRepository.save(dishTemplate);
    }

    public void deleteDishTemplate(SizeCode sizeCode) {
        dishTemplateRepository.deleteById(sizeCode);
    }



}
