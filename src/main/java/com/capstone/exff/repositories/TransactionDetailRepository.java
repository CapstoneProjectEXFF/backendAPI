package com.capstone.exff.repositories;

import com.capstone.exff.entities.TransactionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetailEntity, Integer> {

    @Override
    <S extends TransactionDetailEntity> S save(S s);

    List<TransactionDetailEntity> findAllByTransactionId(int transactionId);

    @Override
    void delete(TransactionDetailEntity transactionDetailEntity);

    @Transactional
    void deleteByTransactionId(int transactionId);
}
