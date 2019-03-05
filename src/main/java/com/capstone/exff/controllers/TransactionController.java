package com.capstone.exff.controllers;

import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.entities.TransactionDetails;
import com.capstone.exff.entities.TransactionEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.TransactionDetailServices;
import com.capstone.exff.services.TransactionServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    private final TransactionServices transactionService;
    private final TransactionDetailServices transactionDetailServices;

    @Autowired
    public TransactionController(TransactionServices transactionService,
                TransactionDetailServices transactionDetailServices) {
        this.transactionService = transactionService;
        this.transactionDetailServices = transactionDetailServices;
    }

    @PostMapping("/transaction")
    public ResponseEntity createTransaction(@RequestBody TransactionRequestWrapper requestWrapper,
                                            ServletRequest servletRequest) {
        TransactionEntity transaction;
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        Timestamp modifiedTime = createTime;
        TransactionDetails transactionDetails = new TransactionDetails();
        try {
            int senderId = getLoginUserId(servletRequest);
            transaction = requestWrapper.getTransaction();
            transaction.setSenderId(senderId);
            int transactionId =
                    transactionService.createTransaction(senderId, transaction.getReceiverId(),
                            transaction.getDonationPostId(), transaction.getStatus(),
                            createTime, modifiedTime);
            transactionDetails.setTransactionId(transactionId);
            transactionDetails.setTransactionDetails(requestWrapper.getDetails());
            transactionDetails.getTransactionDetails().stream()
                    .forEach(t -> transactionDetailServices.createDetailTrans(transactionId, t.getItemId()));
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity( HttpStatus.OK);
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }
}
