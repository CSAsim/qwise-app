package az.company.qwisedemoapp.domain.entity.file;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_categories")
public class FileCategory extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileSubcategory> subcategories = new ArrayList<>();

    public void addSubcategory(FileSubcategory subCategory) {
        subcategories.add(subCategory);
        subCategory.setCategory(this);
    }

    public void removeSubcategory(FileSubcategory subCategory) {
        subcategories.remove(subCategory);
        subCategory.setCategory(null);
    }
}
