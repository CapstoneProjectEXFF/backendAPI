package com.capstone.exff.services;

import com.capstone.exff.entities.RelationshipEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RelationshipServices {
    RelationshipEntity sendAddRelationshipRequest(int senderId, int receiverId);
    boolean acceptAddRelationshipRequest(int id, int userId);
    boolean removeRelationship(int id, int userId);
    List<RelationshipEntity> getAddRelationshipRequest(int userId, Pageable pageable);
    RelationshipEntity checkFriend(int senderId, int receiverId);
}
