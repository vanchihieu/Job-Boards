package vn.vanchihieu.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter
    public static class Meta { // dùng static để có thể truy cập trực tiếp từ class cha mà không cần khởi tạo đối tượng
        private int page; // trang hiện tại
        private int pageSize; // số lượng phần tử trên mỗi trang
        private int pages; // tổng số trang có thể phân trang
        private long total; // tổng số phần tử trong database
    }
}
