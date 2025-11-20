package az.company.qwisedemoapp.domain.entity.file;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserFile;
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
@Table(name = "files")
public class File extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private FileCategory category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private FileSubcategory subcategory;

    @Column(name = "description")
    private String description;

    @Builder.Default
    @Column(name = "sold_count", nullable = false)
    private Integer soldCount = 0;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FileStatus status;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserFile> enrolledStudents = new ArrayList<>();

    @ToString.Exclude
    @JoinColumn(name = "author_id")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
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
