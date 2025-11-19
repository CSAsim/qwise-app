package az.company.qwisedemoapp.domain.entity;

import az.company.qwisedemoapp.domain.entity.packet.Packet;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
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
@SuperBuilder
@ToString
@Table(name = "user_packet")
@NoArgsConstructor
@AllArgsConstructor
public class UserPacket extends BaseEntity {

    @Column(name = "progress", nullable = false)
    private Float progress;

    @Column(name = "usage_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PacketUsageStatus usageStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "packet_id")
    @ToString.Exclude
    private Packet packet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    @ToString.Exclude
    private User student;

    @OneToMany(mappedBy = "userPacket", cascade = CascadeType.ALL)
    private List<UserPacketAttempt> attempts;

    public void addAttempt(UserPacketAttempt attempt) {
        attempts.add(attempt);
        attempt.setUserPacket(this);
    }

    public void removeAttempt(UserPacketAttempt attempt) {
        attempts.remove(attempt);
        attempt.setUserPacket(null);
    }
}
