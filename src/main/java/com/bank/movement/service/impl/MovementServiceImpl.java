package com.bank.movement.service.impl;

import com.bank.movement.api.dto.MovementRequest;
import com.bank.movement.api.dto.MovementResponse;
import com.bank.movement.domain.Movement;
import com.bank.movement.domain.MovementType;
import com.bank.movement.domain.ProductType;
import com.bank.movement.exception.BusinessRuleException;
import com.bank.movement.exception.MovementNotFoundException;
import com.bank.movement.mapper.MovementMapper;
import com.bank.movement.repository.MovementRepository;
import com.bank.movement.service.MovementService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper;

    @Override
    public Single<MovementResponse> create(MovementRequest request) {
        log.info("Creating movement for productId: {}", request.getProductId());

        validateMovementRequest(request);

        Movement movement = movementMapper.toEntity(request);
//        return Single.fromPublisher(movementRepository.insert(movement))
//                .map(movementMapper::toResponse)
//                .doOnSuccess(response ->
//                        log.info("Movement created successfully with id: {}", response.getId()));

        Single<Movement> mov =  Single.fromPublisher(movementRepository.insert(movement));
        Single<MovementResponse> res =       mov.map(movementMapper::toResponse)
                .doOnSuccess(response ->
                        log.info("Movement created successfully with id: {}", response.getId()));

        return res;
    }

    @Override
    public Flowable<MovementResponse> findAll() {
        log.info("Finding all active movements");

        return Flowable.fromPublisher(movementRepository.findByActiveTrue())
                .map(movementMapper::toResponse);
    }

    @Override
    public Single<MovementResponse> findById(String id) {
        log.info("Finding movement by id: {}", id);
        return Single.fromPublisher(
                        movementRepository.findByIdAndActiveTrue(id)
                                .switchIfEmpty(Mono.error(new MovementNotFoundException(id)))
                )
                .map(movementMapper::toResponse);
    }

    @Override
    public Single<MovementResponse> update(String id, MovementRequest request) {
        log.info("Updating movement with id: {}", id);

        validateMovementRequest(request);
        return Single.fromPublisher(
                        movementRepository.findByIdAndActiveTrue(id)
                                .switchIfEmpty(Mono.error(new MovementNotFoundException(id)))
                )
                .map(existingMovement ->
                        movementMapper.updateEntity(existingMovement, request)
                )
                .flatMap(updatedMovement ->
                        Single.fromPublisher(movementRepository.save(updatedMovement))
                )
                .map(movementMapper::toResponse)
                .doOnSuccess(response ->
                        log.info("Movement updated successfully with id: {}", response.getId()));
    }

    @Override
    public Completable delete(String id) {
        log.info("Deleting movement logically with id: {}", id);
        return Single.fromPublisher(
                        movementRepository.findByIdAndActiveTrue(id)
                                .switchIfEmpty(Mono.error(new MovementNotFoundException(id)))
                )
                .map(movement -> {
                    movement.setActive(Boolean.FALSE);
                    return movement;
                })
                .flatMap(movement ->
                        Single.fromPublisher(movementRepository.save(movement))
                )
                .ignoreElement();
    }

    @Override
    public Flowable<MovementResponse> findByCustomerId(String customerId) {
        log.info("Finding movements by customerId: {}", customerId);
        return Flowable.fromPublisher(
                        movementRepository.findByCustomerIdAndActiveTrue(customerId)
                )
                .map(movementMapper::toResponse);
    }

    @Override
    public Flowable<MovementResponse> findByProductId(String productId) {
        log.info("Finding movements by productId: {}", productId);
        return Flowable.fromPublisher(
                        movementRepository.findByProductIdAndActiveTrue(productId)
                )
                .map(movementMapper::toResponse);
    }

    @Override
    public Flowable<MovementResponse> findByProductIdAndCustomerId(
            String productId,
            String customerId) {

        log.info("Finding movements by productId: {} and customerId: {}",
                productId,
                customerId);
        return Flowable.fromPublisher(
                        movementRepository.findByProductIdAndCustomerIdAndActiveTrue(
                                productId,
                                customerId
                        )
                )
                .map(movementMapper::toResponse);
    }

    private void validateMovementRequest(MovementRequest request) {
        validateAmount(request);
        validateProductType(request.getProductType());
        validateMovementType(request.getMovementType());
    }

    private void validateAmount(MovementRequest request) {
        BigDecimal amount = BigDecimal.valueOf(request.getAmount());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Movement amount must be greater than zero");
        }
    }

    private void validateProductType(String productType) {
        try {
            ProductType.valueOf(productType);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new BusinessRuleException("Invalid product type: " + productType);
        }
    }

    private void validateMovementType(String movementType) {
        try {
            MovementType.valueOf(movementType);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new BusinessRuleException("Invalid movement type: " + movementType);
        }
    }
}