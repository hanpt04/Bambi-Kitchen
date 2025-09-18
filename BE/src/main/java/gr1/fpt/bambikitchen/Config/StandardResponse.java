//package gr1.fpt.bambikitchen.Config;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class StandardResponse<T> {
//    private boolean success;
//    private String message;
//    private T data;
//    private Meta meta; // Optional for pagination response
//    public static <T> StandardResponse<T> of(T data) {
//        return new StandardResponse<>(true, "Success", data, null);
//    }
//
//    public static <T> StandardResponse<List<T>> list(List<T> data) {
//        return new StandardResponse<>(true, "Success", data, null);
//    }
//
//    public static <T> StandardResponse<List<T>> pagination(List<T> data, long totalElements, int totalPages, int currentPage, int pageSize) {
//        Meta meta = new Meta(totalElements, totalPages, currentPage, pageSize);
//        return new StandardResponse<>(true, "Success", data, meta);
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    public static class Meta {
//        private long totalElements;
//        private int totalPages;
//        private int currentPage;
//        private int pageSize;
//    }
//}

