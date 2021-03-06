package com.capstone.exff.repositories;

import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface RelationshipRepository extends CrudRepository<RelationshipEntity, Integer> {
    @Override
    <S extends RelationshipEntity> S save(S s);

    Page<RelationshipEntity> findAllByReceiverIdAndStatus(int receiverId, String status, Pageable pageable);

    @Query("select r " +
            "from RelationshipEntity r " +
            "where (r.sender.fullName like concat('%', :fullName, '%') " +
            "or r.receiver.fullName like concat('%', :fullName, '%')) " +
            "and r.status = :status")
    Page<RelationshipEntity> findByUserFullName(@Param("fullName") String fullName, @Param("status") String status, Pageable pageable);

    List<RelationshipEntity> findAllBySenderIdAndStatus(@Param("senderId") int senderId, @Param("status") String status);

    @Query("SELECT r FROM RelationshipEntity r WHERE r.status = :status AND (r.receiverId = :userId OR r.senderId = :userId)")
    List<RelationshipEntity> findFriendByUserId(int userId, String status);

    @Query("SELECT r " +
            "FROM RelationshipEntity r " +
            "WHERE r.status = :status and (r.senderId = :senderId and r.receiverId = :receiverId) " +
            "OR (r.receiverId = :senderId and r.senderId = :receiverId) ")
    RelationshipEntity findFriendRelationshipByUserId(@Param("senderId") int senderId, @Param("receiverId") int receiverId, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("delete from RelationshipEntity r where r.id = :id and (r.receiverId = :userId or r.senderId = :userId)")
    void deleteByIdAndUserId(@Param("id") int id, @Param("userId") int userId);


    @Modifying
    @Query("delete from RelationshipEntity r where (r.senderId = :userId1 and r.receiverId = :userId2) or (r.receiverId = :userId1 and  r.senderId = :userId2)")
    void deleteByUserID(int userId1, int userId2);


    @Modifying
    @Query("update RelationshipEntity r set r.status = :status where r.id = :id and r.receiverId = :userId")
    void acceptRelationshipRequest(@Param("id") Integer id, @Param("status") String status, @Param("userId") Integer userId);

    @Query("SELECT r " +
            "FROM RelationshipEntity r " +
            "WHERE (r.senderId = :senderId and r.receiverId = :receiverId) " +
            "OR (r.receiverId = :senderId and r.senderId = :receiverId) ")
    List<RelationshipEntity> findRelationshipEntitiesByUserId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);


    @Query("SELECT i FROM UserEntity i WHERE i.id IN :ids AND i.id NOT IN (SELECT r.senderId FROM RelationshipEntity r WHERE r.receiverId = :userId) AND i.id NOT IN (SELECT r.receiverId FROM RelationshipEntity r WHERE r.senderId = :userId)")
    List<UserEntity> getNotFriendUserFromPhoneUserList(int userId, List<Integer> ids);

    @Query("SELECT i FROM UserEntity i WHERE i.id NOT IN (SELECT r.senderId FROM RelationshipEntity r WHERE r.receiverId = :userId) AND i.id NOT IN (SELECT r.receiverId FROM RelationshipEntity r WHERE r.senderId = :userId)")
    List<UserEntity> getNewUsersToAddFriendByUserId(int userId);

    @Query("SELECT i FROM UserEntity i WHERE (i.id IN (SELECT r.senderId FROM RelationshipEntity r WHERE r.status = :status AND r.receiverId = :userId1) OR i.id IN (SELECT r.receiverId FROM RelationshipEntity r WHERE r.status = :status AND r.senderId = :userId1))" +
            " AND (i.id IN (SELECT r.senderId FROM RelationshipEntity r WHERE r.status = :status AND r.receiverId = :userId2) OR i.id IN  (SELECT r.receiverId FROM RelationshipEntity r WHERE r.status = :status AND r.senderId = :userId2)) ")
    List<UserEntity> getMutualFriends(int userId1, int userId2, String status);

    @Query("SELECT r.id FROM RelationshipEntity r WHERE (r.senderId = :userId1 and r.receiverId = :userId2) or (r.receiverId = :userId1 and  r.senderId = :userId2)")
    List<Integer> getRelationshipByUserID(int userId1, int userId2);
}
