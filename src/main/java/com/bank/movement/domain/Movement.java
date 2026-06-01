package com.bank.movement.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movements")
public class Movement {
    @Id
    private String id;
    private String customerId;
    private String productId;
    private ProductType productType;
    private MovementType movementType;
    private BigDecimal amount;
    private String description;
    private Boolean active;
    private Date createdAt;
}