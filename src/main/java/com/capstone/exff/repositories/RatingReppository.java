package com.capstone.exff.repositories;

import com.capstone.exff.entities.RatingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingReppository extends CrudRepository<RatingEntity, Integer> {
    List<RatingEntity> findByReceiverIdOrderByCreateTimeDesc(int receiverId);

    @Override
    <S extends RatingEntity> S save(S s);
}
