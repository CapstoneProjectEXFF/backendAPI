package com.capstone.exff.repositories;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonationPostRepository extends JpaRepository<DonationPostEntity, Integer> {

    @Override
    Optional<DonationPostEntity> findById(Integer integer);

    @Override
    <S extends DonationPostEntity> S save(S s);

}
