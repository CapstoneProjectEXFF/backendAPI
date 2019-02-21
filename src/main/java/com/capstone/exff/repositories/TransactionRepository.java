package com.capstone.exff.repositories;

import com.capstone.exff.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Override
    <S extends TransactionEntity> S save(S s);
}
