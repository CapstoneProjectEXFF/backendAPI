package com.capstone.exff.services;

import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.TransactionEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RelationshipServices {
    boolean sendAddRelationshipRequest(int senderId, int receiverId);

    boolean acceptAddRelationshipRequest(int id, int userId);

    boolean removeRelationship(int id);

    List<RelationshipEntity> getAddRelationshipRequest(int userId, Pageable pageable);

    List<RelationshipEntity> getAcceptedFriendRequestByUserId(int userId);

    List<RelationshipEntity> getFriendsByUserId(int userId);

    int countFriendsByUserId(int userId);

    String checkFriend(int senderId, int receiverId);

    RelationshipEntity getRelationshipByRelationshipId(int relationshipId);

    RelationshipEntity getFriendRelationshipByUserId(int firstID, int secondID);

    void deleteRelationship(RelationshipEntity relationshipEntity);
}
