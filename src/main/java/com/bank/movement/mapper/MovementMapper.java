package com.bank.movement.mapper;

import com.bank.movement.api.dto.MovementRequest;
import com.bank.movement.api.dto.MovementResponse;
import com.bank.movement.domain.Movement;
import com.bank.movement.domain.MovementType;
import com.bank.movement.domain.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Component
public class MovementMapper {

    public Movement toEntity(MovementRequest request) {
        return Movement.builder()
                .id(null)
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .productType(ProductType.valueOf(request.getProductType()))
                .movementType(MovementType.valueOf(request.getMovementType()))
                .amount(toBigDecimal(request.getAmount()))
                .description(request.getDescription())
                .active(Boolean.TRUE)
                .createdAt(new Date())
                .build();
    }

    public MovementResponse toResponse(Movement movement) {
        MovementResponse response = new MovementResponse();

        response.setId(movement.getId());
        response.setCustomerId(movement.getCustomerId());
        response.setProductId(movement.getProductId());
        response.setProductType(movement.getProductType().name());
        response.setMovementType(movement.getMovementType().name());
        response.setAmount(toDouble(movement.getAmount()));
        response.setDescription(movement.getDescription());
        response.setActive(movement.getActive());
        response.setCreatedAt(Date.from(movement.getCreatedAt().toInstant()));
        return response;
    }

    public Movement updateEntity(Movement movement, MovementRequest request) {
        movement.setCustomerId(request.getCustomerId());
        movement.setProductId(request.getProductId());
        movement.setProductType(ProductType.valueOf(request.getProductType()));
        movement.setMovementType(MovementType.valueOf(request.getMovementType()));
        movement.setAmount(toBigDecimal(request.getAmount()));
        movement.setDescription(request.getDescription());
        return movement;
    }

    private BigDecimal toBigDecimal(Double value) {
        return value == null ? BigDecimal.ZERO : BigDecimal.valueOf(value);
    }

    private Double toDouble(BigDecimal value) {
        return value == null ? 0.0 : value.doubleValue();
    }
}