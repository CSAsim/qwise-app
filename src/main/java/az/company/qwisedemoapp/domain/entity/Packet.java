package az.company.qwisedemoapp.domain.entity;

import az.company.qwisedemoapp.domain.entity.test.question.InterviewQuestion;
import az.company.qwisedemoapp.domain.entity.test.question.Question;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import az.company.qwisedemoapp.model.enums.PacketType;
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
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "packets")
public class Packet extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "description")
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PacketStatus status;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PacketType type;

    @JoinColumn(name = "author_id")
    @ToString.Exclude
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
    private User author;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "packet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserPacket> enrolledStudents = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "packet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        questions.add(question);
        question.setPacket(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setPacket(null);
    }

    public void addEnrolledStudent(UserPacket userPacket) {
        enrolledStudents.add(userPacket);
        userPacket.setPacket(this);
    }

    public void removeEnrolledStudent(UserPacket userPacket) {
        enrolledStudents.remove(userPacket);
        userPacket.setPacket(null);
    }
}
