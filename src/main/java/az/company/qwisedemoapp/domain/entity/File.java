package az.company.qwisedemoapp.domain.entity;

import az.company.qwisedemoapp.model.enums.FileStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "rating", nullable = false)
    private Float rating = 0.0f;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FileStatus status;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserFile> enrolledStudents;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    public void addEnrolledStudent(UserFile userFile) {
        enrolledStudents.add(userFile);
        userFile.setFile(this);
    }

    public void removeEnrolledStudent(UserFile userFile) {
        enrolledStudents.remove(userFile);
        userFile.setFile(null);
    }
}
