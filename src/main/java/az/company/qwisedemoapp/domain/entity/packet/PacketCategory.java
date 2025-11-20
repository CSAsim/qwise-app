package az.company.qwisedemoapp.domain.entity.packet;

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
@Table(name = "packet_categories")
public class PacketCategory extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PacketSubcategory> subCategories = new ArrayList<>();

    public void addSubCategory(PacketSubcategory subCategory) {
        subCategories.add(subCategory);
        subCategory.setCategory(this);
    }

    public void removeSubCategory(PacketSubcategory subCategory) {
        subCategories.remove(subCategory);
        subCategory.setCategory(null);
    }
}
