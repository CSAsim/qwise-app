package az.company.qwisedemoapp.domain.entity;

import az.company.qwisedemoapp.domain.entity.file.File;
import az.company.qwisedemoapp.domain.entity.packet.Packet;
import az.company.qwisedemoapp.model.enums.UserRole;
import az.company.qwisedemoapp.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @JsonIgnore
    @Column(name = "password")
    @ToString.Exclude
    private String password;

    @Column(name = "provider")
    private String provider;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OtpCode> otpCodes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<PasswordResetToken> passwordResetToken;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Packet> packets;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<File> files;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserPacket> enrolledPackets;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserFile> userEnrolledFiles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Contact> contacts;

    public void addOtpCode(OtpCode otpCode) {
        otpCodes.add(otpCode);
        otpCode.setUser(this);
    }

    public void removeOtpCode(OtpCode otpCode) {
        otpCodes.remove(otpCode);
        otpCode.setUser(null);
    }

    public void addPacket(Packet packet) {
        packets.add(packet);
        packet.setAuthor(this);
    }

    public void removePacket(Packet packet) {
        packets.remove(packet);
        packet.setAuthor(null);
    }

    public void addFile(File file) {
        file.setAuthor(this);
        this.files.add(file);
    }

    public void removeFile(File file) {
        file.setAuthor(null);
        this.files.remove(file);
    }

    public void addEnrolledPacket(UserPacket userPacket) {
        enrolledPackets.add(userPacket);
        userPacket.setStudent(this);
    }

    public void removeEnrolledPacket(UserPacket userPacket) {
        enrolledPackets.remove(userPacket);
        userPacket.setStudent(null);
    }

    public void addEnrolledFile(UserFile file) {
        userEnrolledFiles.add(file);
        file.setStudent(this);
    }

    public void removeEnrolledFile(UserFile file) {
        userEnrolledFiles.remove(file);
        file.setStudent(null);
    }
}
