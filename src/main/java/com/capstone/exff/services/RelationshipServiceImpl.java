package com.capstone.exff.services;

import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.repositories.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public boolean sendAddRelationshipRequest(int senderId, int receiverId) {
        RelationshipEntity entity = new RelationshipEntity();
        entity.setSenderId(senderId);
        entity.setReceiverId(receiverId);
        entity.setStatus("Waiting");
        try {
            List<RelationshipEntity> res = relationshipRepository.findRelationshipEntitiesByUserId(senderId, receiverId);
            if (res.isEmpty())
                relationshipRepository.save(entity);
            else
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public boolean acceptAddRelationshipRequest(int id, int userId) {
        return updateRelationshipStatus(id, "Accept", userId);
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

    @Transactional
    public boolean updateRelationshipStatus(int id, String status, int userId) {
        try {
            relationshipRepository.acceptRelationshipRequest(id, status, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
