package com.capstone.exff.repositories;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DonationPostRepository extends JpaRepository<DonationPostEntity, Integer> {

    @Override
    <S extends DonationPostEntity> S save(S s);

    @Query("select d from DonationPostEntity d where d.id = :donationPostId")
    DonationPostEntity getDonationPostById(int donationPostId);

}
