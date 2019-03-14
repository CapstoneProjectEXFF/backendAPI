package com.capstone.exff.services;

import com.capstone.exff.entities.TransactionEntity;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionServices {
    List<TransactionEntity> getTopTransactionByReceiverId(int receiverId);
    int createTransaction(int senderId, int receiverId, int donationId,
                                 String status, Timestamp createTime,
                                 Timestamp modifiedTime);
    TransactionEntity updateTransaction(TransactionEntity transactionEntity);
    void deleteTransaction(TransactionEntity transactionEntity);
    TransactionEntity getTransactionByTransactionId(int transactionId);
    boolean isValidTransaction(int userId, int transactionId);
    void confirmTransaction(int transactionId);
}
