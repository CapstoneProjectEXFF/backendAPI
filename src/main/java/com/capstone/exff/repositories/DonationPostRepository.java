package com.capstone.exff.repositories;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface DonationPostRepository extends JpaRepository<DonationPostEntity, Integer> {

    @Override
    Page<DonationPostEntity> findAll(Pageable pageable);

    Page<DonationPostEntity> findAllByOrderByModifyTimeDesc(Pageable pageable);

    Page<DonationPostEntity> findByContentContainingOrTitleContaining(String content, String title, Pageable pageable);

    Page<DonationPostEntity> findByContentContainingOrTitleContainingOrderByModifyTimeDesc(String content, String title, Pageable pageable);

    @Override
    Optional<DonationPostEntity> findById(Integer integer);

    @Override
    <S extends DonationPostEntity> S save(S s);

    List<DonationPostEntity> findByUserIdOrderByCreateTimeDesc(Integer integer);
    List<DonationPostEntity> findByUserIdOrderByModifyTimeDesc(Integer integer);

}
