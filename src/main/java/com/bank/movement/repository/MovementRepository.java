package com.bank.movement.repository;

import com.bank.movement.domain.Movement;
import com.bank.movement.domain.MovementType;
import com.bank.movement.domain.ProductType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovementRepository extends ReactiveMongoRepository<Movement, String> {
    Mono<Movement> findByIdAndActiveTrue(String id);
    Flux<Movement> findByActiveTrue();
    Flux<Movement> findByCustomerIdAndActiveTrue(String customerId);
    Flux<Movement> findByProductIdAndActiveTrue(String productId);
    Flux<Movement> findByProductIdAndCustomerIdAndActiveTrue(
            String productId,
            String customerId
    );
    Flux<Movement> findByProductIdAndProductTypeAndActiveTrue(
            String productId,
            ProductType productType
    );
    Flux<Movement> findByCustomerIdAndProductTypeAndActiveTrue(
            String customerId,
            ProductType productType
    );
    Flux<Movement> findByCustomerIdAndMovementTypeAndActiveTrue(
            String customerId,
            MovementType movementType
    );
}