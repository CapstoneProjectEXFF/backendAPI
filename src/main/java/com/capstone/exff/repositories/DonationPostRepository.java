package com.capstone.exff.repositories;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationPostRepository extends JpaRepository<DonationPostEntity, Integer> {

    @Override
    <S extends DonationPostEntity> S save(S s);

}
