package com.icare.entity;

import com.icare.enums.DeliveryMethod;
import com.icare.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.icare.enums.RentalStatus.ORDER_PLACED;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rentals")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDate rentalStartDate;
    LocalDate rentalEndDate;
    Double rentalCost;
    String deliveryAddress;
    Boolean reviewWritten;

    @Enumerated(EnumType.STRING)
    DeliveryMethod deliveryMethod;
    Double deliveryCost;

    @Enumerated(EnumType.STRING)
    DeliveryMethod returnedDeliveryMethod;
    Double returnedDeliveryCost;

    @Enumerated(EnumType.STRING)
    RentalStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    OrderEntity order;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @PrePersist
    protected void autoFill(){
        this.reviewWritten = false;
        this.status = ORDER_PLACED;
    }
}