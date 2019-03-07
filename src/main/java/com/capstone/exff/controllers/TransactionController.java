package com.capstone.exff.controllers;

import com.capstone.exff.entities.*;
import com.capstone.exff.services.ItemServices;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    private final TransactionServices transactionService;
    private final TransactionDetailServices transactionDetailServices;
    private final ItemServices itemServices;

    @Autowired
    public TransactionController(TransactionServices transactionService,
                                 TransactionDetailServices transactionDetailServices,
                                 ItemServices itemServices) {
        this.transactionService = transactionService;
        this.itemServices = itemServices;
        this.transactionDetailServices = transactionDetailServices;
    }

    @PostMapping("/transaction")
    public ResponseEntity createTransaction(@RequestBody TransactionRequestWrapper requestWrapper) {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setTransactionDetails(requestWrapper.getDetails());
//        List<ItemEntity> unavailableItems = verifyItemsAvailabity(transactionDetails.getItemIds());
//        if (!unavailableItems.isEmpty()) {
//            return new ResponseEntity(new ExffMessage("There are unavailable items: " + unavailableItems), HttpStatus.OK);
//        }

        TransactionEntity transaction;
        transaction = requestWrapper.getTransaction();
        try {
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            Timestamp modifiedTime = createTime;
            int transactionId =
                    transactionService.createTransaction(transaction.getSenderId(), transaction.getReceiverId(),
                            transaction.getDonationPostId(), transaction.getStatus(),
                            createTime, modifiedTime);
            transactionDetails.setTransactionId(transactionId);
            transactionDetails.getTransactionDetails().stream()
                    .forEach(t -> transactionDetailServices.createDetailTrans(transactionId, t.getItemId()));
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Sended"), HttpStatus.OK);
    }

    private List<ItemEntity> verifyItemsAvailabity(List<Integer> itemIds) {
        List<ItemEntity> result = itemServices.verifyItems(itemIds);
        return result;
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }
}
