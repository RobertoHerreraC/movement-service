package com.bank.movement.service;

import com.bank.movement.api.dto.MovementRequest;
import com.bank.movement.api.dto.MovementResponse;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MovementService {
    Single<MovementResponse> create(MovementRequest request);
    Flowable<MovementResponse> findAll();
    Single<MovementResponse> findById(String id);
    Single<MovementResponse> update(String id, MovementRequest request);
    Completable delete(String id);
    Flowable<MovementResponse> findByCustomerId(String customerId);
    Flowable<MovementResponse> findByProductId(String productId);
    Flowable<MovementResponse> findByProductIdAndCustomerId(
            String productId,
            String customerId
    );
}
