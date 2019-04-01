package com.capstone.exff.controllers;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.RelationshipServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;

@RestController
public class RelationshipController {

    private final RelationshipServices relationshipServices;

    @Autowired
    public RelationshipController(RelationshipServices relationshipServices) {
        this.relationshipServices = relationshipServices;
    }

    @GetMapping("/relationship")
    public ResponseEntity getRequestAddRelationship(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestAttribute("USER_INFO") UserEntity userEntity
    ) {
        List<RelationshipEntity> relationshipEntities;
        try {
            int userId = userEntity.getId();
            Pageable pageable = PageRequest.of(page, size);
            relationshipEntities = relationshipServices.getAddRelationshipRequest(userId, pageable);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot create relationship request"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(relationshipEntities, HttpStatus.OK);
    }

    @PostMapping("/relationship")
    public ResponseEntity requestAddRelationship(@RequestBody Map<String, String> body, @RequestAttribute("USER_INFO") UserEntity userEntity) {
        try {
            int senderId = userEntity.getId();
            int receiverId = Integer.parseInt(body.get("receiverId"));
            boolean res = relationshipServices.sendAddRelationshipRequest(senderId, receiverId);
            if (res) {
                return new ResponseEntity(new ExffMessage("Relationship request has been created"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ExffMessage("Cannot create relationship request"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot create relationship request"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/relationship")
    public ResponseEntity acceptRelationship(@RequestBody Map<String, String> body, @RequestAttribute("USER_INFO") UserEntity userEntity) {
        try {
            int id = Integer.parseInt(body.get("id"));
            int userId = userEntity.getId();
            boolean res = relationshipServices.acceptAddRelationshipRequest(id, userId);
            if (res) {
                return new ResponseEntity(new ExffMessage("Relationship request has been accepted"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ExffMessage("Cannot accept relationship request"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot accept relationship request"), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/relationship/{userId:[\\d]+}")
    public ResponseEntity checkRelationship(ServletRequest servletRequest, @PathVariable("userId") int userId) {
        try {
            int senderId = getLoginUserId(servletRequest);
            System.out.println("test senderID " + senderId);
            int receiverId = userId;
            String check = relationshipServices.checkFriend(senderId, receiverId);
            switch (check) {
                case ExffStatus.RELATIONSHIP_ACCEPTED:
                    return new ResponseEntity(new ExffMessage("Friend"), HttpStatus.OK);
                case "0":
                    return new ResponseEntity(new ExffMessage("Not Friend"), HttpStatus.OK);
                case ExffStatus.RELATIONSHIP_SEND:
                    return new ResponseEntity(new ExffMessage("Request Sent"), HttpStatus.OK);
                case "-1":
                    return new ResponseEntity(new ExffMessage("Can not check"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(new ExffMessage("Can not check"), HttpStatus.BAD_REQUEST);
    }


    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }

    @DeleteMapping("/relationship/{id:[\\d]+}")
    public ResponseEntity deleteRequest(ServletRequest servletRequest, @PathVariable("id") int id) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            RelationshipEntity relationshipEntity = relationshipServices.getRelationshipByRelationshipId(id);
            if (loginUserId == relationshipEntity.getReceiverId() || loginUserId == relationshipEntity.getSenderId()) {
                relationshipServices.deleteRelationship(relationshipEntity);
            } else {
                return new ResponseEntity(new ExffMessage("Not permission"), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Deleted"), HttpStatus.OK);
    }

}
