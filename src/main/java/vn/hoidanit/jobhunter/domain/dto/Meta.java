package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page; // trang hiện tại
    private int pageSize; // số lượng bản ghi trên 1 trang
    private int pages; // tổng số trang
    private long total; // tổng số lượng bản ghi
}
