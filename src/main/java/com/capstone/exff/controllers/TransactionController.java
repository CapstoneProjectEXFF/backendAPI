package com.capstone.exff.controllers;

import com.capstone.exff.entities.TransactionEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.TransactionServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Map;

@RestController
public class TransactionController {

    private final TransactionServices transactionService;

    @Autowired
    public TransactionController(TransactionServices transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity createTransaction(@RequestBody Map<String, String> body,
                                            ServletRequest servletRequest) {
        TransactionEntity transaction;
        try {
            int senderId = getLoginUserId(servletRequest);
            int receiverId = Integer.parseInt(body.get("receiverId"));
            int donationId = Integer.parseInt(body.get("donationId"));
            String status = body.get("status");
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            Timestamp modifiedTime = createTime;
            transaction = transactionService.createTransaction(
                    senderId, receiverId, donationId,
                    status, createTime, modifiedTime);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(transaction, HttpStatus.OK);
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }
}
