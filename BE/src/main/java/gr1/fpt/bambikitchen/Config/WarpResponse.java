package gr1.fpt.bambikitchen.Config;

import gr1.fpt.bambikitchen.model.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarpResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public static <T> WarpResponse <T>  of(ResponseStatus status, T data) {
        return new WarpResponse<>(
                status.getCode(),
                status.getMessage(),
                data
        );
    }
}

