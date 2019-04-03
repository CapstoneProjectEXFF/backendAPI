package com.capstone.exff.repositories;

import com.capstone.exff.entities.RelationshipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepository extends CrudRepository<RelationshipEntity, Integer> {
    @Override
    <S extends RelationshipEntity> S save(S s);

    Page<RelationshipEntity> findAllByReceiverIdAndStatus(int receiverId, String status, Pageable pageable);

    @Modifying
    @Query("delete from RelationshipEntity r where r.id = :id and (r.receiverId = :userId or r.senderId = :userId)")
    void deleteByIdAndUserId(@Param("id") int id,@Param("userId") int userId);

    @Modifying
    @Query("update RelationshipEntity r set r.status = :status where r.id = :id and r.receiverId = :userId")
    void acceptRelationshipRequest(@Param("id") Integer id,@Param("status") String status, @Param("userId") Integer userId);

    @Query("SELECT r " +
            "FROM RelationshipEntity r " +
            "WHERE (r.senderId = :senderId and r.receiverId = :receiverId) " +
            "OR (r.receiverId = :senderId and r.senderId = :receiverId) ")
    List<RelationshipEntity> findRelationshipEntitiesByUserId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
}
