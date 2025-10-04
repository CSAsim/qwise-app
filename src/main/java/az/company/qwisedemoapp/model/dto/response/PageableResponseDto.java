package az.company.qwisedemoapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PageableResponseDto<T>{

    private List<T> data;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPagesCount;

    public PageableResponseDto(Page<T> page) {
        this.data = page.getContent();
        this.pageNumber = page.getPageable().getPageNumber();
        this.pageSize = page.getPageable().getPageSize();
        this.totalElements = page.getTotalElements();
        this.totalPagesCount = page.getTotalPages();
    }
}
