package gr1.fpt.bambikitchen.model.dto.response;

import lombok.Data;

@Data
public class GeminiResponseDto {
    private int score;
    private String title;
    private String roast;
    private Totals totals;
    private String suggest;

    @Data
    public static class Totals {
        private double calories;
        private double protein;
        private double carb;
        private double fat;
        private double fiber;
    }
}
