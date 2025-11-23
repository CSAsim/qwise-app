package az.company.qwisedemoapp.filter;

import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminResourceSpecificationFilter<E> {

    public Specification<E> byFilters(FilteredRequestDto request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSearch() != null && !request.getSearch().isEmpty()) {
                String q = "%" + request.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), q),
                        cb.like(cb.lower(root.get("description")), q),
                        cb.like(cb.lower(root.get("category").get("name")), q),
                        cb.like(cb.lower(root.get("subcategory").get("name")), q),
                        cb.like(cb.lower(root.get("authorName")), q)
                ));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            if (request.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), request.getUserId()));
            }

            if (request.getCategory() != null) {
                predicates.add(cb.equal(root.get("category").get("name"), request.getCategory()));
            }

            if (request.getSubCategory() != null) {
                predicates.add(cb.equal(root.get("subcategory").get("name"), request.getSubCategory()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
