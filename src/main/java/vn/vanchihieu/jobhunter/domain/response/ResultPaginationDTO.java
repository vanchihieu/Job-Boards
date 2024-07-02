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
        private int page;
        private int pageSize;
        private int pages;
        private long total;
    }
}
