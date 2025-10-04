package az.company.qwisedemoapp.filter;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PacketSpecificationFilter {

    private PacketSpecificationFilter() {
    }

    public static Specification<Packet> byFilters(FilteredRequestDto request) {

        return (root, query, criterialBuilder) -> {
            Predicate predicate = criterialBuilder.conjunction();
            if (request.getAuthorId() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("author").get("id"), request.getAuthorId()));
            }

            if (request.getCategory() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("category"), request.getCategory()));
            }

            if (request.getStatus() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getSubCategory() != null) {
                predicate = criterialBuilder
                        .and(predicate, criterialBuilder.equal(root.get("subCategory"), request.getSubCategory()));
            }
            return predicate;
        };
    }
}
