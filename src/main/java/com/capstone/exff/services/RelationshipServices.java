package com.capstone.exff.services;

public interface RelationshipServices {
    boolean sendAddRelationshipRequest(int senderId, int receiverId);
    boolean acceptAddRelationshipRequest(int id, int userId);
    boolean removeRelationship(int id);
}
