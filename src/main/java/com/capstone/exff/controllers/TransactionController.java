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

    @GetMapping("/transaction")
    public ResponseEntity getTransactionByUserId(ServletRequest servletRequest) {
        List<TransactionEntity> transactionEntities;
        try {
            int receiverId = getLoginUserId(servletRequest);
            transactionEntities = transactionService.getTopTransactionByReceiverId(receiverId);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(transactionEntities, HttpStatus.OK);
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
        return new ResponseEntity(new ExffMessage("Sended"), HttpStatus.OK);
    }

    @PutMapping("/transaction")
    public ResponseEntity updateTransaction(@RequestBody TransactionRequestWrapper requestWrapper,
                                            ServletRequest servletRequest) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            TransactionEntity transaction = requestWrapper.getTransaction();
            if (loginUserId == transaction.getReceiverId()) {
                swapUserId(transaction);
            }
            transaction.setModifyTime(new Timestamp(System.currentTimeMillis()));
            transactionService.updateTransaction(transaction);

            List<TransactionDetailEntity> transactionDetails = requestWrapper.getDetails();
            transactionDetails.stream()
                    .forEach((transactionDetail) -> {
                        if (transactionDetail.getTransactionId() == null)
                            transactionDetailServices.deleteTransactionDetail(transactionDetail);
                        else {
                            transactionDetail.setTransactionId(transaction.getId());
                            transactionDetailServices.updateTransactionDetail(transactionDetail);
                        }
                    });
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Updated and resend"), HttpStatus.OK);
    }

    @DeleteMapping("/transaction")
    public ResponseEntity cancelTransaction(@RequestBody TransactionRequestWrapper requestWrapper,
                                            ServletRequest servletRequest) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            TransactionEntity transaction = requestWrapper.getTransaction();
            if (loginUserId == transaction.getReceiverId()) {
                transactionDetailServices.deleteTransactionDetailByTransactionId(transaction.getId());
                transactionService.deleteTransaction(transaction);
            } else {
                return new ResponseEntity(new ExffMessage("Not permission"), HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Deleted"), HttpStatus.OK);
    }

    private void swapUserId(TransactionEntity transaction) {
        Integer tmpSenderId = transaction.getSenderId();
        transaction.setSenderId(transaction.getReceiverId());
        transaction.setReceiverId(tmpSenderId);
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }
}
