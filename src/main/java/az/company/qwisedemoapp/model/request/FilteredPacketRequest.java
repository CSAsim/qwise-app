package az.company.qwisedemoapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilteredPacketRequest {

    private Long authorId;
    private String category;
    private String subCategory;
    private String status;
}
