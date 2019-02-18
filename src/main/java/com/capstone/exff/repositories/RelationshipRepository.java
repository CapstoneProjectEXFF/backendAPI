package com.capstone.exff.repositories;

import com.capstone.exff.entities.RelationshipEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepository extends CrudRepository<RelationshipEntity, Integer> {
    @Override
    <S extends RelationshipEntity> S save(S s);

    @Override
    void deleteById(Integer integer);

    @Modifying
    @Query("UPDATE RelationshipEntity r set r.status = :status where r.id = :id and (r.senderId = :userId or r.receiverId = :userId)")
    void acceptRelationshipRequest(@Param("id") int id,@Param("status") String status, @Param("userId") int userId);

    @Query("SELECT r " +
            "FROM RelationshipEntity r " +
            "WHERE (r.senderId = :senderId and r.receiverId = :receiverId) " +
            "OR (r.receiverId = :senderId and r.senderId = :receiverId) ")
    List<RelationshipEntity> findRelationshipEntitiesByUserId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
}
