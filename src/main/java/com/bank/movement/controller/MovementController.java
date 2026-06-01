package com.bank.movement.controller;

import com.bank.movement.api.dto.MovementRequest;
import com.bank.movement.api.dto.MovementResponse;
import com.bank.movement.api.generated.MovementsApi;
import com.bank.movement.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MovementController implements MovementsApi {

    private final MovementService movementService;

    @Override
    public Mono<ResponseEntity<MovementResponse>> createMovement(
            @Valid Mono<MovementRequest> movementRequest,
            ServerWebExchange exchange) {
        return movementRequest
                .flatMap(request ->
                        Mono.fromCompletionStage(
                                movementService.create(request).toCompletionStage()
                        )
                )
                .map(response ->
                        ResponseEntity.status(HttpStatus.CREATED).body(response)
                );
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> findAllMovements(
            ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.ok(
                        Flux.from(movementService.findAll())
                )
        );
    }

    @Override
    public Mono<ResponseEntity<MovementResponse>> findMovementById(
            String id,
            ServerWebExchange exchange) {
        return Mono.fromCompletionStage(
                        movementService.findById(id).toCompletionStage()
                )
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<MovementResponse>> updateMovement(
            String id,
            @Valid Mono<MovementRequest> movementRequest,
            ServerWebExchange exchange) {
        return movementRequest
                .flatMap(request ->
                        Mono.fromCompletionStage(
                                movementService.update(id, request).toCompletionStage()
                        )
                )
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteMovement(
            String id,
            ServerWebExchange exchange) {
        return Mono.fromCompletionStage(
                        movementService.delete(id).toCompletionStage(null)
                )
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> findMovementsByCustomerId(
            String customerId,
            ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.ok(
                        Flux.from(movementService.findByCustomerId(customerId))
                )
        );
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> findMovementsByProductId(
            String productId,
            ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.ok(
                        Flux.from(movementService.findByProductId(productId))
                )
        );
    }

    @Override
    public Mono<ResponseEntity<Flux<MovementResponse>>> findMovementsByProductIdAndCustomerId(
            String productId,
            String customerId,
            ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.ok(
                        Flux.from(
                                movementService.findByProductIdAndCustomerId(
                                        productId,
                                        customerId
                                )
                        )
                )
        );
    }
}