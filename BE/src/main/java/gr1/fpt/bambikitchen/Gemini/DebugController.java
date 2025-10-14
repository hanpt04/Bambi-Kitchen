package gr1.fpt.bambikitchen.Gemini;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AgentService agentService;

    /**
     * Kiểm tra tất cả beans có chứa "Dish" trong tên
     */
    @GetMapping("/beans")
    public String checkBeans() {
        return Arrays.stream(context.getBeanDefinitionNames())
                .filter(name -> name.toLowerCase().contains("dish") ||
                        name.toLowerCase().contains("getall"))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Kiểm tra function bean cụ thể
     */
    @GetMapping("/check-function")
    public String checkFunction() {
        try {
            Object bean = context.getBean("getAllDishes");
            return "✅ Bean tồn tại: " + bean.getClass().getName();
        } catch (Exception e) {
            return "❌ Bean KHÔNG tồn tại: " + e.getMessage();
        }
    }

    /**
     * Test service trực tiếp
     */
    @GetMapping("/test-service")
    public String testService() {
        try {
            var dishes = agentService.getAllDishes();
            return "✅ Service hoạt động! Số món: " + dishes.size() +
                    "\nMón đầu tiên: " + (dishes.isEmpty() ? "Không có" : dishes.get(0).getName());
        } catch (Exception e) {
            return "❌ Service lỗi: " + e.getMessage();
        }
    }
}