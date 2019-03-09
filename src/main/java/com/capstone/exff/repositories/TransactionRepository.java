package com.capstone.exff.repositories;

import com.capstone.exff.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Override
    Optional<TransactionEntity> findById(Integer integer);

    @Override
    <S extends TransactionEntity> S save(S s);

    List<TransactionEntity> findTop10ByReceiverIdOrderByCreateTime(int receiverId);

    @Override
    void delete(TransactionEntity transactionEntity);
}
