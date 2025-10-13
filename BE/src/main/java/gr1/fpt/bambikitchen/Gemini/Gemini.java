package gr1.fpt.bambikitchen.Gemini;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
public class Gemini {

    private final ChatClient chatClient;

    public Gemini (ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat (String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }


}
