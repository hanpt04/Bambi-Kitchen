package gr1.fpt.bambikitchen.repository;

import gr1.fpt.bambikitchen.model.DishTemplate;
import gr1.fpt.bambikitchen.model.enums.SizeCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishTemplateRepository  extends JpaRepository<DishTemplate, SizeCode> {
}
