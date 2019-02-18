package com.capstone.exff.controllers;

import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.RelationshipServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RelationshipController {

    private final RelationshipServices relationshipServices;

    @Autowired
    public RelationshipController(RelationshipServices relationshipServices) {
        this.relationshipServices = relationshipServices;
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
                return new ResponseEntity(new ExffMessage("Relationship request has been created"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ExffMessage("Cannot create relationship request"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot create relationship request"), HttpStatus.BAD_REQUEST);
        }
    }
}
