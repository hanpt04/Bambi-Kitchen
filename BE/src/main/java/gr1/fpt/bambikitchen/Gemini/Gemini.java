package gr1.fpt.bambikitchen.Gemini;

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
public class Gemini {

    private final ChatClient chatClient;
    private final ToolCallback getAllDishesTool; // <-- Thêm thuộc tính này

    @Autowired
    public Gemini(
            ChatClient.Builder chatClientBuilder,
            // Yêu cầu Spring inject bean ToolCallback có tên "getAllDishes"
            @Qualifier("getAllDishes") ToolCallback getAllDishesTool
    ) {
        this.chatClient = chatClientBuilder.build();
        this.getAllDishesTool = getAllDishesTool; // <-- Gán bean được inject vào thuộc tính
    }

    @GetMapping("/chat")
    public String chat (String message) {
        return chatClient.prompt()
                .user(message)
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

}
