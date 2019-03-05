package com.capstone.exff.repositories;

import com.capstone.exff.entities.TransactionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetailEntity, Integer> {

    @Override
    <S extends TransactionDetailEntity> S save(S s);
}
