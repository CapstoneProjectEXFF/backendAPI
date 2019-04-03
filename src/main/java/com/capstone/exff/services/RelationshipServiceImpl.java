package com.capstone.exff.services;

import com.capstone.exff.constants.ExffRole;
import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.repositories.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipServices {

    private RelationshipRepository relationshipRepository;

    @Autowired
    public RelationshipServiceImpl(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    @Override
    public RelationshipEntity sendAddRelationshipRequest(int senderId, int receiverId) {
        RelationshipEntity entity = new RelationshipEntity();
        entity.setSenderId(senderId);
        entity.setReceiverId(receiverId);
        entity.setStatus(ExffStatus.RELATIONSHIP_SEND);
        try {
            List<RelationshipEntity> res = relationshipRepository.findRelationshipEntitiesByUserId(senderId, receiverId);
            if (res.isEmpty())
                entity = relationshipRepository.save(entity);
            else
                return null;
        } catch (Exception e) {
            return null;
        }
        return entity;
    }


    @Transactional
    @Override
    public boolean acceptAddRelationshipRequest(int id, int userId) {
        return updateRelationshipStatus(id, ExffStatus.RELATIONSHIP_ACCEPTED, userId);
    }

    @Override
    public boolean removeRelationship(int id) {
        try {
            relationshipRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<RelationshipEntity> getAddRelationshipRequest(int userId, Pageable pageable) {
        return relationshipRepository.findAllByReceiverIdAndStatus(userId, ExffStatus.RELATIONSHIP_SEND, pageable).getContent();
    }

    @Override
    public RelationshipEntity checkFriend(int senderId, int receiverId) {
        try {
            List<RelationshipEntity> res = relationshipRepository.findRelationshipEntitiesByUserId(senderId, receiverId);
            if (res.isEmpty()) {
                return null;
            } else return res.get(0);
        } catch (Exception e) {
            return null;
        }
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
