package az.company.qwisedemoapp.domain.entity.file;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_subcategories")
public class FileSubcategory extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "category_id", nullable = false)
    private FileCategory category;
}
