package com.capstone.exff.services;

import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RelationshipServices {
    RelationshipEntity sendAddRelationshipRequest(int senderId, int receiverId);

    boolean acceptAddRelationshipRequest(int id, int userId);

    boolean removeRelationship(int id);

    List<RelationshipEntity> getAcceptedRelationshipByFullname(String fullName, int page, int size);

    List<RelationshipEntity> getAddRelationshipRequest(int userId, Pageable pageable);

    List<RelationshipEntity> getAcceptedFriendRequestByUserId(int userId);

    List<RelationshipEntity> getFriendsByUserId(int userId);

    int countFriendsByUserId(int userId);

    RelationshipEntity checkFriend(int senderId, int receiverId);

    RelationshipEntity getRelationshipByRelationshipId(int relationshipId);

    RelationshipEntity getFriendRelationshipByUserId(int firstID, int secondID);

    void deleteRelationship(RelationshipEntity relationshipEntity);

    List<UserEntity> getNotFriendUserFromPhoneUserList(int userId, List<Integer> userIdList);

    List<UserEntity> getNewUsersToAddFriendByUserId(int userId);

    List<UserEntity> getMutualFriendFromUserID(int userId1, int userId2);
}
