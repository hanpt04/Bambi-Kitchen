package gr1.fpt.bambikitchen.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonExtractor {

    public static String extractJsonFromGemini(String geminiResponse) {
        if (geminiResponse == null || geminiResponse.trim().isEmpty()) {
            return "{}";
        }

        String cleaned = geminiResponse.trim();

        // Xóa ```json {} ```
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.replaceFirst("^```json\\s*", "");
            cleaned = cleaned.replaceFirst("```\\s*$", "");
        }

        // Xóa ``` {} ```
        else if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceFirst("^```\\s*", "");
            cleaned = cleaned.replaceFirst("```\\s*$", "");
        }

        // Trim whitespace
        cleaned = cleaned.trim();

        // Validate xem có đúng dạng Json không (bắt đầu bằng { hoặc [)
        if (!cleaned.startsWith("{") && !cleaned.startsWith("[")) {
            // nếu không thì cố mà tìm cho đúng dạng
            int jsonStart = cleaned.indexOf('{');
            int jsonEnd = cleaned.lastIndexOf('}');

            // nếu mà tìm thấy thì cắt chuỗi, lấy {data}, không thì báo lỗi không có json
            if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                cleaned = cleaned.substring(jsonStart, jsonEnd + 1);
            } else {
                throw new IllegalArgumentException("No valid JSON found in response");
            }
        }

        return cleaned;
    }
}
