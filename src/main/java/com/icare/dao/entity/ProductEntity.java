package com.icare.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Double pricePerDay;
    String address;
    boolean deliveryAvailable;
    Double deliveryPricePerKm;
    Double totalRating;
    Integer viewCount;
    Integer rentalCount;
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "city_id")
    CityEntity city;

    @ManyToOne
    @JoinColumn(name = "category_id")
    CategoryEntity category;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @PrePersist
    protected void autoFill() {
        this.status = true;
        this.totalRating = 0.0;
        this.viewCount = 0;
        this.rentalCount = 0;
    }
}
