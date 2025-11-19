package az.company.qwisedemoapp.util;

import org.springframework.data.domain.Sort;

public class SortUtil {

    private SortUtil() {
    }

    public static Sort resolveSort(String sort) {
        Sort sorting = Sort.unsorted();
        if ("latest".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        } else if ("oldest".equals(sort)) {
            sorting = Sort.by(Sort.Direction.ASC, "createdAt");
        } else if ("expensive".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "price");
        } else if ("cheap".equals(sort)) {
            sorting = Sort.by(Sort.Direction.ASC, "price");
        } else if ("top-selling".equals(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "soldCount");
        }
        return sorting;
    }
}
