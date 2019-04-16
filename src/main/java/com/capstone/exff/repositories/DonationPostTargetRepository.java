package com.capstone.exff.repositories;

import com.capstone.exff.entities.DonationPostTargetEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DonationPostTargetRepository extends CrudRepository<DonationPostTargetEntity, Integer> {

    List<DonationPostTargetEntity> findByDonationPostId(int donationPostId);

    @Override
    <S extends DonationPostTargetEntity> S save(S s);

    @Override
    <S extends DonationPostTargetEntity> Iterable<S> saveAll(Iterable<S> iterable);

    @Override
    void deleteById(Integer integer);
}
