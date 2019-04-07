package com.capstone.exff.services;

import com.capstone.exff.constants.ExffRole;
import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipServices {

    private RelationshipRepository relationshipRepository;

    @Autowired
    public RelationshipServiceImpl(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    @Override
    public boolean sendAddRelationshipRequest(int senderId, int receiverId) {
        RelationshipEntity entity = new RelationshipEntity();
        entity.setSenderId(senderId);
        entity.setReceiverId(receiverId);
        entity.setStatus(ExffStatus.RELATIONSHIP_SEND);
        try {
            List<RelationshipEntity> res = relationshipRepository.findRelationshipEntitiesByUserId(senderId, receiverId);
            if (res.isEmpty())
                entity = relationshipRepository.save(entity);
            else
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Transactional
    @Override
    public boolean acceptAddRelationshipRequest(int id, int userId) {
        return updateRelationshipStatus(id, ExffStatus.RELATIONSHIP_ACCEPTED, userId);
    }

    @Override
    public boolean removeRelationship(int id) {
        return false;
    }

//    @Transactional
//    @Override
//    public boolean removeRelationship(int id, int userId) {
//        try {
//            relationshipRepository.deleteByIdAndUserId(id, userId);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public List<RelationshipEntity> getAddRelationshipRequest(int userId, Pageable pageable) {
        return relationshipRepository.findAllByReceiverIdAndStatus(userId, ExffStatus.RELATIONSHIP_SEND, pageable).getContent();
    }

    @Override
    public List<RelationshipEntity> getAcceptedFriendRequestByUserId(int userId) {
        return relationshipRepository.findAllBySenderIdAndStatus(userId, ExffStatus.RELATIONSHIP_ACCEPTED);
    }

    @Override
    public List<RelationshipEntity> getFriendsByUserId(int userId) {
        return relationshipRepository.findAllBySenderIdOrReceiverIdAndStatus(userId, userId, ExffStatus.RELATIONSHIP_ACCEPTED);
    }

    @Override
    public int countFriendsByUserId(int userId) {
        return relationshipRepository.findAllBySenderIdOrReceiverIdAndStatus(userId, userId, ExffStatus.RELATIONSHIP_ACCEPTED).size();
    }



    @Override
    public RelationshipEntity getRelationshipByRelationshipId(int relationshipId) {
        return relationshipRepository.findById(relationshipId).get();
    }

    @Override
    public RelationshipEntity getFriendRelationshipByUserId(int firstID, int secondID) {
        return relationshipRepository.findFriendRelationshipByUserId(firstID, secondID, ExffStatus.RELATIONSHIP_ACCEPTED);
    }

    @Override
    public void deleteRelationship(RelationshipEntity relationshipEntity) {
        relationshipRepository.delete(relationshipEntity);
    }

    @Override
    public List<UserEntity> getNotFriendUserFromPhoneUserList(int userId, List<Integer> userIdList) {
        return relationshipRepository.getNotFriendUserFromPhoneUserList(userId, userIdList);
    }

    @Override
    public List<UserEntity> getNewUsersToAddFriendByUserId(int userId) {
        return relationshipRepository.getNewUsersToAddFriendByUserId(userId);
    }

    @Transactional
    public boolean updateRelationshipStatus(int id, String status, int userId) {
        try {
            relationshipRepository.acceptRelationshipRequest(id, status, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
