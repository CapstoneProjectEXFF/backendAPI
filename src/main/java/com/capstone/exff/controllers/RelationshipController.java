package com.capstone.exff.controllers;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.RelationshipEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.RelationshipRepository;
import com.capstone.exff.services.RelationshipServices;
import com.capstone.exff.services.UserServices;
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
import java.util.*;

@RestController
public class RelationshipController {

    private final RelationshipServices relationshipServices;
    private final UserServices userServices;

    @Autowired
    public RelationshipController(RelationshipServices relationshipServices, UserServices userServices) {
        this.relationshipServices = relationshipServices;
        this.userServices = userServices;
    }


    @GetMapping("/relationship/accepted")
    public ResponseEntity getAcceptedFriendRequestByUserId(@RequestAttribute("USER_INFO") UserEntity userEntity) {
        List<RelationshipEntity> relationshipEntities;
        try {
            int userId = userEntity.getId();
            relationshipEntities = relationshipServices.getAcceptedFriendRequestByUserId(userId);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot create relationship request"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(relationshipEntities, HttpStatus.OK);
    }

    @GetMapping("/relationship/friend")
    public ResponseEntity getFriendsByUserId(@RequestAttribute("USER_INFO") UserEntity userEntity) {
        List<RelationshipEntity> relationshipEntities;
        List<UserEntity> friendList = new ArrayList<>();
        try {
            int userId = userEntity.getId();
            relationshipEntities = relationshipServices.getFriendsByUserId(userId);

            for (int i = 0; i < relationshipEntities.size(); i++) {
                if (relationshipEntities.get(i).getSenderId() == userId) {
                    friendList.add(relationshipEntities.get(i).getReceiver());
                } else {
                    friendList.add(relationshipEntities.get(i).getSender());
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot get relationship"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(relationshipEntities, HttpStatus.OK);
    }

    @GetMapping("/relationship/friend/count")
    public ResponseEntity countFriendsByUserId(@RequestAttribute("USER_INFO") UserEntity userEntity) {
        int count;
        try {
            int userId = userEntity.getId();
            count = relationshipServices.countFriendsByUserId(userId);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot count friend"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(count, HttpStatus.OK);
    }

    @GetMapping("/relationship/friend/mutual")
    public ResponseEntity getMutual(@RequestAttribute("USER_INFO") UserEntity userEntity, @RequestBody Map<String, String> body) {
        List<UserEntity> userEntityList;
        List<UserEntity> friendList = new ArrayList<>();
        try {
            int userId = userEntity.getId();
            int userId2 = Integer.parseInt(body.get("id"));
            userEntityList = relationshipServices.getMutualFriendFromUserID(userId, userId2);

        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Cannot get relationship"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(userEntityList, HttpStatus.OK);
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

    @PostMapping("/relationship/contact")
    public ResponseEntity getUserFromContact(ServletRequest servletRequest, @RequestBody ArrayList<String> body) {
        int userID = getLoginUserId(servletRequest);
        List<UserEntity> allUser = new ArrayList<>();
        List<UserEntity> userList = new ArrayList<>();
        List<Integer> userIdList = new ArrayList<>();
        try {
            allUser = userServices.findUsersbyPhoneNumberList(body);
            if (allUser != null) {
                for (int i = 0; i < allUser.size(); i++) {
                    userIdList.add(allUser.get(i).getId());
                }
                userList = relationshipServices.getNotFriendUserFromPhoneUserList(userID, userIdList);
            }
        } catch (Exception ex) {
            return new ResponseEntity(new ExffMessage("Cannot import friend"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(userList, HttpStatus.OK);
    }

    @GetMapping("/relationship/explore")
    public ResponseEntity getNewUsersToAddFriend(ServletRequest servletRequest) {
        int userID = getLoginUserId(servletRequest);
        List<UserEntity> userList = new ArrayList<>();
        List<UserEntity> resultList = new ArrayList<>();
        List<RelationshipWrapper> relationshipWrapperList = new ArrayList<>();
        try {
            userList = relationshipServices.getNewUsersToAddFriendByUserId(userID);
            if (userList.size() != 0) {
                for (int i = 0; i < userList.size(); i++) {
                    relationshipWrapperList.add(new RelationshipWrapper(userList.get(i), relationshipServices.getMutualFriendFromUserID(userID, userList.get(i).getId()).size()));
                }
                Collections.sort(relationshipWrapperList, Collections.reverseOrder());
                for (int i = 0; i < relationshipWrapperList.size(); i++) {
                    resultList.add(relationshipWrapperList.get(i).getUser());
                    System.out.println(relationshipWrapperList.get(i).getMutualFriendNumber());
                }
            }
        } catch (Exception ex) {
            return new ResponseEntity(new ExffMessage("Cannot Explore friend"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(resultList, HttpStatus.OK);
    }


    @PostMapping("/relationship")
    public ResponseEntity requestAddRelationship(@RequestBody Map<String, String> body, @RequestAttribute("USER_INFO") UserEntity userEntity) {
        try {
            int senderId = userEntity.getId();
            int receiverId = Integer.parseInt(body.get("receiverId"));
            RelationshipEntity res = relationshipServices.sendAddRelationshipRequest(senderId, receiverId);
            if (res != null) {
                return new ResponseEntity(res, HttpStatus.OK);
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


    @DeleteMapping("/relationship")
    public ResponseEntity removeRelationship(ServletRequest servletRequest, @RequestBody Map<String, String> body) {
        if (body.containsKey("id")) {
            try {
                int senderId = getLoginUserId(servletRequest);
                int id = Integer.parseInt(body.get("id"));
                boolean res = relationshipServices.removeRelationship(id);
                if (res) {
                    return new ResponseEntity(new ExffMessage("Done"), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new ExffMessage("Fail"), HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (body.containsKey("userId")) {
            try {
                int senderId = getLoginUserId(servletRequest);
                int receiverId = Integer.parseInt(body.get("userId"));
                boolean res = relationshipServices.removeRelationshipByUserId(senderId, receiverId);
                if (res) {
                    return new ResponseEntity(new ExffMessage("Done"), HttpStatus.OK);
                } else {
                    return new ResponseEntity(new ExffMessage("Fail"), HttpStatus.BAD_REQUEST);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity(new ExffMessage("Fail"), HttpStatus.BAD_REQUEST);
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }
}
