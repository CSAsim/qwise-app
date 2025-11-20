package az.company.qwisedemoapp.filter;

import az.company.qwisedemoapp.domain.entity.file.File;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class FileSpecificationFilter {

    private FileSpecificationFilter() {
    }

    public static Specification<File> byFilters(FilteredRequestDto request) {

        return (root, query, criterialBuilder) -> {
            Predicate predicate = criterialBuilder.conjunction();
            if (request.getAuthorId() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("author").get("id"), request.getAuthorId()));
            }

            if (request.getCategory() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("category").get("name"), request.getCategory()));
            }

            if (request.getStatus() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getSubCategory() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("subcategory").get("name"), request.getSubCategory()));
            }
            return predicate;
        };
    }
}
