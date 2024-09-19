package com.icare.entity;

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
    Boolean deliveryAvailable;
    Double deliveryPricePerKm;
    Integer viewCount;
    Integer rentalCount;
    Integer totalOfRatings;
    Integer reviewCount;
    Integer favoriteCount;
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
        this.totalOfRatings = 0;
        this.viewCount = 0;
        this.rentalCount = 0;
        this.reviewCount = 0;
        this.favoriteCount = 0;
    }
}
