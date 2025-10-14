package gr1.fpt.bambikitchen.Gemini;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import gr1.fpt.bambikitchen.model.Dish;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class AgentFunction {

    @Autowired
    private AgentService agentService;

    /**
     * Request class - để trống vì function này không cần params
     */
    public record GetAllDishesRequest() {}

    /**
     * Function Bean - Spring AI 1.1.0-SNAPSHOT sẽ TỰ ĐỘNG DETECT
     * Gemini sẽ tự động gọi function này khi user hỏi về menu
     */


    @Bean
    public ToolCallback getAllDishes() {

        // Bước 1: Tạo ra Function như bình thường
        Function<GetAllDishesRequest, List<Dish>> getAllDishesFunction = request -> {
            System.out.println("🔥 Function getAllDishes() được gọi!");
            return agentService.getAllDishes();
        };

        // Bước 2: DÙNG BUILDER CỦA FunctionToolCallback theo đúng tài liệu
        return FunctionToolCallback.builder("getAllDishes", getAllDishesFunction)
                .description("Lấy tất cả các món ăn có trong thực đơn của nhà hàng bao gồm tên, giá và mô tả")
                .inputType(GetAllDishesRequest.class)
                .build();
    }
}
