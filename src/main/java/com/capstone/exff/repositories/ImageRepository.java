package com.capstone.exff.repositories;

import com.capstone.exff.entities.ImageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<ImageEntity, Integer>{

    @Query("select i from ImageEntity i where i.itemId = :itemId")
    List<ImageEntity> getImagesByIdItem(int itemId);

}
