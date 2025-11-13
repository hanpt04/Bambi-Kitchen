package gr1.fpt.bambikitchen.Gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr1.fpt.bambikitchen.model.dto.request.DishNutritionRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
@CrossOrigin("*")
public class Gemini {

    private static ChatClient chatClient;
    private static ToolCallback getAllDishesTool;

    @Autowired
    public void init(
            ChatClient.Builder chatClientBuilder,
            @Qualifier("getAllDishes") ToolCallback getAllDishesTool
    ) {
        Gemini.chatClient = chatClientBuilder.build();
        Gemini.getAllDishesTool = getAllDishesTool;
    }

    @GetMapping("/chat")
    public String chat (String message) {
        return chatClient.prompt()
                .user(message)
                .system( systemPrompt2)
                .call()
                .content();
    }

    @SneakyThrows
    public static String roastDish(DishNutritionRequest dishJson) {
        String jsonInput = new ObjectMapper().writeValueAsString(dishJson);
        return chatClient.prompt()
                .user(jsonInput)
                .system(systemPrompt1)
                .call()
                .content();
    }



    @PostMapping("/agent")
    public String agentChat(@RequestBody ChatRequest request) {
        return chatClient.prompt()
                .toolCallbacks(this.getAllDishesTool) // <-- Truyền đúng đối tượng
                .user(request.message())
                .call()
                .content();
    }

        public record ChatRequest(String message) {}

    static String systemPrompt2 = "Bạn là siêu đầu bếp " +
            " có khả năng đề xuất món ăn "+
            " đồng thời là chuyên gia về dinh dưỡng " +
            " giúp người dùng xây dựng thực đơn hàng ngày " ;

    static String systemPrompt1 = "Bạn là Nutrition ROAST Master – thằng bạn thân siêu lầy, chuyên \"nướng\" tô cơm custom của khách bằng tiếng Việt dí dóm, hài hước kiểu TikTok viral. Vai trò: Phân tích dinh dưỡng → tự đánh giá cân bằng → chấm điểm 0-10 → roast 2 câu lầy lội → gợi ý thêm nguyên liệu. Luôn nghĩ như chuyên gia dinh dưỡng vui tính: cân bằng macro (carb ~45-65%, pro ~10-35%, fat ~20-35% tổng calo), calo bữa chính ~500-800, fiber ≥5g, tránh thiếu rau/protein hoặc ngập mỡ/đường.\n" +
            "\n" +
            "NHẬN INPUT: 1 tô JSON (name + ingredients với amount/unit/per/cal/pro/carb/fat/fiber).\n" +
            "BƯỚC 1: Tự nhìn tên nguyên liệu và đi kiếm nutrtion của nó:\n" +
            "- Cộng dồn: calories, protein(g), carb(g), fat(g), fiber(g).\n" +
            "\n" +
            "BƯỚC 2: TỰ ĐÁNH GIÁ ĐIỂM 0-10 (dựa trên input thực tế, không quy tắc chết):\n" +
            "- 9-10: Cân bằng đỉnh (calo vừa, macro hài hòa, fiber cao, đa dạng chất).\n" +
            "- 7-8: Tạm ổn, gu ngon nhưng thiếu tí (ví dụ: protein cao nhưng fiber thấp).\n" +
            "- 5-6: Trung bình, ăn được nhưng cần chỉnh (quá carb hoặc mỡ).\n" +
            "- 0-4: Thảm họa (calo thấp/cao vl, thiếu rau hoàn toàn, kiểu \"ăn cơm trắng + xúc xích = mập ú\").\n" +
            "\n" +
            "BƯỚC 3: ROAST HÀI HƯỚC (2 câu ngắn, dùng icon \uD83D\uDD25\uD83C\uDF57\uD83E\uDD6C\uD83E\uDD5A, lầy kiểu bạn thân):\n" +
            "- Câu 1: Khen/gu/điểm + tóm tô.\n" +
            "- Câu 2: Chỉ trích dí dóm + giục thêm.\n" +
            "- Tối đa 100 ký tự.\n" +
            "\n" +
            "BƯỚC 4: SUGGEST (1 câu ngắn, gợi ý 1-2 nguyên liệu cụ thể + lý do).\n" +
            "\n" +
            "OUTPUT: DUY NHẤT 1 JSON – KHÔNG THÊM CHỮ NÀO. Copy đúng format:\n" +
            "{\n" +
            "  \"score\": 8,\n" +
            "  \"title\": \"8/10 – Gu gym thủ đỉnh cao! \uD83D\uDD25\",\n" +
            "  \"roast\": \"Tô sườn + trứng + gạo lứt = protein bùng nổ, calo vừa vặn! Nhưng fiber hơi khiêm tốn, mai mốt táo bón thì đừng trách bro \uD83E\uDD6C.\",\n" +
            "  \"totals\": {\n" +
            "    \"calories\": 742.0,\n" +
            "    \"protein\": 38.5,\n" +
            "    \"carb\": 48.0,\n" +
            "    \"fat\": 36.0,\n" +
            "    \"fiber\": 3.6\n" +
            "  },\n" +
            "  \"suggest\": \"Thêm 100g rau bina để fiber lên top, lên 10/10 ngay!\"\n" +
            "}\n" +
            "\n" +
            "Bắt đầu roast tô khách vừa custom đi, làm nó cười bò mà vẫn học được! \uD83C\uDF5A\uD83D\uDE0E";

}
