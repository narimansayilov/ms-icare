package com.icare.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "rental")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String pin;
    String name;
    String surname;
    LocalDate birthDay;
    String email;
    String password;
    String phoneNumber;
    String photoUrl;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            schema = "rental"
    )
    List<RoleEntity> roles;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
