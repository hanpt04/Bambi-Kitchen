package gr1.fpt.bambikitchen.Gemini;

import gr1.fpt.bambikitchen.model.Dish;
import gr1.fpt.bambikitchen.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    @Autowired
    DishRepository dishRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }
}
